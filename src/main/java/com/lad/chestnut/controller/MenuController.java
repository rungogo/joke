package com.lad.chestnut.controller;

import com.lad.chestnut.annotation.IgnoreTokenValidate;
import com.lad.chestnut.annotation.WebLogController;
import com.lad.chestnut.common.ResponseData;
import com.lad.chestnut.service.MenuService;
import com.lad.chestnut.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户controller
 *
 * @author lad
 * @date 2019/4/29
 */
@RestController
@RequestMapping(value = "/sys")
public class MenuController {

    @Autowired
    private MenuService menuService;

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

    @WebLogController(description = "测试2")
    @IgnoreTokenValidate
    @GetMapping(value = "/menuTree")
    public ResponseData getMenuTree() {
        return new ResponseData<>(menuService.getCurrentUserMenu(UserUtils.getCurrentHr()));
    }
}
