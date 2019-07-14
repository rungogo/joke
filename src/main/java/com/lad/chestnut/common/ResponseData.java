package com.lad.chestnut.common;

/**
 * 封装返回参数
 *
 * @author lad
 * @date 2019/4/28
 */
public class ResponseData<T> {
    private int code;

    private String msg;

    private T content;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getContent() {
        return content;
    }
    public ResponseData() {
    }

    public ResponseData(ResponseEnum responseEnum, T content) {
        this.code = responseEnum.getCode();
        this.msg = responseEnum.getMsg();
        this.content = content;
    }

    public ResponseData(T content) {
        this(ResponseEnum.SUCCESS);
        this.content = content;
    }

    public ResponseData(ResponseEnum responseEnum) {
        this.code = responseEnum.getCode();
        this.msg = responseEnum.getMsg();
    }

    public static ResponseData success() {
        return new ResponseData(ResponseEnum.SUCCESS, null);
    }

    public static ResponseData fail() {
        return new ResponseData(ResponseEnum.SYSTEM_EXCEPTION, null);
    }
}
