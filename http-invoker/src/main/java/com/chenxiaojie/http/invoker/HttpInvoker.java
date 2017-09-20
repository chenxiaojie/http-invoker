package com.chenxiaojie.http.invoker;

import com.alibaba.fastjson.JSON;
import com.chenxiaojie.http.invoker.client.HttpClientBuilder;
import com.chenxiaojie.http.invoker.enums.HttpMethod;
import com.chenxiaojie.http.invoker.exception.HttpRequestException;
import com.chenxiaojie.http.invoker.monitor.Monitor;
import com.chenxiaojie.http.invoker.monitor.MonitorLoader;
import com.chenxiaojie.http.invoker.monitor.MonitorTransaction;
import com.chenxiaojie.http.invoker.utils.MimeTypes;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chenxiaojie on 2017/9/9.
 * 每次builder之后都会new出来新对象,new出来的对象不是线程安全的,建议每次都调用builder
 */
@NotThreadSafe
public class HttpInvoker {

    private static final Monitor monitor = MonitorLoader.getMonitor();

    private static final String HTTP_TRANSACTION = "HTTP_TRANSACTION";

    private volatile static HttpClient DEFAULT_HTTP_CLIENT;

    private final HttpClient httpClient;

    private final Request request = new Request();

    private final Response response = new Response();

    private HttpInvoker(HttpClient httpClient) {
        Args.notNull(httpClient, "httpClient");
        this.httpClient = httpClient;
    }

    /**
     * @return 返回一个共享httpClient的HttpInvoker
     */
    public static HttpInvoker builder() {
        if (DEFAULT_HTTP_CLIENT == null) {
            synchronized (monitor) {
                if (DEFAULT_HTTP_CLIENT == null) {
                    DEFAULT_HTTP_CLIENT = HttpClientBuilder.builder().build();
                }
            }
        }
        return builder(DEFAULT_HTTP_CLIENT);
    }

    /**
     * @param httpClient httpClient
     * @return 返回一个独享httpClient的HttpInvoker, 传入自身维护的httpClient, qps高, 高性能模式下使用
     */
    public static HttpInvoker builder(HttpClient httpClient) {
        return new HttpInvoker(httpClient);
    }


    public HttpInvoker uri(URI uri) {
        Args.notNull(uri, "uri");
        this.request.uri = uri;
        return this;
    }

    public HttpInvoker uri(String uri) {
        Args.notBlank(uri, "uri");
        try {
            return uri(new URI(uri));
        } catch (URISyntaxException e) {
            throw new HttpRequestException(e.getMessage(), e);
        }
    }

    public HttpInvoker method(HttpMethod method) {
        Args.notNull(method, "method");
        this.request.method = method;
        return this;
    }

    public HttpInvoker method(String method) {
        return method(HttpMethod.generateByName(method));
    }

    private HttpInvoker data(Request.Parameter parameter) {
        Args.notNull(parameter, "parameter");
        this.request.parameters.add(parameter);
        return this;
    }

    public HttpInvoker data(String name, String value) {
        this.data(Request.Parameter.create(name, value));
        return this;
    }

    /**
     * 上传文件
     *
     * @param name        type="file"时name属性,例如:file
     * @param filename    例如:attachment.png
     * @param inputStream 文件流
     * @return HttpInvoker
     */
    public HttpInvoker data(String name, String filename, InputStream inputStream) {
        Args.check(filename.lastIndexOf(".") != -1, "filename must contains dot, such as attachment.png");
        this.data(Request.Parameter.create(name, filename, inputStream));
        return this;
    }

    public HttpInvoker data(Map<String, String> data) {
        Args.notNull(data, "data");
        for (Map.Entry<String, String> entry : data.entrySet()) {
            this.data(Request.Parameter.create(entry.getKey(), entry.getValue()));
        }
        return this;
    }

    public HttpInvoker data(String... parameters) {
        Args.notNull(parameters, "parameters");
        Args.check(parameters.length % 2 == 0, "Must supply an even number of name value pairs");
        for (int i = 0; i < parameters.length; i += 2) {
            String name = parameters[i];
            String value = parameters[i + 1];
            this.data(Request.Parameter.create(name, value));
        }
        return this;
    }

    public HttpInvoker json(String json) {
        Args.notNull(json, "json");
        Args.check(!this.request.hasJSON(), "json body can only be set once");
        this.request.json = json;
        return this;
    }

    public HttpInvoker json(Object json) {
        if (json instanceof String) {
            return json((String) json);
        } else {
            return json(JSON.toJSONString(json));
        }
    }

    public HttpInvoker header(String name, String value) {
        Args.notBlank(name, "name");
        Args.notBlank(value, "value");
        if (this.request.headers == null) {
            this.request.headers = Maps.newHashMap();
        }
        this.request.headers.put(name, value);
        return this;
    }

