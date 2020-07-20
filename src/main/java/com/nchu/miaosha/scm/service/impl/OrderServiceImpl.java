package com.nchu.miaosha.scm.service.impl;

import com.nchu.miaosha.bd.domain.User;
import com.nchu.miaosha.materials.vo.GoodsVo;
import com.nchu.miaosha.scm.dao.OrderDao;
import com.nchu.miaosha.scm.domain.OrderBill;
import com.nchu.miaosha.scm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @ClassName: OrderServiceImpl
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/16 1:03
 * @Version: 1.0
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Override
    public OrderBill createOrder(User user, GoodsVo goods) {
        OrderBill orderBill = new OrderBill();
        orderBill.setCreateDate(new Date());
        orderBill.setDeliveryAddrId(0L);
        orderBill.setGoodsCount(1);
        orderBill.setGoodsId(goods.getId());
        orderBill.setGoodsName(goods.getGoodsName());
        orderBill.setGoodsPrice(goods.getMiaoshaPrice()); // 秒杀价格
        orderBill.setOrderChannel(1);
        orderBill.setStatus(0);
        orderBill.setUserId(user.getId());
        // mybatis 成功后会把id塞到orderBill对象中
        orderDao.insert(orderBill);
        orderBill.setId(orderBill.getId());
        return orderBill;
    }

    @Override
    public OrderBill getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }

    @Override
    public void deleteOrders() {
        orderDao.deleteOrders();
    }
}
