package com.github.tool.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: PengCheng
 * @Description: 加解密工具类
 * @Date: 2018/4/12
 */
public class EncryptUtils {

    /**
     * SHA-1 hash加密算法
     * @param bytes
     * @return
     */
    public static String hashSHAEncrypt(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest sha1 = null;
        sha1 = MessageDigest.getInstance("SHA-1");
        sha1.reset();
        sha1.update(bytes);
        return bytesToHex(sha1.digest());
    }

    public static String bytesToHex(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


}
