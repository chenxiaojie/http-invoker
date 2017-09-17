package com.chenxiaojie.http.invoker.spring;

import com.chenxiaojie.http.invoker.client.HttpClientBuilder;
import com.google.common.collect.Lists;
import org.apache.http.Header;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.cookie.Cookie;
import org.springframework.beans.factory.FactoryBean;

import java.util.List;

/**
 * Created by chenxiaojie on 2017/9/9.
 */
@NotThreadSafe
public class HttpClientFactoryBean implements FactoryBean<HttpClient> {

    /**
     * 线程池获取超时时间
     */
    private int connectionRequestTimeout = 1000;

    /**
     * 服务器连接超时时间
     */
    private int connectTimeout = 2000;

    /**
     * 数据传输超时时间
     */
    private int socketTimeout = 10000;

    /**
     * 最大连接数
     */
    private int maxConnections = 100;

    /**
     * 连接域名数
     */
    private int hostCount = 1;

    /**
     * 是否重定向
     */
    private boolean followRedirects = false;

    /**
     * 默认头部
     */
    private List<Header> headers = Lists.newArrayList();

    /**
     * 默认cookie
     */
    private List<Cookie> cookies = Lists.newArrayList();

    /**
     * 默认的cookie存储
     */
    private CookieStore cookieStore;

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public void setHostCount(int hostCount) {
        this.hostCount = hostCount;
    }

    public void setFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }

    public void setCookieStore(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }

    @Override
    public HttpClient getObject() throws Exception {
        return HttpClientBuilder.builder()
                .connectionRequestTimeout(connectionRequestTimeout)
                .connectTimeout(connectTimeout)
                .socketTimeout(socketTimeout)
                .maxConnections(maxConnections)
                .hostCount(hostCount)
                .followRedirects(followRedirects)
                .headers(headers)
                .cookies(cookies)
                .cookieStore(cookieStore)
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return HttpClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
