package com.nchu.miaosha.materials.keys;

import com.nchu.miaosha.redis.keys.BasePrefix;

/**
 * @ClassName: GoodsKey
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/17 21:50
 * @Version: 1.0
 */
public class GoodsKey extends BasePrefix {

    private GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    private GoodsKey(String prefix) {
        super(prefix);
    }

    public static GoodsKey getGoodsList = new GoodsKey(60, "gl");
    public static GoodsKey getGoodsDetail = new GoodsKey(60, "gd");
    public static GoodsKey getMiaoshaGoodsStock = new GoodsKey("gs");
}

