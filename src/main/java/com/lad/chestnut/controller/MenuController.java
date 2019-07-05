package com.lad.chestnut.controller;

import com.lad.chestnut.annotation.IgnoreTokenValidate;
import com.lad.chestnut.annotation.WebLogController;
import com.lad.chestnut.common.ResponseData;
import com.lad.chestnut.common.ResponseEnum;
import com.lad.chestnut.common.Token;
import com.lad.chestnut.pojo.param.LoginParam;
import com.lad.chestnut.pojo.param.SignInParam;
import com.lad.chestnut.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * 用户controller
 *
 * @author lad
 * @date 2019/4/29
 */
@RestController
@RequestMapping(value = "/menu")
public class MenuController {


    @WebLogController(description = "测试1")
    @IgnoreTokenValidate
    @GetMapping(value = "/test1")
    public ResponseData test1() {
        return ResponseData.success();
    }


    @WebLogController(description = "测试2")
    @IgnoreTokenValidate
    @GetMapping(value = "/test2")
    public ResponseData test2() {
        return ResponseData.success();
    }

}
