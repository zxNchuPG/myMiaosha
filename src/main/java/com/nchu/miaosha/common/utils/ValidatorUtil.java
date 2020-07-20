package com.nchu.miaosha.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName: ValidatorUtil
 * @Author: 时间
 * @Description: 格式校验工具类
 * @Date: 2020/7/14 22:11
 * @Version: 1.0
 */
public class ValidatorUtil {
    // 1 开头跟10个数字
    private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");

    public static boolean isMobile(String src) {
        if (StringUtils.isEmpty(src)) {
            return false;
        }
        Matcher m = mobile_pattern.matcher(src);
        return m.matches();
    }
}
