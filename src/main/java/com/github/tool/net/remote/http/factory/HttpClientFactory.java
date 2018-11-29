package com.github.tool.net.remote.http.factory;

import com.github.tool.net.remote.http.config.ConnectionProperties;
import com.github.tool.net.remote.http.exception.HttpClientFactoryException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * <p>httpclient 远程调用工厂</p>
 * singleton
 * @author PengCheng
 * @date 2018/11/15
 */
public class HttpClientFactory {

    private volatile static HttpClientFactory factory;

    private CloseableHttpClient client;

    private HttpClientFactory(final ConnectionProperties connectionProperties){
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = HttpClientManager.poolingHttpClientConnectionManager(connectionProperties.getMaxTotal(),connectionProperties.getDefaultMaxPerRoute());
        RequestConfig requestConfig = HttpClientManager.requestConfig(connectionProperties.getConnectTimeout(),connectionProperties.getConnectionRequestTimeout(),connectionProperties.getSocketTimeout(),connectionProperties.isStaleConnectionCheckEnabled());
        HttpRequestRetryHandler httpRequestRetryHandler = HttpClientManager.httpRequestRetryHandler(connectionProperties.getRetryTime());

        /*
         * 建议此处使用HttpClients.custom的方式来创建HttpClientBuilder，而不要使用HttpClientBuilder.create()方法来创建HttpClientBuilder
         * 从官方文档可以得出，HttpClientBuilder是非线程安全的，但是HttpClients确实Immutable的，immutable 对象不仅能够保证对象的状态不被改变，
         * 而且还可以不使用锁机制就能被其他线程共享
         *
         *  .setRetryHandler(httpRequestRetryHandler)    重试处理
         *	.setKeepAliveStrategy(connectionKeepAliveStrategy)  保持策略
         *	.setRoutePlanner(proxyRoutePlanner)         路由策略
         */
        client = HttpClients.custom().setConnectionManager(poolingHttpClientConnectionManager)
                //重试策略
                .setRetryHandler(httpRequestRetryHandler)
                .setDefaultRequestConfig(requestConfig)
                /*
                 *	.setKeepAliveStrategy(connectionKeepAliveStrategy)  保持策略
                 *	.setRoutePlanner(proxyRoutePlanner)         路由策略
                 */
                .build();
    }

    public static HttpClientFactory getInstance(final ConnectionProperties connectionProperties){
        if (factory == null){
            synchronized (HttpClientFactory.class){
                if (factory == null){
                    factory = new HttpClientFactory(connectionProperties);
                }
            }
        }
        return factory;
    }

    public CloseableHttpClient getClient(){
        return this.client;
    }

    /**
     * get方法
     * @param url   请求的路径
     * @return      响应的内容
     */
    public String httpGet(String url){
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response =  getClient().execute(httpGet);
            int httpStatus = response.getStatusLine().getStatusCode();
            String responseParam = EntityUtils.toString(response.getEntity());
            if (httpStatus == HttpStatus.SC_OK) {
                return responseParam;
            }
            throw new HttpClientFactoryException("GET response error : "+httpStatus);
        } catch (IOException e) {
            throw new HttpClientFactoryException("GET request connect break",e);
        }
    }

    /**
     * post请求
     * @param url            请求的路径
     * @param params         携带的参数
     * @param encoding       编码
     * @return               响应的内容
     */
    public String httpPost(String url,String params,String encoding) {
        return httpPost(url,params,encoding,null);
    }

    /**
     * post请求
     * @param url            请求的路径
     * @param params         携带的参数
     * @param encoding       编码
     * @param cookie         cookie值
     * @return               响应的内容
     */
    public String httpPost(String url,String params,String encoding,String cookie) {
        try {
            CloseableHttpResponse response = (CloseableHttpResponse) httpPostForResponse(url,params,cookie);
            int httpStatus = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if (httpStatus == HttpStatus.SC_OK) {
                return EntityUtils.toString(entity,encoding);
            }
            EntityUtils.consume(entity);
            throw new HttpClientFactoryException("POST response error："+httpStatus);
        } catch (IOException e) {
            throw new HttpClientFactoryException("POST response data handle fail",e);
        }
    }

    /**
     * 发送post请求并返回响应体对象
     * @param url           请求的路径
     * @param params
     * @return
     */
    public HttpResponse httpPostForResponse(String url,String params,String cookie) {
        HttpPost httpPost = new HttpPost(url);
        if (StringUtils.isNotEmpty(cookie)) {
            httpPost.setHeader("Cookie", cookie);
        }
        httpPost.setEntity(new StringEntity(params,ContentType.APPLICATION_JSON));
        try {
            return getClient().execute(httpPost);
        } catch (IOException e) {
            throw new HttpClientFactoryException("POST request connect break" ,e);
        }
    }



}
