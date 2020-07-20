package com.nchu.miaosha.materials.vo;

import com.nchu.miaosha.materials.domain.Goods;

import java.util.Date;

/**
 * @ClassName: GoodsVo
 * @Author: 时间
 * @Description: 查询商品的时候，将秒杀信息带出来
 * @Date: 2020/7/15 21:05
 * @Version: 1.0
 */
public class GoodsVo extends Goods {
    private Double miaoshaPrice; // 秒杀价格
    private Integer stockCount; // 秒杀库存数量
    private Date startDate; // 秒杀开始时间
    private Date endDate; // 秒杀结束时间

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getMiaoshaPrice() {
        return miaoshaPrice;
    }

    public void setMiaoshaPrice(Double miaoshaPrice) {
        this.miaoshaPrice = miaoshaPrice;
    }
}
