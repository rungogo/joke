package com.lad.chestnut.configuration;

import com.lad.chestnut.common.ResponseEnum;

/**
 * 定义系统异常 继承RuntimeException
 *
 * @author lad
 * @date 2019/4/28
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 2332608236621015980L;

    private ResponseEnum responseEnum;

    public ResponseEnum getResultInfo() {
        return this.responseEnum;
    }

    public BusinessException(ResponseEnum responseEnum) {
        super(responseEnum.getCode() + ":" + responseEnum.getMsg());
        this.responseEnum = responseEnum;
    }

    public BusinessException(ResponseEnum responseEnum, Throwable cause) {
        super(cause);
        this.responseEnum = responseEnum;
    }
}
