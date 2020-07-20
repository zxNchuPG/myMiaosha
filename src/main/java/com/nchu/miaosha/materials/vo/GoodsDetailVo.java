package com.nchu.miaosha.materials.vo;

import com.nchu.miaosha.bd.domain.User;
import com.nchu.miaosha.materials.domain.Goods;

/**
 * @ClassName: GoodsDetailVo
 * @Author: 时间
 * @Description: 商品详情显示数据
 * @Date: 2020/7/18 0:46
 * @Version: 1.0
 */
public class GoodsDetailVo {
    private int miaoshaStatus = 0; // 0秒杀没开始 1秒杀进行中 2秒杀已经结束
    private int remainSeconds = 0; // 离秒杀剩余多长时间
    private Goods goods;
    private User user;

    public int getMiaoshaStatus() {
        return miaoshaStatus;
    }

    public void setMiaoshaStatus(int miaoshaStatus) {
        this.miaoshaStatus = miaoshaStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
