package com.lad.chestnut.service;

import com.lad.chestnut.common.ResponseData;
import com.lad.chestnut.common.Token;
import com.lad.chestnut.pojo.model.User;
import com.lad.chestnut.pojo.param.LoginParam;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 用户service
 *
 * @author lad
 * @date 2019/4/29
 */
public interface UserService extends UserDetailsService {

    /**
     * 用户登陆
     * @param loginParam
     * @return
     */
    ResponseData login(LoginParam loginParam);

    /**
     * 用户退出
     * @param token
     * @return
     */
    ResponseData logout(Token token);

    /**
     * 获取本人用户信息
     *
     * @param token
     * @return
     */
    ResponseData getUserInfo(Token token);

    /**
     * 保存用户
     *
     * @param user
     * @return
     */
    User save(User user);
}
