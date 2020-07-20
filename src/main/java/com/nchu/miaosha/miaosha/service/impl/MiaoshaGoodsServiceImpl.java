package com.nchu.miaosha.miaosha.service.impl;

import com.nchu.miaosha.materials.vo.GoodsVo;
import com.nchu.miaosha.miaosha.dao.MiaoshaGoodsDao;
import com.nchu.miaosha.miaosha.domain.MiaoshaGoods;
import com.nchu.miaosha.miaosha.service.MiaoshaGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: MisaoshaGoodsServiceImpl
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/16 0:55
 * @Version: 1.0
 */
@Service
public class MiaoshaGoodsServiceImpl implements MiaoshaGoodsService {
    @Autowired
    private MiaoshaGoodsDao miaoshaGoodsDao;

    @Override
    public boolean reduceStock(GoodsVo goods) {
        MiaoshaGoods miaoshaGoods = new MiaoshaGoods();
        miaoshaGoods.setGoodsId(goods.getId());
        int result = miaoshaGoodsDao.reduceStock(miaoshaGoods);
        return result > 0;
    }

    @Override
    public void resetStock(List<GoodsVo> goodsVoList) {
        for (GoodsVo goods : goodsVoList) {
            MiaoshaGoods g = new MiaoshaGoods();
            g.setGoodsId(goods.getId());
            g.setStockCount(goods.getStockCount());
            miaoshaGoodsDao.resetStock(g);
        }
    }
}
