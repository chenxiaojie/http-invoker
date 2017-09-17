package com.chenxiaojie.http.invoker.test.vo;

import java.io.Serializable;

/**
 * Created by chenxiaojie on 15/7/16.
 * 公用的请求json结果类
 */
public class Response<T> implements Serializable {

    public static final int INTERNAL_ERROR = 500;
    public static final int SUCCESS = 200;

    /**
     * 返回码,非200则出错
     */
    private int code = SUCCESS;
    /**
     * 提示信息(大部分是报错信息,也可以是提示信息)
     */
    private String msg;
    /**
     * 返回数据
     */
    private T data;

    public Response() {
    }

    public Response(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> Response<T> success() {
        return new Response<T>(SUCCESS, "", null);
    }

    public static <T> Response<T> success(T data) {
        return new Response<T>(SUCCESS, "", data);
    }

    public static <T> Response<T> successMsg(String msg) {
        return new Response<T>(SUCCESS, msg, null);
    }

    public static <T> Response<T> success(T data, String msg) {
        return new Response<T>(SUCCESS, msg, data);
    }

    public static <T> Response<T> fail(String msg) {
        return new Response<T>(INTERNAL_ERROR, msg, null);
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
