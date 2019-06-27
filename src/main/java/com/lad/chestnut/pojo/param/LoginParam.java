package com.lad.chestnut.pojo.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 登陆参数
 *
 * @author lad
 * @date 2019/4/29
 */
@Data
public class LoginParam {
    @NotNull(message = "登陆名不能为空！")
    private String userName;
    @NotNull(message = "密码不能为空！")
    private String password;
}
