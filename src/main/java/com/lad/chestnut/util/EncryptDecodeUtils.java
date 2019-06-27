package com.lad.chestnut.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.netty.util.CharsetUtil;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lad
 * @date 2019/4/28
 */
public class EncryptDecodeUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(EncryptDecodeUtils.class);

    /**
     * 公钥加密
     */
    public static String encryptByPublicKey(String data, String publicKey) {
        try {
            HashMap map = new HashMap(3);
            // keySpec 生成对称密钥
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            SecretKey secretKey = keyGenerator.generateKey();
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

            // RSA 用对方公钥对‘对称密钥’进行加密
            Cipher cipher = Cipher.getInstance("RSA");

            cipher.init(Cipher.WRAP_MODE, loadPublicKeyByStr(publicKey));
            byte[] wrappedKey = cipher.wrap(keySpec);

            map.put("wrappedKey", Base64.encodeBase64String(wrappedKey));
            // 加密数据
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encryptedData = cipher.doFinal(data.getBytes(CharsetUtil.UTF_8));
            map.put("encryptedData", Base64.encodeBase64String(encryptedData));
            return Base64.encodeBase64String(JSON.toJSONBytes(map, new SerializerFeature[0]));
        } catch (Exception e) {
            LOGGER.info("加密失败{}", data);
            throw new RuntimeException("系统异常-加密失败", e);
        }
    }

    /**
     * 私钥解密
     */
    public static String decryptByPrivateKey(String data, String privateKey) {
        try {
            String decryptData = new String(Base64.decodeBase64(data));
            Map<String, Object> map = (Map) JSON.parseObject(decryptData, Map.class);
            // 获取密钥
            byte[] wrappedKey = Base64.decodeBase64(map.get("wrappedKey").toString());
            // RSA解密密钥
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.UNWRAP_MODE, loadPrivateKeyByStr(privateKey));
            Key key = cipher.unwrap(wrappedKey, "AES", Cipher.SECRET_KEY);

            // 解密数据
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedData = cipher.doFinal(Base64.decodeBase64(map.get("encryptedData").toString()));
            return new String(decryptedData, CharsetUtil.UTF_8);
        } catch (Exception e) {
            LOGGER.info("解密失败{}", data);
            throw new RuntimeException("系统异常-解密失败", e);
        }
    }


    private static RSAPublicKey loadPublicKeyByStr(String publicKeyStr) throws Exception {
        byte[] buffer = Base64.decodeBase64(publicKeyStr);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    private static RSAPrivateKey loadPrivateKeyByStr(String privateKeyStr) throws Exception {
        byte[] buffer = Base64.decodeBase64(privateKeyStr);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
        RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 产生公私钥对
     *
     * @return
     * @throws Exception
     */
    public static Map<String, String> genKeyPair() throws Exception {
        Map map = new HashMap(3);
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        String publicKeyString = Base64.encodeBase64String(publicKey.getEncoded());
        String privateKeyString = Base64.encodeBase64String(privateKey.getEncoded());
        map.put("publicKey", publicKeyString);
        map.put("privateKey", privateKeyString);
        return map;
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> map = genKeyPair();
        System.out.println(map.get("publicKey"));
        System.out.println(map.get("privateKey"));
    }
}

