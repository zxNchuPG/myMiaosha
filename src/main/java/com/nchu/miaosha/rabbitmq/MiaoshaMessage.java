package com.nchu.miaosha.rabbitmq;

import com.nchu.miaosha.bd.domain.User;

/**
 * @ClassName: MiaoshaMessage
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/18 22:19
 * @Version: 1.0
 */
public class MiaoshaMessage {
    private User user;
    private long goodsId;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
