package com.github.tool.common;

import java.lang.reflect.Array;
import java.util.Iterator;

/**
 * @Author: PengCheng
 * @Description:     数组工具包
 * @Date: 2018/9/13
 */
public class ArrayUtil {

    /**
     * 以 conjunction 为分隔符将数组转换为字符串
     * @param array  数组
     * @param conjunction   分隔符
     * @param <T>   被处理的集合类型
     * @return   连接后的字符串
     */
    public static <T> String join(T[] array, CharSequence conjunction) {
        if (null == array) {
            return null;
        }

        final StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (T item : array) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(conjunction);
            }
            if (ArrayUtil.isArray(item)) {
                sb.append(join(ArrayUtil.wrap(item), conjunction));
            } else if (item instanceof Iterable<?>) {
                sb.append(CollectionUtil.join((Iterable<?>) item, conjunction));
            } else if (item instanceof Iterator<?>) {
                sb.append(CollectionUtil.join((Iterator<?>) item, conjunction));
            } else {
                sb.append(item);
            }
        }
        return sb.toString();
    }

    /**
     * 包装数组对象
     *
     * @param obj 对象，可以是对象数组或者基本类型数组
     * @return 包装类型数组或对象数组
     */
    public static Object[] wrap(Object obj) {
        if (isArray(obj)) {
            int length = Array.getLength(obj);
            Object[] os = new Object[length];
            for (int i = 0; i < os.length; i++) {
                os[i] = Array.get(obj, i);
            }
            return os;
        }
        throw new RuntimeException("obj is not a array");
    }

    /**
     * 判断对象是否是数组
     * @param obj
     * @return
     */
    public static boolean isArray(Object obj){
        return obj!=null && obj.getClass().isArray();
    }

}
