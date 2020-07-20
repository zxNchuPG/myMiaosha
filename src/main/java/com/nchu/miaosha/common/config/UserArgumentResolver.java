package com.nchu.miaosha.common.config;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nchu.miaosha.bd.domain.User;
import com.nchu.miaosha.bd.service.UserService;
import com.nchu.miaosha.common.exception.GlobalException;
import com.nchu.miaosha.common.response.CodeMsg;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @ClassName: UserArgumentResolver
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/15 1:33
 * @Version: 1.0
 */
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    UserService userService;

    /**
     * 获取参数类型
     *
     * @param parameter
     * @return
     */
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        return clazz == User.class;
    }

    /**
     * 如果supportsParameter返回的true,那么做该方法的处理
     * 该方法是没有写LoginInterceptor之前使用
     * 写了LoginIntercetor后，User的获取放到 LoginInterceptor （@CheckLogin注解） 中
     *
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
//    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
//        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
//
//        String paramToken = request.getParameter(UserService.COOKIE_NAME_TOKEN);
//        String cookieToken = getCookieValue(request, UserService.COOKIE_NAME_TOKEN);
//        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
//            return null;
//        }
//        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
//        return userService.getByToken(response, token);
//    }

    /**
     * 为Controller方法注入User对象
     *
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // 获取LoginInterceptor设置到request中的用户信息，注入到参数中
        User user = (User) webRequest.getAttribute("currentUser", RequestAttributes.SCOPE_REQUEST);
        if (user != null) {
            return user;
        } else {
            user = UserThreadLocal.getUser();
            if (user != null) {
                return user;
            }
        }
        throw new GlobalException(CodeMsg.SESSION_ERROR);
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