package com.github.tool.encrypt;

import com.github.tool.common.DateUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @Author: PengCheng
 * @Description: 生成器工具类
 * @Date: 2018/4/28
 */
public class GenerateUtil {

    /**
     * 订单号生成器
     * @return
     */
    public static String orderCodeGenerator(){
        int random = (int) (Math.random()*9000+1000);
        return DateUtil.format(DateUtil.getCurrentSystemTime(),DateUtil.DATE_TIME_DETAIL) + random;
    }

    /**
     * 时间版本号生成器
     * @return
     */
    public static String versionGenerator(){
        return DateUtil.format(DateUtil.getCurrentSystemTime(),DateUtil.DATE_TIME_DETAIL);
    }

    /**
     * sessionId生成器
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static synchronized String sessionIdGenerator() throws NoSuchAlgorithmException {
        // 取随机数发生器, 默认是SecureRandom
        Random random = new SecureRandom();
        byte[] bytes = new byte[16];
        //产生16字节的byte
        random.nextBytes(bytes);
        // 取摘要,默认是"MD5"算法
        bytes = MessageDigest.getInstance("MD5").digest(bytes);
        // Render the result as a String of hexadecimal digits
        StringBuffer result = new StringBuffer();
        //转化为16进制字符串
        for (int i = 0; i < bytes.length; i++) {
            byte b1 = (byte) ((bytes[i] & 0xf0) >> 4);
            byte b2 = (byte) (bytes[i] & 0x0f);
            if (b1 < 10) {
                result.append((char) ('0' + b1));
            } else {
                result.append((char) ('A' + (b1 - 10)));
            }
            if (b2 < 10) {
                result.append((char) ('0' + b2));
            } else {
                result.append((char) ('A' + (b2 - 10)));
            }
        }
        return (result.toString());
    }
}
