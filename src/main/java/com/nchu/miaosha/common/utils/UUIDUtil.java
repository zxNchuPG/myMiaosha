package com.nchu.miaosha.common.utils;

import java.util.UUID;

/**
 * @ClassName: UUIDUtil
 * @Author: 时间
 * @Description: uuid生成
 * @Date: 2020/7/14 23:19
 * @Version: 1.0
 */
public class UUIDUtil {
    /**
     * 原生uuid带有 “-”
     *
     * @return
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
