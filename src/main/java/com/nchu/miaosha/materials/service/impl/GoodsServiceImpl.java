package com.nchu.miaosha.materials.service.impl;

import com.nchu.miaosha.materials.dao.GoodsDao;
import com.nchu.miaosha.materials.service.GoodsService;
import com.nchu.miaosha.materials.vo.GoodsVo;
import com.nchu.miaosha.miaosha.domain.MiaoshaGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: GoodsServiceImpl
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/15 21:01
 * @Version: 1.0
 */
@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsDao goodsDao;

    @Override
    public List<GoodsVo> findAll() {
        return goodsDao.findAll();
    }

    @Override
    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

}
