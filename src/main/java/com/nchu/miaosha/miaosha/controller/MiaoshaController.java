package com.nchu.miaosha.miaosha.controller;

import com.nchu.miaosha.bd.domain.User;
import com.nchu.miaosha.common.annotation.AccessLimit;
import com.nchu.miaosha.common.annotation.CheckLogin;
import com.nchu.miaosha.common.enums.CodeMsgEnum;
import com.nchu.miaosha.common.response.ApiResponse;
import com.nchu.miaosha.common.response.CodeMsg;
import com.nchu.miaosha.materials.keys.GoodsKey;
import com.nchu.miaosha.materials.service.GoodsService;
import com.nchu.miaosha.materials.vo.GoodsVo;
import com.nchu.miaosha.miaosha.domain.MiaoshaOrderBill;
import com.nchu.miaosha.miaosha.keys.MiaoshaKey;
import com.nchu.miaosha.miaosha.keys.MiaoshaOrderKey;
import com.nchu.miaosha.miaosha.service.MiaoshaOrderService;
import com.nchu.miaosha.miaosha.service.MiaoshaService;
import com.nchu.miaosha.rabbitmq.MQSender;
import com.nchu.miaosha.rabbitmq.MiaoshaMessage;
import com.nchu.miaosha.redis.service.RedisService;
import com.nchu.miaosha.scm.domain.OrderBill;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName: MiaoshaController
 * @Author: 时间
 * @Description: 执行秒杀操作
 * @Date: 2020/7/15 23:42
 * @Version: 1.0
 * @Version: 2.0 实现InitializingBean接口,实现afterPropertiesSet方法
 */
@Controller
@RequestMapping(value = "/miaosha")
public class MiaoshaController implements InitializingBean {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MiaoshaOrderService miaoshaOrderService;

    @Autowired
    private MQSender mqSender;

    @Autowired
    private MiaoshaService miaoshaService;

    //  解决long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId) 一直访问redis扣减负数,减少redis访问
    private Map<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();

