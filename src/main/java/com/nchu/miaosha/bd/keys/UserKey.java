package com.nchu.miaosha.bd.keys;

import com.nchu.miaosha.redis.keys.BasePrefix;

/**
 * @ClassName: UserKey
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/14 23:41
 * @Version: 1.0
 */
public class UserKey extends BasePrefix {

    // token有效期
    public static final int TOKEN_EXPIRE = 3600 * 24 * 2;

    public UserKey(String prefix) {
        super(prefix);
    }

    public UserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static UserKey token = new UserKey(TOKEN_EXPIRE, "tk");
    // 对象缓存设置为永久有效
    public static UserKey getById = new UserKey(0, "id");
}
