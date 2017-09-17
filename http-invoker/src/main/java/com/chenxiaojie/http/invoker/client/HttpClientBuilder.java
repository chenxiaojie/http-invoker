package com.chenxiaojie.http.invoker.client;

import com.google.common.collect.Lists;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

import java.util.List;

/**
 * Created by chenxiaojie on 2017/9/9.
 * builder出来的httpclient是线程安全的
 */
@NotThreadSafe
public class HttpClientBuilder {

    private HttpClientBuilder() {
    }

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

    public static HttpClientBuilder builder() {
        return new HttpClientBuilder();
    }

    public HttpClientBuilder connectionRequestTimeout(int connectionRequestTimeout) {
        Args.check(connectionRequestTimeout > 0, "connectionRequestTimeout must greater than zero");
        this.connectionRequestTimeout = connectionRequestTimeout;
        return this;
    }

    public HttpClientBuilder connectTimeout(int connectTimeout) {
        Args.check(connectTimeout > 0, "connectTimeout must greater than zero");
        this.connectTimeout = connectTimeout;
        return this;
    }

    public HttpClientBuilder socketTimeout(int socketTimeout) {
        Args.check(socketTimeout > 0, "socketTimeout must greater than zero");
        this.socketTimeout = socketTimeout;
        return this;
    }

    public HttpClientBuilder maxConnections(int maxConnections) {
        Args.check(maxConnections > 0, "maxConnections must greater than zero");
        this.maxConnections = maxConnections;
        return this;
    }

    public HttpClientBuilder hostCount(int hostCount) {
        Args.check(hostCount > 0, "hostCount must greater than zero");
        this.hostCount = hostCount;
        return this;
    }

    public HttpClientBuilder followRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
        return this;
    }

    public HttpClientBuilder header(Header header) {
        Args.notNull(header, "header");
        headers.add(header);
        return this;
    }

    /**
     * 添加一个头部
     *
     * @param name  use HttpHeaders
     * @param value custom value
     * @return
     */
    public HttpClientBuilder header(String name, String value) {
        Args.notBlank(name, "name");
        Args.notBlank(value, "value");
        header(new BasicHeader(name, value));
        return this;
    }

    public HttpClientBuilder headers(List<Header> headers) {
        Args.notNull(headers, "headers");
        for (Header header : headers) {
            header(header);
        }
        return this;
    }

    public HttpClientBuilder cookie(Cookie cookie) {
        Args.notNull(cookie, "cookie");
        this.cookies.add(cookie);
        return this;
    }

    public HttpClientBuilder cookies(List<Cookie> cookies) {
        Args.notNull(cookies, "cookies");
        for (Cookie cookie : cookies) {
            cookie(cookie);
        }
        return this;
    }

    public HttpClientBuilder cookieStore(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
        return this;
    }

    public HttpClient build() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .build();

        /**
         * 线程池
         */
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

        /**
         * 设置总共最大连接数
         */
        connectionManager.setMaxTotal(maxConnections);

        /**
         * 设置每个路由(host)最大连接数
         */
        connectionManager.setDefaultMaxPerRoute(maxConnections / hostCount);

        /**
         * 添加cookie
         */
        if (cookieStore == null) {
            cookieStore = new BasicCookieStore();
        }
        for (Cookie cookie : cookies) {
            cookieStore.addCookie(cookie);
        }

        /**
         * 构造一个常用的httpClient
         */
        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .setRedirectStrategy(followRedirects ? new LaxRedirectStrategy() : new DefaultRedirectStrategy() {
                    @Override
                    public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                        return false;
                    }
                })
                .setDefaultHeaders(headers)
                .setDefaultCookieStore(cookieStore)
                .build();
    }

}
