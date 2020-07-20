package com.nchu.miaosha.redis.keys;

/**
 * @ClassName: KeyPrefix
 * @Author: 时间
 * @Description: redis key前缀，通用缓存key封装
 * @Date: 2020/7/13 21:01
 * @Version: 1.0
 */
public interface KeyPrefix {

    /**
     * 过期时间
     *
     * @return
     */
    public int expireSeconds();

    public String getPrefix();

}

