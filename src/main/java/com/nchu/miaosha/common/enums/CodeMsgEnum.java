package com.nchu.miaosha.common.enums;

/**
 * @ClassName: CodeMsgEnum
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/15 23:16
 * @Version: 1.0
 */
public enum CodeMsgEnum {
    //通用的错误码
    SUCCESS(0, "success"),
    SERVER_ERROR(500100, "服务端异常"),
    BIND_ERROR(500101, "参数校验异常：%s"),
    //登录模块 5002XX
    SESSION_ERROR(500210, "Session不存在或者已经失效"),
    PASSWORD_EMPTY(500211, "登录密码不能为空"),
    MOBILE_EMPTY(500212, "手机号不能为空"),
    MOBILE_ERROR(500213, "手机号格式错误"),
    MOBILE_NOT_EXIST(500214, "手机号不存在"),
    PASSWORD_ERROR(500215, "密码错误"),
    // 秒杀模块
    MIAO_SHA_OVER(500500, "商品已经秒杀完毕"),
    REPEATE_MIAOSHA(500501, "不能重复秒杀");

    private int code;
    private String message;

    CodeMsgEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
