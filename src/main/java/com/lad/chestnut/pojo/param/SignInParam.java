package com.lad.chestnut.pojo.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 注册参数
 *
 * @author lad
 * @date 2019/7/3
 */
@Data
public class SignInParam {
    @NotNull(message = "登陆名不能为空！")
    private String username;
    @NotNull(message = "密码不能为空！")
    private String password1;
    @NotNull(message = "密码不能为空！")
    private String password2;
}
