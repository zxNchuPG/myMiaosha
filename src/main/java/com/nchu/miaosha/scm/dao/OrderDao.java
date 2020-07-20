package com.nchu.miaosha.scm.dao;

import com.nchu.miaosha.scm.domain.OrderBill;
import org.apache.ibatis.annotations.*;

/**
 * @ClassName: OrderDao
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/16 1:10
 * @Version: 1.0
 */
@Mapper
public interface OrderDao {
    @Insert("insert into t_scm_orderbill(userid, goodsid, goodsname, goodscount, goodsprice, orderchannel, status, createdate)values("
            + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
    long insert(OrderBill orderBill);

    @Select("select * from t_scm_orderbill where id = #{orderId}")
    OrderBill getOrderById(long orderId);

    @Delete("delete from t_scm_orderbill")
    void deleteOrders();
}
