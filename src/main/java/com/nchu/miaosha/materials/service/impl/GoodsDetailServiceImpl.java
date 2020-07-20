package com.nchu.miaosha.materials.service.impl;

import com.nchu.miaosha.materials.dao.GoodsDetailDao;
import com.nchu.miaosha.materials.service.GoodsDetailService;
import com.nchu.miaosha.materials.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: GoodsDetailServiceImpl
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/18 0:31
 * @Version: 1.0
 */
@Service
public class GoodsDetailServiceImpl implements GoodsDetailService {
    @Autowired
    private GoodsDetailDao goodsDetailDao;
}
