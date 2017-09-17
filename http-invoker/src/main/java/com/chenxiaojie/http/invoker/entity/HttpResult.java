package com.chenxiaojie.http.invoker.entity;


/**
 * Created by chenxiaojie on 16/3/30.
 */
public class HttpResult<T> {

    private T data;
    private Object errorMessage;
    private boolean isSuccess = true;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Object getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Object errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public static HttpResult success(Object data) {
        HttpResult result = new HttpResult();
        result.data = data;
        return result;
    }

    public static HttpResult fail(Object errorMessage) {
        HttpResult result = new HttpResult();
        result.isSuccess = false;
        result.errorMessage = errorMessage;
        return result;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "data=" + data +
                ", errorMessage=" + errorMessage +
                ", isSuccess=" + isSuccess +
                '}';
    }
}
