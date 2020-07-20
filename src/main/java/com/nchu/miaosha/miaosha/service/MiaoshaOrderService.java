package com.nchu.miaosha.miaosha.service;

import com.nchu.miaosha.miaosha.domain.MiaoshaOrderBill;
import com.nchu.miaosha.scm.domain.OrderBill;

/**
 * @ClassName: MiaoshaOrderService
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/16 0:19
 * @Version: 1.0
 */
public interface MiaoshaOrderService {
    MiaoshaOrderBill getMiaoshaOrderByUserIdGoodsId(long userId, long godosId);

    void createMiaoshaOrderByOrder(OrderBill orderBill);

    void deleteOrders();
}
