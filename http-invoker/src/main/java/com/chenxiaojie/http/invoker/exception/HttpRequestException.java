package com.chenxiaojie.http.invoker.exception;

/**
 * Created by chenxiaojie on 16/9/9.
 */
public class HttpRequestException extends RuntimeException {

    public HttpRequestException() {
        super("");
    }

    public HttpRequestException(String message) {
        super(message);
    }

    public HttpRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpRequestException(Throwable cause) {
        super(cause);
    }

    protected HttpRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