    public HttpInvoker headers(Map<String, String> headers) {
        Args.notNull(headers, "headers");
        if (this.request.headers == null) {
            this.request.headers = Maps.newHashMap();
        }
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            header(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public HttpInvoker cookie(String name, String value) {
        Args.notBlank(name, "name");
        Args.notBlank(value, "value");
        if (this.request.cookies == null) {
            this.request.cookies = Maps.newHashMap();
        }
        this.request.cookies.put(name, value);
        return this;
    }

    public HttpInvoker cookies(Map<String, String> cookies) {
        Args.notNull(cookies, "cookies");
        if (this.request.cookies == null) {
            this.request.cookies = Maps.newHashMap();
        }
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            cookie(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public HttpInvoker interceptor(Interceptor interceptor) {
        Args.notNull(interceptor, "interceptor");
        if (this.request.interceptors == null) {
            this.request.interceptors = Lists.newArrayList();
        }
        this.request.interceptors.add(interceptor);
        return this;
    }

    public HttpInvoker charset(Charset charSet) {
        Args.notNull(charSet, "charSet");
        this.request.charSet = charSet;
        this.response.charSet = charSet;
        return this;
    }

    public HttpInvoker charset(String charSet) {
        Args.notBlank(charSet, "charSet");
        this.charset(Charset.forName(charSet));
        return this;
    }

    public HttpInvoker requestCharset(Charset charSet) {
        Args.notNull(charSet, "charSet");
        this.request.charSet = charSet;
        return this;
    }

    public HttpInvoker requestCharset(String charSet) {
        Args.notBlank(charSet, "charSet");
        this.requestCharset(Charset.forName(charSet));
        return this;
    }

    public HttpInvoker responseCharset(Charset charSet) {
        Args.notNull(charSet, "charSet");
        this.response.charSet = charSet;
        return this;
    }

    public HttpInvoker responseCharset(String charSet) {
        Args.notBlank(charSet, "charSet");
        this.responseCharset(Charset.forName(charSet));
        return this;
    }

    public Response get() throws HttpRequestException {
        this.method(HttpMethod.GET);
        return this.execute();
    }

    public Response post() throws HttpRequestException {
        this.method(HttpMethod.POST);
        return this.execute();
    }

    public Response put() throws HttpRequestException {
        this.method(HttpMethod.PUT);
        return this.execute();
    }

    public Response delete() throws HttpRequestException {
        this.method(HttpMethod.DELETE);
        return this.execute();
    }

    public Response patch() throws HttpRequestException {
        this.method(HttpMethod.PATCH);
        return this.execute();
    }

    public Response head() throws HttpRequestException {
        this.method(HttpMethod.HEAD);
        return this.execute();
    }

    public Response options() throws HttpRequestException {
        this.method(HttpMethod.OPTIONS);
        return this.execute();
    }

    public Response trace() throws HttpRequestException {
        this.method(HttpMethod.TRACE);
        return this.execute();
    }

    /**
     * 支持多次重入
     *
     * @return Response
     * @throws HttpRequestException HttpRequestException
     */
    public Response execute() throws HttpRequestException {

        Request req = this.request;
        Response res = this.response;

        //删除上次请求日志
        if (res.log.length() > 0) {
            res.log.delete(0, res.log.length());
        }

        String transaction = req.method.name() + "." + req.uri.toString();

        res.log.append(transaction).append('\n');

        MonitorTransaction monitorTransaction = monitor.newTransaction(HTTP_TRANSACTION, transaction);

        HttpRequestBase httpRequestBase = null;
        try {
            httpRequestBase = req.toHttpRequest();

            if (httpRequestBase.getURI().getQuery() != null) {
                res.log.append("query: ").append(httpRequestBase.getURI().getQuery()).append('\n');
            }

            if (req.method.hasRequestBody()) {
                if (req.hasJSON()) {
                    res.log.append("requestBody: ").append(req.json).append('\n');
                } else if (req.hasParameter()) {
                    String requestBody = req.parameterToString();
                    if (StringUtils.isNotEmpty(requestBody)) {
                        res.log.append("requestBody: ").append(requestBody).append('\n');
                    }
                }
            }

            if (req.hasInterceptor()) {
                for (Interceptor interceptor : req.interceptors) {
                    // 拦截器返回false，则结束请求
                    if (!interceptor.intercept(httpRequestBase)) {
                        res.success = false;
                        return res;
                    }
                }
            }

            HttpResponse httpResponse = httpClient.execute(httpRequestBase);

            res.status = httpResponse.getStatusLine().getStatusCode();

            Header[] headers = httpResponse.getAllHeaders();
            if (headers != null && headers.length > 0) {
                for (Header header : headers) {
                    res.headers.put(header.getName(), header.getValue());
                }
            }

            if (req.method.hasResponseBody()) {

                HttpEntity httpEntity = httpResponse.getEntity();

                try {
                    final ContentType contentType = ContentType.get(httpEntity);
                    if (contentType != null) {
                        Charset charset = contentType.getCharset();
                        if (charset != null) {
                            res.charSet = charset;
                        }
                    }
                } catch (Exception ex) {
                }

                res.bodyBytes = EntityUtils.toByteArray(httpEntity);

                EntityUtils.consume(httpEntity);
            }
            monitorTransaction.success();
        } catch (Throwable e) {
            monitorTransaction.failure(e);
            res.success = false;
            String errorMsg = e.getMessage();
            if (StringUtils.isEmpty(errorMsg) && e.getCause() != null) {
                errorMsg = e.getCause().getMessage();
            }
            if (StringUtils.isEmpty(errorMsg)) {
                errorMsg = "no error message";
            }
            res.log.insert(0, req.method.name() + " error, msg: " + errorMsg + "\n");
            throw new HttpRequestException(res.log.toString(), e);
        } finally {
            monitorTransaction.complete();

            if (httpRequestBase != null) {
                httpRequestBase.abort();
            }
        }
        return res;
    }

    private static class Request {

        private Charset charSet = Charset.forName("UTF-8");

        private URI uri;

        private HttpMethod method;

        private String json = "";

        private List<Parameter> parameters = Lists.newArrayList();

        private Map<String, String> headers;

        private Map<String, String> cookies;

        private List<Interceptor> interceptors;

        /**
         * @return 是否有jsonbody
         */
        private boolean hasJSON() {
            return StringUtils.isNotEmpty(json);
        }

        /**
         * @return 是否有参数
         */
        private boolean hasParameter() {
            return parameters != null && parameters.size() > 0;
        }

        /**
         * @return 是否有头部
         */
        private boolean hasHeader() {
            return headers != null && headers.size() > 0;
        }

        /**
         * @return 是否有cookie
         */
        private boolean hasCookie() {
            return cookies != null && cookies.size() > 0;
        }

        /**
         * @return 是否有文件
         */
        private boolean needsMultipart() {
            for (Parameter parameter : parameters) {
                if (parameter.hasInputStream()) {
                    return true;
                }
            }
            return false;
        }

        private boolean hasInterceptor() {
            return interceptors != null && interceptors.size() > 0;
        }

        /**
         * @return 没有body的请求将参数放在url后面
         */
        private String getParameterString() {
            if (hasParameter()) {
                StringBuilder sb = new StringBuilder(parameters.size() * 50);
                for (Parameter parameter : parameters) {
                    if (!parameter.hasInputStream()) {
                        sb.append(parameter.name)
                                .append('=')
                                .append(parameter.value)
                                .append('&');
                    }
                }
                sb.delete(sb.length() - 1, sb.length());
                return sb.toString();
            }
            return "";
        }

        /**
         * @return 打日志专用
         */
        private String parameterToString() {
            if (hasParameter()) {
                StringBuilder sb = new StringBuilder(parameters.size() * 30);
                for (Parameter parameter : parameters) {
                    sb.append(parameter.name)
                            .append('=')
                            .append(parameter.value)
                            .append('&');
                }
                sb.delete(sb.length() - 1, sb.length());
                return sb.toString();
            }
            return "";
        }

        /**
         * @return 组装cookie的字符串
         */
        private String getCookieString() {
            if (hasCookie()) {
                StringBuilder sb = new StringBuilder(cookies.size() * 50);
                for (Map.Entry<String, String> entry : cookies.entrySet()) {
                    sb.append(entry.getKey())
                            .append('=')
                            .append(entry.getValue())
                            .append("; ");
                }
                sb.delete(sb.length() - 1, sb.length());
                return sb.toString();
            }
            return "";
        }

        public HttpRequestBase toHttpRequest() throws IllegalAccessException, InstantiationException, URISyntaxException {
            Args.notNull(uri, "request uri");
            Args.notNull(method, "request method");

            HttpRequestBase httpRequestBase = method.getHttpMethodClass().newInstance();

            URI _uri = uri;

            boolean parameterAlreadyUsed = false;

            if (method.hasRequestBody()) {
                HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase = (HttpEntityEnclosingRequestBase) httpRequestBase;
                HttpEntity httpEntity = null;
                if (hasJSON()) {
                    httpEntityEnclosingRequestBase.setHeader(HTTP.CONTENT_TYPE, ContentType.create("application/json", charSet).toString());
                    StringEntity stringEntity = new StringEntity(json, charSet);
                    stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING, charSet.toString()));
                    stringEntity.setContentType("text/json");
                    httpEntity = stringEntity;
                } else if (needsMultipart()) {
                    MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().setCharset(charSet);
                    for (Request.Parameter parameter : parameters) {
                        if (parameter.hasInputStream()) {
                            String mimeType = MimeTypes.MimeType.get(parameter.value.substring(parameter.value.lastIndexOf(".")));
                            Args.notBlank(mimeType, "not found mimeType, please MimeTypes.MimeType.put your file MimeType");
                            ContentType contentType = ContentType.create(mimeType, charSet);
                            multipartEntityBuilder.addBinaryBody(parameter.name, parameter.inputStream, contentType, parameter.value);
                        } else {
                            multipartEntityBuilder.addTextBody(parameter.name, parameter.value, ContentType.create("text/plain", charSet));
                        }
                    }
                    parameterAlreadyUsed = true;
                    httpEntity = multipartEntityBuilder.build();
                } else if (hasParameter()) {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    for (Request.Parameter parameter : parameters) {
                        nameValuePairs.add(new BasicNameValuePair(parameter.name, parameter.value));
                    }
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs, charSet);
                    httpEntity = urlEncodedFormEntity;
                    parameterAlreadyUsed = true;
                }

                if (httpEntity != null) {
                    httpEntityEnclosingRequestBase.setEntity(httpEntity);
                }
            }

            if (!parameterAlreadyUsed && hasParameter()) {
                String parameterString = getParameterString();
                String query = uri.getQuery() == null ? parameterString : uri.getQuery() + "&" + parameterString;
                _uri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), uri.getPath(), query, uri.getFragment());
            }

            httpRequestBase.setURI(_uri);

            if (hasHeader()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpRequestBase.setHeader(entry.getKey(), entry.getValue());
                }
            }

