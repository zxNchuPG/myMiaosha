package com.nchu.miaosha.bd.controller;

import com.nchu.miaosha.bd.service.UserService;
import com.nchu.miaosha.bd.vo.LoginVo;
import com.nchu.miaosha.common.response.ApiResponse;
import com.nchu.miaosha.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @ClassName: LoginController
 * @Author: 时间
 * @Description: 登录注册
 * @Date: 2020/7/14 21:52
 * @Version: 1.0
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController {
    @Autowired
    UserService userService;

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    RedisService redisService;

    /**
     * 跳转到登录页面
     *
     * @return
     */
    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public ApiResponse<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        logger.info(loginVo.toString());
        //登录
        userService.login(response, loginVo);
        return ApiResponse.success(true);
    }
}
