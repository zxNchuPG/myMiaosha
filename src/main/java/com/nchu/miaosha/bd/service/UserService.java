package com.nchu.miaosha.bd.service;

import com.nchu.miaosha.bd.domain.User;
import com.nchu.miaosha.bd.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: UserService
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/14 22:59
 * @Version: 1.0
 */
public interface UserService {

    static final String COOKIE_NAME_TOKEN = "token";

    User getById(long id);

    boolean updatePassword(String token, long id, String password);

    boolean login(HttpServletResponse response, LoginVo loginVo);

    User getByToken(HttpServletResponse response, String token);
}
