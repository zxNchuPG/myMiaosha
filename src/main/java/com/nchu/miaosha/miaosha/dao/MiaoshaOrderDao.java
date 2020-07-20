package com.nchu.miaosha.miaosha.dao;

import com.nchu.miaosha.miaosha.domain.MiaoshaOrderBill;
import org.apache.ibatis.annotations.*;

/**
 * @ClassName: MiaoshaOrderDao
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/16 0:27
 * @Version: 1.0
 */
@Mapper
public interface MiaoshaOrderDao {

    @Select("select * from t_scm_miaoshaorderbill where userId=#{userId} and goodsId=#{goodsId}")
    MiaoshaOrderBill getMiaoshaOrderByUserIdGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

    @Insert("insert into t_scm_miaoshaorderbill (userid, goodsid, orderBillId)values(#{userId}, #{goodsId}, #{orderBillId})")
    public int insertMiaoshaOrder(MiaoshaOrderBill miaoshaOrder);

    @Delete("delete from t_scm_miaoshaorderbill")
    void deleteMiaoshaOrders();
}
