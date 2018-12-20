package com.github.tool.net.remote.http.config;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>httpClient 连接配置,内部已经提供了默认参数</p>
 *
 * @author PengCheng
 * @date 2018/11/15
 */
@Data
@Accessors(chain = true)
public class HttpClientConnProperties {

    /**
     * 最大连接数
     */
    private Integer maxTotal = 100;

    /**
     * 默认的最大并发数
     */
    private Integer defaultMaxPerRoute = 20;

    /**
     * 创建连接的超时时间
     */
    private Integer connectTimeout = 1000;

    /**
     * 从连接池中获取到连接的超时时间
     */
    private Integer connectionRequestTimeout = 500;

    /**
     * 数据传输的超时时间
     */
    private Integer socketTimeout = 10000;

    /**
     * 提交请求前测试连接是否可用
     */
    private boolean staleConnectionCheckEnabled = true;

    /**
     * 连接重试次数
     */
    private int retryTime = 5;

}
