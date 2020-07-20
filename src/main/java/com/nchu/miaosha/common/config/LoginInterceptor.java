package com.nchu.miaosha.common.config;

import com.nchu.miaosha.bd.domain.User;
import com.nchu.miaosha.bd.service.UserService;
import com.nchu.miaosha.common.annotation.CheckLogin;
import com.nchu.miaosha.common.exception.GlobalException;
import com.nchu.miaosha.common.response.CodeMsg;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @ClassName: LoginInterceptor
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/18 12:22
 * @Version: 1.0
 */
@Service
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        CheckLogin checkLogin = handlerMethod.getMethodAnnotation(CheckLogin.class);
        if (Objects.nonNull(checkLogin)) {
            String paramToken = request.getParameter(UserService.COOKIE_NAME_TOKEN);
            String cookieToken = getCookieValue(request, UserService.COOKIE_NAME_TOKEN);
            if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
                new GlobalException(CodeMsg.SESSION_ERROR);
            }
            String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
            User user = userService.getByToken(response, token);
            request.setAttribute("currentUser", user);
            return true;
        }

        return true;
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
