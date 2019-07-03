package com.lad.chestnut.common;

/**
 * 系统提示错误码
 *
 * @author lad
 * @date 2019/4/28
 */
public enum ResponseEnum {

    /**
     * 成功
     */
    SUCCESS(0, "成功！"),
    FAIL(1, "失败！"),
    PARAMETER_ERROR(2, "参数错误！"),
    ACCESS_DENIED(3, "没有权限，不允许访问"),

    NOT_LOGIN(1001, "未登录！"),
    TOKEN_EXPIRED(1002, "Token已过期！"),
    NO_FIND_USER(1003, "未找到该账号！"),
    LOGIN_FAILURE_USERNAME_OR_PASSWORD_WRONG(1004, "用户名或密码错误"),
    ACCOUNT_LOCKED(1005, "账户被锁定，请联系管理员!"),
    PASSWORD_AGING(1006, "密码过期，请联系管理员!"),
    OVERDUE_ACCOUNT(1007, "账户过期，请联系管理员!"),
    ACCOUNT_DISABLED(1008, "账户被禁用，请联系管理员!"),
    PASSWORD_INCONSISTENCY(1009, "密码不一致,请重新输入!"),


    /**
     * 系统异常
     */
    SYSTEM_EXCEPTION(9000,"系统异常！错误码：9000"),

    /**
     * 请求方式不正确
     * @return
     */
    REQUEST_METHOD_NOT_SUPPORTED(9001, "系统异常！错误码：9001"),

    /**
     * 接口不存在
     */
    NO_HANDLER_FOUND(9002, "系统异常！错误码：9002"),

    /**
     * IO流异常
     */
    IO_EXCEPTION(9003, "系统异常！错误码：9003"),

    REQUIRED_PARAMETER_LACK(9100, "缺少必填参数！"),
    PARAMETER_FORMAT_ERROR(9102, "参数格式错误！"),
    PARAMETER_LENGTH_OVER_LIMIT(9101, "参数长度超过限制！"),
    PAGE_SIZE_OVER_LIMIT(9103, "Page size超过最大限制！");

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    ResponseEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
