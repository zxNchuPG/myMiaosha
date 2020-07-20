package com.nchu.miaosha.common.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @ClassName: WebConfig
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/15 1:32
 * @Version: 1.0
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    UserArgumentResolver userArgumentResolver;

    @Autowired
    AccessInterceptor accessInterceptor;

    @Autowired
    LoginInterceptor loginInterceptor;

    /**
     * 往Controller的参数中赋值
     *
     * @param argumentResolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(userArgumentResolver);
    }

    /**
     * 将校验登录User是否为空的拦截器添加进来
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 加入的顺序就是执行的顺序
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**");
        registry.addInterceptor(accessInterceptor);
        super.addInterceptors(registry);
    }
}
