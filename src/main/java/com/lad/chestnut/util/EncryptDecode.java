package com.lad.chestnut.util;

import com.alibaba.fastjson.JSONObject;
import com.lad.chestnut.common.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EncryptDecode {

    /**
     * 加密用
     */
    @Value("${publicKey}")
    private String publicKey;

    /**
     * 解密用
     */
    @Value("${privateKey}")
    private String privateKey;

    /**
     * 加密
     * @param token
     * @return
     */
    public String encryptToken(Token token) {
        return EncryptDecodeUtils.encryptByPublicKey(JSONObject.toJSONString(token), publicKey);
    }

    /**
     * 解密
     * @param token
     * @return
     */
    public Token decryptToken(String token) {
        return JSONObject.parseObject(EncryptDecodeUtils.decryptByPrivateKey(token, privateKey), Token.class);
    }
}
