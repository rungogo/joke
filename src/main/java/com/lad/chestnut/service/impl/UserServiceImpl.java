package com.lad.chestnut.service.impl;

import com.lad.chestnut.common.ResponseData;
import com.lad.chestnut.common.ResponseEnum;
import com.lad.chestnut.common.Token;
import com.lad.chestnut.dao.UserDao;
import com.lad.chestnut.pojo.model.User;
import com.lad.chestnut.pojo.param.LoginParam;
import com.lad.chestnut.service.UserService;
import com.lad.chestnut.util.EncryptDecode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author lad
 * @date 2019/4/29
 */
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private EncryptDecode encryptDecode;

    @Autowired
    private UserDao userDao;

    @Override
    public ResponseData login(LoginParam loginParam) {
        Optional<User> optionalUser = userDao.getUserByUserNameAndPassword(loginParam.getUserName(), loginParam.getPassword());
        if (!optionalUser.isPresent()) {
            return new ResponseData(ResponseEnum.LOGIN_FAILURE_USERNAME_OR_PASSWORD_WRONG);
        }
        Token token = new Token();
        token.setUserName(loginParam.getUserName());
        token.setExpireTime(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
        Map<String, String> map = new HashMap<>(2);
        map.put("token", encryptDecode.encryptToken(token));
        return new ResponseData(ResponseEnum.SUCCESS, map);
    }

    @Override
    public ResponseData loginSecurity(LoginParam loginParam) {
        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(loginParam.getUserName(), "222", AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
//        User user = new User();
//        user.setUserName("111");
//        user
//        return null;
        return new ResponseData(user);
    }

    @Override
    public ResponseData logout(Token token) {
        return null;
    }

    @Override
    public ResponseData getUserInfo(Token token) {
        Optional<User> optionalUser = userDao.getUserByUserName(token.getUserName());
        if (!optionalUser.isPresent()) {
            return new ResponseData(ResponseEnum.NO_FIND_USER);
        }
        return new ResponseData(ResponseEnum.SUCCESS, optionalUser.get());
    }
}
