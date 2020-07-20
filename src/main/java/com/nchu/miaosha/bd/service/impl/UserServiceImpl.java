package com.nchu.miaosha.bd.service.impl;

import com.nchu.miaosha.bd.dao.UserDao;
import com.nchu.miaosha.bd.domain.User;
import com.nchu.miaosha.bd.keys.UserKey;
import com.nchu.miaosha.bd.service.UserService;
import com.nchu.miaosha.bd.vo.LoginVo;
import com.nchu.miaosha.common.exception.GlobalException;
import com.nchu.miaosha.common.response.CodeMsg;
import com.nchu.miaosha.common.utils.MD5Util;
import com.nchu.miaosha.common.utils.UUIDUtil;
import com.nchu.miaosha.redis.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @ClassName: UserServiceImpl
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/14 23:53
 * @Version: 1.0
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    RedisService redisService;

    /**
     * 加缓存
     *
     * @param id
     * @return
     */
    @Override
    public User getById(long id) {
        // 取缓存
        User user = redisService.get(UserKey.getById, "" + id, User.class);
        if (Objects.nonNull(user)) {
            return user;
        }

        // 缓存不存在
        user = userDao.getById(id);
        if (Objects.isNull(user)) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }

        // 写入缓存
        redisService.set(UserKey.getById, "" + id, user);
        return user;
    }

    /**
     * 需要更新缓存
     * 先写入数据库，再让缓存失效，最后更新缓存
     *
     * @param id
     * @param password
     * @return
     */
    @Override
    public boolean updatePassword(String token, long id, String password) {
        // 取user
        User user = getById(id);
        if (Objects.isNull(user)) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }

        // 更新数据库
        User userUpdate = new User();
        userUpdate.setId(id);
        userUpdate.setPassword(MD5Util.formPassToDBPass(password, user.getSalt()));
        userDao.update(user);

        //  更新数据库成功，处理缓存

        // 对象删除
        redisService.delete(UserKey.getById, "" + id);

        // 更新token，删除的话登录不了
        user.setPassword(MD5Util.formPassToDBPass(password, user.getSalt()));
        redisService.set(UserKey.token, token, user);
        return false;
    }

    @Override
    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if (Objects.isNull(loginVo)) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        // 根据手机号、id获取用户
        User user = getById(Long.valueOf(loginVo.getMobile()));
        //验证密码
        verifyPassWord(user, loginVo.getPassword());
        //生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return true;
    }

    @Override
    public User getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        // 对象缓存，粒度最小
        User user = redisService.get(UserKey.token, token, User.class);

        // 可以通过该方式延长cookie有效期
        if (Objects.isNull(user)) {
            addCookie(response, token, user);
        }
        return user;
    }

    /**
     * @param user
     * @param passWord form表单第一次md5传过来的密码
     */
    private void verifyPassWord(User user, String passWord) {
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(passWord, saltDB);
        if (!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
    }

    /**
     * 通过将cookid存入第三方缓存实现简单分布式Session
     *
     * @param response
     * @param user
     */
    private void addCookie(HttpServletResponse response, String token, User user) {
        //没必要每次生成一个新的,注释掉该行代码,添加方法参数token
//        String token = UUIDUtil.uuid();
        // 一个token对应一个用户
        redisService.set(UserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(UserKey.token.expireSeconds()); // 设置成和redis过期时间一致
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}