            if (hasCookie()) {
                httpRequestBase.setHeader("Cookie", getCookieString());
            }

            return httpRequestBase;
        }

        private static class Parameter {
            private String name;
            private String value;
            private InputStream inputStream;

            private Parameter(String name, String value, InputStream inputStream) {
                this.name = name;
                this.value = value;
                this.inputStream = inputStream;
            }

            boolean hasInputStream() {
                return inputStream != null;
            }

            static Parameter create(String name, String value) {
                return create(name, value, null);
            }

            static Parameter create(String name, String value, InputStream inputStream) {
                Args.notBlank(name, "parameter name");
                Args.notNull(value, "parameter value");
                return new Parameter(name, value, inputStream);
            }
        }

    }

    public static class Response {

        private static final Logger logger = LoggerFactory.getLogger(Response.class);

        private Charset charSet = Charset.forName("UTF-8");

        private boolean success = true;

        private int status = HttpStatus.SC_OK;

        private byte[] bodyBytes;

        private String body = null;

        private Map<String, String> headers = Maps.newHashMap();

        private StringBuilder log = new StringBuilder(300);

        private String logString = null;

        public Charset charSet() {
            return charSet;
        }

        public boolean isSuccess() {
            return success;
        }

        public int status() {
            return status;
        }

        public byte[] bodyBytes() {
            return bodyBytes;
        }

        public String body() {
            if (body == null) {
                if (bodyBytes == null) {
                    body = "";
                } else {
                    body = new String(bodyBytes, charSet);
                }
            }
            return body;
        }

        public Map<String, String> headers() {
            return headers;
        }

        private StringBuilder getLog() {
            log.append("responseStatus: ")
                    .append(status)
                    .append('\n');

            if (status != HttpStatus.SC_OK) {
                log.append("responseHeaders: \n");
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    log.append("header: ").append(entry.getKey()).append(": ").append(entry.getValue()).append('\n');
                }
            }

            log.append("responseBody: ");
            String body = body();
            if (body.length() > 1000) {
                log.append(body.substring(0, 1000)).append("...");
            } else {
                log.append(body);
            }
            return log.append('\n');
        }

        public String logString() {
            if (logString == null) {
                logString = getLog().toString();
            }
            return logString;
        }

        public void log() {
            if (success) {
                logger.info(logString());
            } else {
                logger.error(logString());
            }
        }

        /**
         * 将返回体转换成对象
         *
         * @param type 转换类型
         * @param <T>  泛型
         * @return 返回类型对象
         */
        public <T> T toType(Type type) {
            if (String.class == type) {
                return (T) body();
            }
            return JSON.parseObject(body(), type);
        }
    }

    public interface Interceptor {
        /**
         * 请求前的拦截
         *
         * @param httpRequestBase httpRequestBase
         * @return 返回false, 则结束请求
         * @throws HttpRequestException HttpRequestException
         */
        boolean intercept(HttpRequestBase httpRequestBase) throws HttpRequestException;
    }
}
