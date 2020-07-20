package com.nchu.miaosha.common.keys;

import com.nchu.miaosha.redis.keys.BasePrefix;

/**
 * @ClassName: AccessKey
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/20 23:14
 * @Version: 1.0
 */
public class AccessKey extends BasePrefix {

    private AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static AccessKey withExpire(int expireSeconds) {
        return new AccessKey(expireSeconds, "access");
    }

}