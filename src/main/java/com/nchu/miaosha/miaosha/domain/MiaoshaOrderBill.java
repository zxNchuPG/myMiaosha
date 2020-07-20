package com.nchu.miaosha.miaosha.domain;

/**
 * @ClassName: MiaoshaOrderBill
 * @Author: 时间
 * @Description: 秒杀订单表 t_scm_miaoshaorderbill
 * @Date: 2020/7/15 20:58
 * @Version: 1.0
 */
public class MiaoshaOrderBill {
    private Long id;
    private Long userId;
    private Long orderBillId;
    private Long goodsId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderBillId() {
        return orderBillId;
    }

    public void setOrderBillId(Long orderBillId) {
        this.orderBillId = orderBillId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
}
