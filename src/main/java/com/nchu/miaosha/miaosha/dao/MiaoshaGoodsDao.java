package com.nchu.miaosha.miaosha.dao;

import com.nchu.miaosha.miaosha.domain.MiaoshaGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * @ClassName: MiaoshaGoodsDao
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/16 1:27
 * @Version: 1.0
 */
@Mapper
public interface MiaoshaGoodsDao {
    // stockcount > 0 ,防止库存为负数
    @Update("update t_bd_miaoshagoods set stockcount = stockcount - 1 where goodsid = #{goodsId} and stockcount > 0")
    public int reduceStock(MiaoshaGoods miaoshaGoods);

    @Update("update t_bd_miaoshagoods set stockcount = #{stockCount} where goodsid = #{goodsId}")
    public int resetStock(MiaoshaGoods miaoshaGoods);
}
