package com.github.tool.net;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * <p>URL工具包</p>
 *
 * @author PengCheng
 * @date 2018/10/31
 */
public class URLUtil {

    /**
     * 从url路径字符串中获得path部分
     *
     * @param uriStr URI路径
     * @return path
     */
    public static String getPath(String uriStr) throws URISyntaxException {
        URI uri = new URI(uriStr);
        return uri.getPath();
    }
}
