package com.chenxiaojie.http.invoker.entity;

import java.lang.reflect.Type;

/**
 * Created by chenxiaojie on 16/3/30.
 */
public class HttpInvokerMethodResult {

    /**
     * 返回值类型
     */
    private Type returnType;
    /**
     * 是否返回HttpResult
     */
    private boolean isReturnHttpResult;

    public HttpInvokerMethodResult(Type returnType, boolean isReturnHttpResult) {
        this.returnType = returnType;
        this.isReturnHttpResult = isReturnHttpResult;
    }

    public Type getReturnType() {
        return returnType;
    }

    public boolean isReturnHttpResult() {
        return isReturnHttpResult;
    }
}
