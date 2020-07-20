package com.nchu.miaosha.scm.service;

import com.nchu.miaosha.bd.domain.User;
import com.nchu.miaosha.materials.vo.GoodsVo;
import com.nchu.miaosha.scm.domain.OrderBill;

/**
 * @ClassName: OrderService
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/16 1:03
 * @Version: 1.0
 */
public interface OrderService {
    OrderBill createOrder(User user, GoodsVo goods);

    OrderBill getOrderById(long orderId);

    void deleteOrders();
}
