package com.lad.chestnut.common;

import lombok.Data;

/**
 * @author lad
 * @date 2019/4/28
 */
@Data
public class Token {
    /**
     * 用户名
     */
    private String username;

    /**
     * 失效时间
     */
    private long expireTime;
}
