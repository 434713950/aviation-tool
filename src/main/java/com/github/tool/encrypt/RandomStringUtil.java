package com.github.tool.encrypt;

/**
 * @Author: PengCheng
 * @Description:    a-zA-Z0-9随机字符串生成工具
 * @Date: 2018/5/14
 */
public class RandomStringUtil {

    public static final char[] letters = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P','Q', 'R', 'S', 'T', 'U', 'V',
            'W', 'X', 'Y', 'Z',
            'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u'
            ,'v','w','x','y','z',
            '0','1','2','3','4','5','6','7','8','9'};

    /**
     * 生成可重复的随机字符串
     * @param length    生成的字符串长度
     * @return
     */
    public static String generateRandomString(int length){
        char[] chs = new char[length];
        for (int i = 0; i < chs.length; i++) {
            int index = (int) (Math.random() * (letters.length));
            chs[i] = letters[index];
        }
        return new String(chs);
    }

    /**
     * 生成不可重复的随机字符串
     * @param length    生成的字符串长度
     * @return
     */
    public static String generateUnRepeatRandomString(int length){
        boolean[] flags = new boolean[letters.length];
        char[] chs = new char[length];
        for (int i = 0; i < chs.length; i++) {
            int index;
            // 判断生成的字符是否重复
            do {
                index = (int) (Math.random() * (letters.length));
            } while (flags[index]);
            chs[i] = letters[index];
            flags[index] = true;
        }
        return new String(chs);
    }
}
