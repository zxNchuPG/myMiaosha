package com.nchu.miaosha.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit {
    int seconds(); // 时间限制

    int maxCount(); // 访问次数

    boolean needLogin() default true; //是否需要登录
}