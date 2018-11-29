package com.github.tool.net.remote.http.factory;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

/**
 * <p>http 处理器</p>
 *
 * @author PengCheng
 * @date 2018/11/15
 */
public class HttpClientManager {


    /**
     * 连接池配置
     * @param maxTotal             最大连接数
     * @param defaultMaxPerRoute   并发数
     * @return
     */
    public static PoolingHttpClientConnectionManager poolingHttpClientConnectionManager(int maxTotal,int defaultMaxPerRoute) {
        PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager();
        httpClientConnectionManager.setMaxTotal(maxTotal);
        httpClientConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        return httpClientConnectionManager;
    }

    /**
     * 请求配置
     * @param connectTimeout                创建连接的最长时间
     * @param connectionRequestTimeout      从连接池中获取到连接的最长时间
     * @param socketTimeout                 数据传输的最长时间
     * @param staleConnectionCheckEnabled   提交请求前测试连接是否可用
     * @return
     */
    public static RequestConfig requestConfig(int connectTimeout,int connectionRequestTimeout,int socketTimeout,boolean staleConnectionCheckEnabled) {
        return RequestConfig.custom().setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout)
                .setStaleConnectionCheckEnabled(staleConnectionCheckEnabled).build();
    }

    /**
     * 请求重试处理器
     * @param retryTime 重试次数
     * @return
     */
    public static HttpRequestRetryHandler httpRequestRetryHandler(final int retryTime) {
        // 请求重试
        return new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                // Do not retry if over max retry count,如果重试次数超过了retryTime,则不再重试请求
                if (executionCount >= retryTime) {
                    return false;
                }
                // 服务端断掉客户端的连接异常
                if (exception instanceof NoHttpResponseException) {
                    return true;
                }
                // time out 超时重试
                if (exception instanceof InterruptedIOException) {
                    return true;
                }
                // Unknown host
                if (exception instanceof UnknownHostException) {
                    return false;
                }
                // Connection refused
                if (exception instanceof ConnectTimeoutException) {
                    return false;
                }
                // SSL handshake exception
                if (exception instanceof SSLException) {
                    return false;
                }
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };
    }
}
