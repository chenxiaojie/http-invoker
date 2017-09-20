package com.chenxiaojie.http.invoker.enums;

import org.apache.http.client.methods.*;

/**
 * Created by chenxiaojie on 16/3/30.
 */
public enum HttpMethod {

    GET(HttpGet.class, false, true),

    /**
     * 官方推荐当上传文件等multipart的表单只能使用POST
     */
    POST(HttpPost.class, true, true),

    /**
     * 不支持multipart
     */
    PUT(HttpPut.class, true, true),

    DELETE(HttpDelete.class, false, true),

    /**
     * 不支持multipart
     */
    PATCH(HttpPatch.class, true, true),

    /**
     * 下方请求无返回内容,不符合本框架的主要功能,尽量不要使用
     */
    HEAD(HttpHead.class, false, false),
    OPTIONS(HttpOptions.class, false, false),
    TRACE(HttpTrace.class, false, false);

    private final Class<? extends HttpRequestBase> clazz;
    /**
     * HttpEntityEnclosingRequestBase.class.isAssignableFrom(HttpGet.class)
     */
    private final boolean hasRequestBody;
    private final boolean hasResponseBody;

    HttpMethod(Class<? extends HttpRequestBase> clazz, boolean hasRequestBody, boolean hasResponseBody) {
        this.clazz = clazz;
        this.hasRequestBody = hasRequestBody;
        this.hasResponseBody = hasResponseBody;
    }

    public Class<? extends HttpRequestBase> getHttpMethodClass() {
        return this.clazz;
    }

    public boolean hasRequestBody() {
        return hasRequestBody;
    }

    public boolean hasResponseBody() {
        return hasResponseBody;
    }

    public static HttpMethod generateByName(String name) {
        for (HttpMethod httpMethod : HttpMethod.values()) {
            if (httpMethod.name().equalsIgnoreCase(name)) {
                return httpMethod;
            }
        }
        return null;
    }
}
