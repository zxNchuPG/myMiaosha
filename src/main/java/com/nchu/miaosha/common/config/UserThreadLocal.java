package com.nchu.miaosha.common.config;

import com.nchu.miaosha.bd.domain.User;

/**
 * @ClassName: UserThreadLocal
 * @Author: 时间
 * @Description: 线程安全
 * @Date: 2020/7/20 23:31
 * @Version: 1.0
 */
public class UserThreadLocal {
    private static ThreadLocal<User> userHolder = new ThreadLocal<User>();

    public static void setUser(User user) {
        userHolder.set(user);
    }

    public static User getUser() {
        return userHolder.get();
    }
}
