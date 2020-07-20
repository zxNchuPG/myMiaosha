package com.nchu.miaosha.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @ClassName: MD5Util
 * @Author: 时间
 * @Description: md5加密工具类
 * @Date: 2020/7/14 21:30
 * @Version: 1.0
 */
public class MD5Util {

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    /**
     * 写死一个客户端的salt
     */
    private static final String salt = "1a2b3c4d";

    /**
     * 第一次将输入的密码做md5
     * 使用写死的salt
     *
     * @param inputPass
     * @return
     */
    public static String inputPassToFormPass(String inputPass) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
//        System.out.println(str);
        return md5(str);
    }

    /**
     * 第二次md5,存入数据库
     * 使用随机salt
     *
     * @param formPass form表单传上来的md5
     * @param salt
     * @return
     */
    public static String formPassToDBPass(String formPass, String salt) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    /**
     * 将用户输入的md5转换成db密码
     *
     * @param inputPass
     * @param saltDB
     * @return
     */
    public static String inputPassToDbPass(String inputPass, String saltDB) {
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass, saltDB);
        return dbPass;
    }

    public static void main(String[] args) {
//        System.out.println(inputPassToFormPass("123456"));//d3b1294a61a07da9b49b6e22b2cbd7f9
//        System.out.println(formPassToDBPass(inputPassToFormPass("123456"), "1a2b3c4d"));
        System.out.println(inputPassToDbPass("123456", "1a2b3c4d"));//b7797cce01b4b131b433b6acf4add449
    }
}
