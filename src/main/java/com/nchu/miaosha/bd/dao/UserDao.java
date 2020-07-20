package com.nchu.miaosha.bd.dao;

import com.nchu.miaosha.bd.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @ClassName: UserDao
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/14 22:35
 * @Version: 1.0
 */
@Mapper
public interface UserDao {
    @Select("select * from t_bd_user where id = #{id}")
    User getById(@Param("id") long id);

    @Update("update t_bd_user set password = #{password} where id = #{id}")
    void update(User user);
}
