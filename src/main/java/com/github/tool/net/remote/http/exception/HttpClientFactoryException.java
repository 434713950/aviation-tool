package com.github.tool.net.remote.http.exception;

/**
 * @Author: PengCheng
 * @Description:
 * @Date: 2018/9/19
 */
public class HttpClientFactoryException extends RuntimeException{

    public HttpClientFactoryException() {
    }

    public HttpClientFactoryException(String message) {
        super(message);
    }

    public HttpClientFactoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpClientFactoryException(Throwable cause) {
        super(cause);
    }

    public HttpClientFactoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
