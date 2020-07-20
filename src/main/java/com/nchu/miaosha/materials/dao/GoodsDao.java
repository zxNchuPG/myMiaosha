package com.nchu.miaosha.materials.dao;

import com.nchu.miaosha.materials.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: GoodsDao
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/15 21:01
 * @Version: 1.0
 */
@Mapper
public interface GoodsDao {

    @Select("select t.*,m.miaoshaPrice,m.stockCount,m.startDate,m.endDate from t_bd_goods t left join t_bd_miaoshagoods m on t.id = m.goodsid")
    public List<GoodsVo> findAll();

    @Select("select t.*, m.stockcount,  m.startdate,  m.enddate, m.miaoshaprice from t_bd_miaoshagoods m left join t_bd_goods t on  m.goodsid = t.id where t.id = #{goodsId}")
    GoodsVo getGoodsVoByGoodsId(@Param("goodsId")long goodsId);
}
