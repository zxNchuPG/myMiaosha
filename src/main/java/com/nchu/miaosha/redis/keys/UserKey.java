package com.nchu.miaosha.redis.keys;

/**
 * @ClassName: UserKey
 * @Author: 时间
 * @Description: 通用缓存key封装，User模块 （测试框架用）
 * @Date: 2020/7/13 22:27
 * @Version: 1.0
 */
public class UserKey extends BasePrefix {
    /**
     * 防止外部实体化
     *
     * @param prefix
     */
    private UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");
}