    /**
     * 系统初始化的时候回调该方法
     * 系统初始化，把商品库存数量加载到Redis
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.findAll();
        if (CollectionUtils.isEmpty(goodsVoList)) {
            return;
        }

        for (GoodsVo goodsVo : goodsVoList) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goodsVo.getId(), goodsVo.getStockCount());
            localOverMap.put(goodsVo.getId(), false);
        }

    }

    /**
     * @param model
     * @param user
     * @param goodsId
     * @return
     * @version 3.0 加入消息队列,接口优化
     * @version 4.0 安全优化,秒杀接口地址隐藏,校验前端传过来的path参数
     */
    @RequestMapping(value = "/{path}/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    @CheckLogin
    public ApiResponse<Integer> doMiaosha(Model model, User user, @RequestParam("goodsId") long goodsId, @PathVariable("path") String path) {
        model.addAttribute("user", user);

        //验证path
        boolean checkPath = miaoshaService.checkPath(user, goodsId, path);
        if (!checkPath) {
            return ApiResponse.error(CodeMsg.REQUEST_ILLEGAL);
        }

        //内存标记，减少redis访问
        boolean over = localOverMap.get(goodsId);
        if (over) {
            return ApiResponse.error(CodeMsg.MIAO_SHA_OVER);
        }

        // 在redis中做预减库存
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
        if (stock < 0) { // 存在问题,负数的时候也会访问redis,可以进一步优化
            localOverMap.put(goodsId, true);
            return ApiResponse.error(CodeMsg.MIAO_SHA_OVER);
        }

        // 判断是否秒杀到,这步之前已经做过redis缓存
        MiaoshaOrderBill order = miaoshaOrderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (Objects.nonNull(order)) {
            model.addAttribute("errmsg", CodeMsgEnum.REPEATE_MIAOSHA.getMessage());
            return ApiResponse.error(CodeMsg.REPEATE_MIAOSHA);
        }

        // 进入消息队列
        MiaoshaMessage message = new MiaoshaMessage();
        message.setGoodsId(goodsId);
        message.setUser(user);
        mqSender.sendMiaoshaMessage(message);

        // 返回一个状态表示正在排队中
        return ApiResponse.success(0);
    }

    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    @CheckLogin
    public ApiResponse<Long> miaoshaResult(Model model, User user, @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        long result = miaoshaService.getMiaoshaResult(user.getId(), goodsId);
        return ApiResponse.success(result);
    }

    /**
     * 获取秒杀地址,服务端将生成的path参数写进redis缓存,返回给客户端
     * 安全优化 秒杀接口地址隐藏
     *
     * @param user       登录用户
     * @param goodsId    商品ID
     * @param verifyCode 图形验证码
     * @return
     */
    @AccessLimit(seconds = 5, maxCount = 5, needLogin = false)
    @RequestMapping(value = "/getPath", method = RequestMethod.GET)
    @CheckLogin
    @ResponseBody
    public ApiResponse<String> getPath(User user, @RequestParam("goodsId") long goodsId,
                                       @RequestParam(value = "verifyCode") int verifyCode) {
        // 校验图形验证码
        boolean check = miaoshaService.checkVerifyCode(user, goodsId, verifyCode);
        if (!check) {
            return ApiResponse.error(CodeMsg.REQUEST_ILLEGAL);
        }

        String path = miaoshaService.createMiaoshaPath(user, goodsId);
        return ApiResponse.success(path);
    }

    /**
     * 生成图形验证码参数,返回给客户端
     *
     * @param response
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/getVerifyCode", method = RequestMethod.GET)
    @ResponseBody
    @CheckLogin
    public ApiResponse<String> getVerifyCode(HttpServletResponse response, User user, @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return ApiResponse.error(CodeMsg.SESSION_ERROR);
        }
        try {
            BufferedImage image = miaoshaService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(CodeMsg.MIAOSHA_FAIL);
        }
    }

    /**
     * 测试用,清理数据
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse<Boolean> reset(Model model) {
        List<GoodsVo> goodsList = goodsService.findAll();
        for (GoodsVo goods : goodsList) {
            goods.setStockCount(10);
            redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goods.getId(), 10);
            localOverMap.put(goods.getId(), false);
        }
        redisService.delete(MiaoshaOrderKey.getMiaoshaOrderByUidGid);
        redisService.delete(MiaoshaKey.isGoodsOver);
        miaoshaService.reset(goodsList);
        return ApiResponse.success(true);
    }

    /**
     * @param model
     * @param user
     * @param goodsId
     * @version 2.0 秒杀静态化
     * @return
     *
    //    get 幂等  post 非幂等
    //    @RequestMapping(value = "/do_miaosha", method = RequestMethod.POST)
    //    @ResponseBody
    //    @CheckLogin
    //    public ApiResponse<OrderBill> doMiaosha(Model model, User user, @RequestParam("goodsId") long goodsId) {
    ////       通过@CheckLogin注解的方式判断user是否为空
    ////        if (user == null) { // 用户不存在，返回登录页面
    ////            return ApiResponse.error(CodeMsg.SESSION_ERROR);
    ////        }
    //        model.addAttribute("user", user);
    //        // 判断秒杀库存
    //        // 如果一个用户，同时对2个商品秒杀，可能会出现一个用户秒杀两个商品，可以通过加唯一索引，创建订单的时候抛异常，或者让用户输入验证码，防止秒杀两个商品
    //        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    //        int stock = goods.getStockCount();
    //        if (stock <= 0) {
    //            return ApiResponse.error(CodeMsg.MIAO_SHA_OVER);
    //        }
    //
    //        // 查询秒杀订单，判断是否已经秒杀到了
    //        // 优化，判断是否秒杀的时候，可以从redis缓存中取
    //        MiaoshaOrderBill order = miaoshaOrderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
    //        if (Objects.nonNull(order)) {
    //            model.addAttribute("errmsg", CodeMsgEnum.REPEATE_MIAOSHA.getMessage());
    //            return ApiResponse.error(CodeMsg.REPEATE_MIAOSHA);
    //        }
    //        //1、减库存 2、下订单 3、写入秒杀订单
    //        OrderBill orderInfo = miaoshaService.miaosha(user, goods);
    //        return ApiResponse.success(orderInfo);
    //    }

    /**
     * @param model
     * @param user
     * @param goodsId
     * @version 1.0
     * @return
     */
//    @RequestMapping("/do_miaosha")
//    public String doMiaosha(Model model, User user, @RequestParam("goodsId") long goodsId) {
//        if (user == null) { // 用户不存在，返回登录页面
//            return "login";
//        }
//
//        //判断秒杀库存
//        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//        int stock = goods.getStockCount();
//        if (stock <= 0) {
//            model.addAttribute("errmsg", CodeMsgEnum.MIAO_SHA_OVER.getMessage());
//            return "materials/miaosha_fail";
//        }
//
//        //判断是否已经秒杀到了
//        MiaoshaOrderBill order = miaoshaOrderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
//        if (Objects.nonNull(order)) {
//            model.addAttribute("errmsg", CodeMsgEnum.REPEATE_MIAOSHA.getMessage());
//            return "materials/miaosha_fail";
//        }
//        //1、减库存 2、下订单 3、写入秒杀订单
//        OrderBill orderInfo = miaoshaService.miaosha(user, goods);
//        model.addAttribute("user", user);
//        model.addAttribute("orderInfo", orderInfo);
//        model.addAttribute("goods", goods);
//        return "order/order_detail";
//    }
}
