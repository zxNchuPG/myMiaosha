package com.nchu.miaosha.miaosha.keys;

import com.nchu.miaosha.redis.keys.BasePrefix;

/**
 * @ClassName: MiaoshaKey
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/19 12:08
 * @Version: 1.0
 */
public class MiaoshaKey extends BasePrefix {

    private MiaoshaKey(String prefix) {
        super(prefix);
    }

    private MiaoshaKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static MiaoshaKey isGoodsOver = new MiaoshaKey("go");
    public static MiaoshaKey getMiaoshaPath = new MiaoshaKey(60, "mp");
    public static MiaoshaKey getMiaoshaVerifyCode = new MiaoshaKey(300, "vc");
}
