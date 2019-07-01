package com.lad.chestnut.controller;

import com.lad.chestnut.annotation.IgnoreTokenValidate;
import com.lad.chestnut.annotation.WebLogController;
import com.lad.chestnut.common.ResponseData;
import com.lad.chestnut.common.ResponseEnum;
import com.lad.chestnut.common.Token;
import com.lad.chestnut.pojo.param.LoginParam;
import com.lad.chestnut.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户controller
 *
 * @author lad
 * @date 2019/4/29
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 登陆
     * @param loginParam
     * @param result
     * @return
     */
    @WebLogController(description = "用户登录")
    @IgnoreTokenValidate
    @PostMapping(value = "/login")
    public ResponseData login(@RequestBody @Validated LoginParam loginParam, BindingResult result) {
        return userService.login(loginParam);
    }
    @WebLogController(description = "用户登录")
    @IgnoreTokenValidate
    @GetMapping(value = "/loginSecurity")
    public ResponseData loginSecurity(@Validated LoginParam loginParam, BindingResult result) {
        return userService.login(loginParam);
    }

    @WebLogController(description = "获取用户信息")
    @GetMapping(value = "/getUserInfo")
    public ResponseData getUserInfo(Token token) {
        return userService.getUserInfo(token);
    }
}
