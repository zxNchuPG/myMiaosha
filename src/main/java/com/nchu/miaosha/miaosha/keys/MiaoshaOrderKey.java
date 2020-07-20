package com.nchu.miaosha.miaosha.keys;

import com.nchu.miaosha.redis.keys.BasePrefix;

/**
 * @ClassName: MiaoshaOrderKey
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/18 19:10
 * @Version: 1.0
 */
public class MiaoshaOrderKey extends BasePrefix {

    public MiaoshaOrderKey(String prefix) {
        super(prefix);
    }

    public static MiaoshaOrderKey getMiaoshaOrderByUidGid = new MiaoshaOrderKey("moug");
}
