package com.guhai.smartbuilding.util;

import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.KeyType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RsaUtils {
    private static RSA rsa;
    private static String publicKeyStr;
    private static String privateKeyStr;

    static {
        try {
            // 生成RSA密钥对
            rsa = new RSA();
            publicKeyStr = rsa.getPublicKeyBase64();
            privateKeyStr = rsa.getPrivateKeyBase64();
            System.out.println("RSA密钥对生成成功");
        } catch (Exception e) {
            log.error("RSA密钥对生成失败", e);
        }
    }

    /**
     * 获取公钥
     */
    public static String getPublicKey() {
        return publicKeyStr;
    }

    /**
     * 加密
     */
    public static String encrypt(String data) {
        try {
            return rsa.encryptBase64(data, KeyType.PublicKey);
        } catch (Exception e) {
            log.error("RSA加密失败", e);
            return null;
        }
    }

    /**
     * 解密
     */
    public static String decrypt(String encryptedData) {
        try {
            return rsa.decryptStr(encryptedData, KeyType.PrivateKey);
        } catch (Exception e) {
            log.error("RSA解密失败", e);
            return null;
        }
    }
} 