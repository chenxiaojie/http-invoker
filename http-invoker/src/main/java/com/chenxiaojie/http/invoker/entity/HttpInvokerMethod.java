package com.chenxiaojie.http.invoker.entity;


import com.chenxiaojie.http.invoker.HttpInvoker;

/**
 * Created by chenxiaojie on 16/3/30.
 */
public class HttpInvokerMethod {

    private HttpInvoker httpInvoker;

    private int retryTimes;

    private String resultJsonPath;

    private HttpInvokerMethodResult httpInvokerMethodResult;

    public HttpInvokerMethod(HttpInvoker httpInvoker, int retryTimes, String resultJsonPath, HttpInvokerMethodResult httpInvokerMethodResult) {
        this.httpInvoker = httpInvoker;
        this.retryTimes = retryTimes;
        this.resultJsonPath = resultJsonPath;
        this.httpInvokerMethodResult = httpInvokerMethodResult;
    }

    public HttpInvoker getHttpInvoker() {
        return httpInvoker;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public String getResultJsonPath() {
        return resultJsonPath;
    }

    public HttpInvokerMethodResult getHttpInvokerMethodResult() {
        return httpInvokerMethodResult;
    }
}
