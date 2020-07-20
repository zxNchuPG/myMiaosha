package com.nchu.miaosha.materials.controller;

import com.nchu.miaosha.bd.domain.User;
import com.nchu.miaosha.bd.service.UserService;
import com.nchu.miaosha.common.annotation.CheckLogin;
import com.nchu.miaosha.common.response.ApiResponse;
import com.nchu.miaosha.materials.keys.GoodsKey;
import com.nchu.miaosha.materials.service.GoodsService;
import com.nchu.miaosha.materials.vo.GoodsDetailVo;
import com.nchu.miaosha.materials.vo.GoodsVo;
import com.nchu.miaosha.redis.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName: GoodsController
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/15 0:05
 * @Version: 1.0
 */
@Controller
@RequestMapping(value = "/goods")
public class GoodsController {
//    @Autowired
//    private UserService userService;

    @Autowired
    private GoodsService goodsService;


    @Autowired
    private RedisService redisService;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 对商品列表页面做缓存(有效期一般比较短,防止一瞬间压力太大)
     * 1. 先从缓存中取
     * 2. 缓存没取到，贼手动渲染
     *
     * @param model
     * @param user
     * @return
     */
    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    @CheckLogin
    public String list(HttpServletRequest request, HttpServletResponse response, Model model, User user) {
        model.addAttribute("user", user);

        // 取缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        // 获取最新信息
        List<GoodsVo> goodsList = goodsService.findAll();
        model.addAttribute("goodsList", goodsList);

        SpringWebContext context = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);

        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("materials/goods_list", context);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }

    /**
     * 跳转到商品列表页面
     *
     * @param model
     * @param user
     * @return
     */
//    @RequestMapping("/to_list")
//    public String list(Model model, User user) {
//        model.addAttribute("user", user);
//        List<GoodsVo> goodsList = goodsService.findAll();
//        model.addAttribute("goodsList", goodsList);
//        return "materials/goods_list";
//    }

    /**
     * 根据商品Id获取商品详情
     * url缓存，和页面缓存区别不大，根据不同的goodsId显示不同的数据
     * 区别在redisService.set
     *
     * @param request
     * @param response
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/to_detail/{goodsId}", produces = "text/html")
    @ResponseBody
    @CheckLogin
    public String detail(HttpServletRequest request, HttpServletResponse response, Model model, User user, @PathVariable("goodsId") long goodsId) {
        model.addAttribute("user", user);

        //取缓存
        String html = redisService.get(GoodsKey.getGoodsDetail, "" + goodsId, String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        //手动渲染
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0; // 秒杀没开始
        int remainSeconds = 0; // 秒杀还多少秒
        if (now < startAt) {
            miaoshaStatus = 0; //秒杀还没开始，倒计时
            remainSeconds = (int) ((startAt - now) / 1000);
        } else if (now > endAt) {
            miaoshaStatus = 2; //秒杀已经结束
            remainSeconds = -1;
        } else { //秒杀进行中
            miaoshaStatus = 1;// 秒杀进行中
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        SpringWebContext context = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("materials/goods_detail", context);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsDetail, "" + goodsId, html);
        }
        return html;
    }

    /**
     * 根据商品Id获取商品详情
     * 页面静态化
     * 对应页面 goods_detail.htm(纯html)
     * 通过goods_list。html的按钮跳转
     *
     * @param request
     * @param response
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    @CheckLogin
    public ApiResponse<GoodsDetailVo> detail2(HttpServletRequest request, HttpServletResponse response, Model model, User user, @PathVariable("goodsId") long goodsId) {

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0; // 秒杀没开始
        int remainSeconds = 0; // 秒杀还多少秒
        if (now < startAt) {
            miaoshaStatus = 0; //秒杀还没开始，倒计时
            remainSeconds = (int) ((startAt - now) / 1000);
        } else if (now > endAt) {
            miaoshaStatus = 2; //秒杀已经结束
            remainSeconds = -1;
        } else { //秒杀进行中
            miaoshaStatus = 1;// 秒杀进行中
            remainSeconds = 0;
        }
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setMiaoshaStatus(miaoshaStatus);
        vo.setRemainSeconds(remainSeconds);
        vo.setUser(user);
        return ApiResponse.success(vo);
    }

    /**
     * 根据商品Id获取商品详情
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
//    @RequestMapping("/to_detail/{goodsId}")
//    public String detail(Model model, User user, @PathVariable("goodsId") long goodsId) {
//        model.addAttribute("user", user);
//
//        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//        model.addAttribute("goods", goods);
//
//        long startDate = goods.getStartDate().getTime(); // 获取秒杀开始时间，转换成毫秒
//        long endDate = goods.getEndDate().getTime(); // 获取秒杀结束时间，转换成毫秒
//        long now = System.currentTimeMillis(); // 获取当前系统时间
//
//        int miaoshaStatus = 0; // 秒杀没开始
//        int remainSeconds = 0; // 秒杀还多少秒
//        if (now < startDate) { //秒杀还没开始，倒计时
//            miaoshaStatus = 0;
//            remainSeconds = (int) ((startDate - now) / 1000);
//        } else if (now > endDate) { //秒杀已经结束
//            miaoshaStatus = 2; // 秒杀结束
//            remainSeconds = -1;
//        } else { //秒杀进行中
//            miaoshaStatus = 1; // 秒杀进行中
//            remainSeconds = 0;
//        }
//        model.addAttribute("miaoshaStatus", miaoshaStatus);
//        model.addAttribute("remainSeconds", remainSeconds);
//        return "materials/goods_detail";
//    }

    /**
     * 该方法获取cookie的方式转到UserArgumentResolver实现 <br>
     * 该方法主要测试UserArgumentResolver是否生效
     *
     * @param model
     * @param user
     * @return
     */
//    @RequestMapping("/to_list")
//    public String list(Model model, User user) {
//        model.addAttribute("user", user);
//        return "materials/goods_list";
//    }

    /**
     * 该方式通过获取cookie方式判断有效期，以及延长cookie有效期
     */
//    @RequestMapping("/to_list")
//    public String list(HttpServletResponse response, Model model,
//                       @CookieValue(value = UserService.COOKIE_NAME_TOKEN, required = false) String cookieToken,
//                       @RequestParam(value = UserService.COOKIE_NAME_TOKEN, required = false) String paramToken) {
//
//        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) { // cookie不存在，跳转到登录页面
//            return "login";
//        }
//
//        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
//        User user = userService.getByToken(response, token);
//
//        model.addAttribute("user", user);
//        return "materials/goods_list";
//    }
}
