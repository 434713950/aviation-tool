package com.github.tool.net.remote.http.factory;

import com.github.tool.net.remote.http.config.ConnectionProperties;
import com.github.tool.net.remote.http.exception.HttpClientFactoryException;
import org.apache.http.*;
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
import java.util.Map;

/**
 * <p>httpclient 远程调用工厂</p>
 * singleton
 * @author PengCheng
 * @date 2018/11/15
 */
public class HttpClientFactory {

    private volatile static HttpClientFactory factory;

    private CloseableHttpClient client;

    private HttpClientFactory(ConnectionProperties connectionProperties){
        //线程池处理器
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = HttpClientManager.poolingHttpClientConnectionManager(connectionProperties.getMaxTotal(),connectionProperties.getDefaultMaxPerRoute());
        RequestConfig requestConfig = HttpClientManager.requestConfig(connectionProperties.getConnectTimeout(),connectionProperties.getConnectionRequestTimeout(),connectionProperties.getSocketTimeout(),connectionProperties.isStaleConnectionCheckEnabled());
        HttpRequestRetryHandler httpRequestRetryHandler = HttpClientManager.httpRequestRetryHandler(connectionProperties.getRetryTime());

        // 建议此处使用HttpClients.custom的方式来创建HttpClientBuilder，而不要使用HttpClientBuilder.create()方法来创建HttpClientBuilder
        // 从官方文档可以得出，HttpClientBuilder是非线程安全的，但是HttpClients确实Immutable的，immutable 对象不仅能够保证对象的状态不被改变，
        // 而且还可以不使用锁机制就能被其他线程共享
        client = HttpClients.custom().setConnectionManager(poolingHttpClientConnectionManager)
                //重试策略
                .setRetryHandler(httpRequestRetryHandler)
                .setDefaultRequestConfig(requestConfig)
                //	.setKeepAliveStrategy(connectionKeepAliveStrategy)  保持策略
                //	.setRoutePlanner(proxyRoutePlanner)         路由策略
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
        return httpGet(url,null);
    }

    public String httpGet(String url,Map<String,String> headers){
        HttpGet httpGet = new HttpGet(url);
        if (headers!=null && !headers.isEmpty()){
            for (Map.Entry<String,String> header: headers.entrySet()){
                httpGet.addHeader(header.getKey(),header.getValue());
            }
        }
        try {
            HttpResponse response =  getClient().execute(httpGet);
            return responseHandle(response, "UTF-8");
        } catch (IOException e) {
            throw new HttpClientFactoryException("GET request connect break",e);
        }
    }


    /**
     * post请求
     * @param url            请求的路径
     * @param params         携带的参数
     * @return               响应的内容
     */
    public String httpPost(String url,String params) {
        return httpPost(url,params, "UTF-8",null);
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
     * @param encoding       响应数据编码
     * @param headers        请求头
     * @return               响应的内容
     */
    public String httpPost(String url,String params,String encoding,Map<String,String> headers) {
        CloseableHttpResponse response = null;
        try {
            response = (CloseableHttpResponse) httpPostForResponse(url, params, headers);
            return responseHandle(response, encoding);
        } finally {
            try {
                if (response!=null){
                    response.close();
                }
            } catch (IOException e) {
                throw new HttpClientFactoryException("POST request connect break",e);
            }
        }
    }

    /**
     * 发送post请求并返回响应体对象
     * @param url           请求路径
     * @param params        请求参数
     * @param headers       请求头
     * @return              响应流
     */
    private HttpResponse httpPostForResponse(String url, String params,Map<String,String> headers) {
        HttpPost httpPost = new HttpPost(url);
        if (headers!=null && !headers.isEmpty()){
            for (Map.Entry<String,String> header: headers.entrySet()){
                httpPost.addHeader(header.getKey(),header.getValue());
            }
        }
        httpPost.setEntity(new StringEntity(params,ContentType.APPLICATION_JSON));
        try {
            return getClient().execute(httpPost);
        } catch (IOException e) {
            throw new HttpClientFactoryException("POST request connect break" ,e);
        }
    }

    /**
     * 响应流处理
     * @param response  响应体
     * @param encoding  解析的编码
     * @return
     */
    private String responseHandle(HttpResponse response,String encoding){
        try {
            int httpStatus = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if (httpStatus == HttpStatus.SC_OK) {
                //这个方法会关闭流
                return EntityUtils.toString(entity, encoding);
            }
            EntityUtils.consume(entity);
            throw new HttpClientFactoryException("response error：" + httpStatus);
        } catch (IOException e) {
            throw new HttpClientFactoryException("response data handle fail",e);
        }
    }
}
