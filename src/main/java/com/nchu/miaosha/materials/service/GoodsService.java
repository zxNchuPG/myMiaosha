package com.nchu.miaosha.materials.service;

import com.nchu.miaosha.materials.dao.GoodsDao;
import com.nchu.miaosha.materials.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName: GoodsService
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/15 21:01
 * @Version: 1.0
 */
public interface GoodsService {
    List<GoodsVo> findAll();

    GoodsVo getGoodsVoByGoodsId(long goodsId);
}
