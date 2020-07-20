package com.nchu.miaosha.miaosha.service;

import com.nchu.miaosha.materials.vo.GoodsVo;

import java.util.List;

/**
 * @ClassName: MiaoshaGoodsService
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/16 0:55
 * @Version: 1.0
 */
public interface MiaoshaGoodsService {
    boolean reduceStock(GoodsVo goods);

    void resetStock(List<GoodsVo> goodsVoList);
}
