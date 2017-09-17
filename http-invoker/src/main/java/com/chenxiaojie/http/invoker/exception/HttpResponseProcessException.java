package com.chenxiaojie.http.invoker.exception;

/**
 * Created by chenxiaojie on 16/9/9.
 */
public class HttpResponseProcessException extends RuntimeException {

    public HttpResponseProcessException() {
        super("");
    }

    public HttpResponseProcessException(String message) {
        super(message);
    }

    public HttpResponseProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpResponseProcessException(Throwable cause) {
        super(cause);
    }

    protected HttpResponseProcessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
