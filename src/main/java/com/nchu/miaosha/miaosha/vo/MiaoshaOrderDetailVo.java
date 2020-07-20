package com.nchu.miaosha.miaosha.vo;

import com.nchu.miaosha.materials.domain.Goods;
import com.nchu.miaosha.scm.domain.OrderBill;

/**
 * @ClassName: MiaoshaOrderDetailVo
 * @Author: 时间
 * @Description: 秒杀订单详情页信息展示
 * @Date: 2020/7/18 11:40
 * @Version: 1.0
 */
public class MiaoshaOrderDetailVo {
    private Goods goods;
    private OrderBill order;

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public OrderBill getOrder() {
        return order;
    }

    public void setOrder(OrderBill order) {
        this.order = order;
    }
}
