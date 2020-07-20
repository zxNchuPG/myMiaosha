package com.nchu.miaosha.redis.keys;

/**
 * @ClassName: BasePrefix
 * @Author: 时间
 * @Description: 通用缓存key封装
 * @Date: 2020/7/13 22:26
 * @Version: 1.0
 */
public abstract class BasePrefix implements KeyPrefix {

    private int expireSeconds;

    private String prefix;

    public BasePrefix(String prefix) {//0代表永不过期
        this(0, prefix);
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    public int expireSeconds() {//默认0代表永不过期
        return expireSeconds;
    }

    public String getPrefix() {
        // 通过class保证每个模块的key唯一
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }

}
