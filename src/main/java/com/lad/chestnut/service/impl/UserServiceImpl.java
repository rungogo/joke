package com.lad.chestnut.service.impl;

import com.lad.chestnut.common.ResponseData;
import com.lad.chestnut.common.ResponseEnum;
import com.lad.chestnut.common.Token;
import com.lad.chestnut.configuration.BusinessException;
import com.lad.chestnut.dao.UserDao;
import com.lad.chestnut.pojo.model.User;
import com.lad.chestnut.pojo.param.LoginParam;
import com.lad.chestnut.service.UserService;
import com.lad.chestnut.util.EncryptDecode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author lad
 * @date 2019/4/29
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Autowired
    private EncryptDecode encryptDecode;

    @Autowired
    private UserDao userDao;

//    @RolesAllowed("ROLE_ADMIN")
    @Override
    public ResponseData login(LoginParam loginParam) {
        Optional<User> optionalUser = userDao.getUserByUsernameAndPassword(loginParam.getUsername(), loginParam.getPassword());
        if (!optionalUser.isPresent()) {
            return new ResponseData(ResponseEnum.LOGIN_FAILURE_USERNAME_OR_PASSWORD_WRONG);
        }
        Token token = new Token();
        token.setUsername(loginParam.getUsername());
        token.setExpireTime(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
        Map<String, String> map = new HashMap<>(2);
        map.put("token", encryptDecode.encryptToken(token));
        return new ResponseData(ResponseEnum.SUCCESS, map);
    }

    @Override
    public ResponseData logout(Token token) {
        return null;
    }

    @Override
    public ResponseData getUserInfo(Token token) {
        Optional<User> optionalUser = userDao.getUserByUsername(token.getUsername());
        if (!optionalUser.isPresent()) {
            return new ResponseData(ResponseEnum.NO_FIND_USER);
        }
        return new ResponseData(ResponseEnum.SUCCESS, optionalUser.get());
    }

    @Override
    public User findByUsername(String username) {
        return userDao.getUserByUsername(username).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userDao.getUserByUsername(s).orElseThrow(() -> new BusinessException(ResponseEnum.NO_FIND_USER));
    }
}
