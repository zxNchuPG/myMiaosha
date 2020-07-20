package com.nchu.miaosha.miaosha.controller;

import com.nchu.miaosha.bd.domain.User;
import com.nchu.miaosha.bd.service.UserService;
import com.nchu.miaosha.common.annotation.CheckLogin;
import com.nchu.miaosha.common.response.ApiResponse;
import com.nchu.miaosha.common.response.CodeMsg;
import com.nchu.miaosha.materials.service.GoodsService;
import com.nchu.miaosha.materials.vo.GoodsVo;
import com.nchu.miaosha.miaosha.vo.MiaoshaOrderDetailVo;
import com.nchu.miaosha.redis.service.RedisService;
import com.nchu.miaosha.scm.domain.OrderBill;
import com.nchu.miaosha.scm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName: MiaoshaOrderController
 * @Author: 时间
 * @Description: 秒杀订单信息Controller
 * @Date: 2020/7/18 11:44
 * @Version: 1.0
 */
@Controller
@RequestMapping("/miaoshaorder")
public class MiaoshaOrderController {
    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    @CheckLogin
    public ApiResponse<MiaoshaOrderDetailVo> info(Model model, User user, @RequestParam("orderId") long orderId) {
//        通过@CheckLogin注解的方式判断user是否为空
//        if (user == null) {
//            return ApiResponse.error(CodeMsg.SESSION_ERROR);
//        }

        OrderBill orderBill = orderService.getOrderById(orderId);
        if (orderBill == null) {
            return ApiResponse.error(CodeMsg.ORDER_NOT_EXIST);
        }

        // 如果有订单信息，把商品信息也查出来
        long goodsId = orderBill.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

        MiaoshaOrderDetailVo vo = new MiaoshaOrderDetailVo();
        vo.setOrder(orderBill);
        vo.setGoods(goods);
        return ApiResponse.success(vo);
    }
}
