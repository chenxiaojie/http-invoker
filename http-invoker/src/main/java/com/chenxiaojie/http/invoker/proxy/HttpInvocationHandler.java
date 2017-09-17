package com.chenxiaojie.http.invoker.proxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.chenxiaojie.http.invoker.HttpInvoker;
import com.chenxiaojie.http.invoker.entity.HttpInvokerMethod;
import com.chenxiaojie.http.invoker.entity.HttpResult;
import com.chenxiaojie.http.invoker.exception.HttpRequestException;
import com.chenxiaojie.http.invoker.exception.HttpResponseProcessException;
import com.chenxiaojie.http.invoker.utils.HttpInvokerUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by chenxiaojie on 16/3/30.
 */
@NotThreadSafe
public class HttpInvocationHandler implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(HttpInvocationHandler.class);

    private static final Type stringMapType = (new TypeReference<Map<String, String>>() {
    }).getType();

    /**
     * 自定义的httpClient,可通过HttpClientBuilder构造,可以不传
     */
    private HttpClient httpClient;

    /**
     * 所有请求url的前缀,比如https://www.chenxiaojie.com,可以不传
     */
    private String requestUrlPrefix = "";

    /**
     * 是否记录请求详情日志,可以不传
     */
    private boolean openLog = true;

    /**
     * 在每一次请求前面拦截,如果返回false则结束请求,可以不传
     */
    private List<HttpInvoker.Interceptor> interceptors = Lists.newArrayList();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        HttpInvokerMethod invokerMethod = HttpInvokerUtils.generateHttpInvokerMethod(httpClient, requestUrlPrefix, interceptors, method, args);

        HttpInvoker.Response response;
        try {
            response = generateResponse(invokerMethod);
        } catch (HttpRequestException e) {
            if (invokerMethod.getHttpInvokerMethodResult().isReturnHttpResult()) {
                return HttpResult.fail(e.getMessage());
            }
            throw e;
        }

        Object result = null;
        try {
            if (StringUtils.isEmpty(invokerMethod.getResultJsonPath())) {
                result = response.toType(invokerMethod.getHttpInvokerMethodResult().getReturnType());
            } else {
                Map<String, String> map = response.toType(stringMapType);
                String[] keys;
                if (!invokerMethod.getResultJsonPath().contains(".")) {
                    keys = new String[]{invokerMethod.getResultJsonPath()};
                } else {
                    keys = invokerMethod.getResultJsonPath().split("\\.");
                }
                String value = pickMapValue(map, keys, 0);
                result = parseSimpleResult(value, invokerMethod.getHttpInvokerMethodResult().getReturnType());
            }
        } catch (Throwable e) {
            String errorMessage = "process responseBody error, errorMessage: " + e.getMessage() + "\n" + response.getLogString();
            logger.error(errorMessage, e);
            if (invokerMethod.getHttpInvokerMethodResult().isReturnHttpResult()) {
                return HttpResult.fail(errorMessage);
            }
            throw new HttpResponseProcessException(errorMessage, e);
        }

        if (invokerMethod.getHttpInvokerMethodResult().isReturnHttpResult()) {
            return HttpResult.success(result);
        }

        return result;
    }

    private HttpInvoker.Response generateResponse(HttpInvokerMethod invokerMethod) throws HttpRequestException {

        HttpRequestException exception = null;
        HttpInvoker.Response response = null;

        for (int i = 0; i <= invokerMethod.getRetryTimes(); i++) {
            try {
                response = invokerMethod.getHttpInvoker().execute();
                if (HttpStatus.SC_OK != response.status()) {
                    throw new HttpRequestException("HttpStatusCode not 200, HttpStatusCode: " + response.status() + "\n" + response.getLogString());
                }
                if (StringUtils.isEmpty(response.body())) {
                    throw new HttpRequestException("body is empty\n" + response.getLogString());
                }
                break;
            } catch (HttpRequestException e) {
                if (invokerMethod.getRetryTimes() == i) {
                    exception = e;
                } else {
                    logger.warn("request error, retryTimes: " + i + ", errorMessage: " + e.getMessage());
                }
            }
        }

        if (exception != null) {
            throw exception;
        }

        if (response == null) {
            throw new HttpRequestException("generate response is null");
        }

        if (openLog) {
            response.log();
        }

        if (!response.isSuccess()) {
            throw new HttpRequestException(response.getLogString());
        }

        return response;
    }

    private String pickMapValue(Map<String, String> map, String[] keys, int pickIndex) {
        if (map == null || keys == null || pickIndex >= keys.length) {
            return null;
        }

        String key = keys[pickIndex];
        String value;

        if (key.contains("[") && key.contains("]")) {
            String _key = key.substring(0, key.indexOf("["));
            JSONArray array = JSON.parseArray(map.get(_key));

            if (array == null || array.size() == 0) {
                return null;
            }

            int index = Integer.parseInt(key.substring(key.indexOf("[") + 1, key.indexOf("]")));
            if (index >= array.size()) {
                return null;
            }

            value = array.getString(index);
        } else {
            value = map.get(key);
        }

        if (++pickIndex == keys.length) {
            return value;
        }

        if (StringUtils.isEmpty(value)) {
            return null;
        }

        return pickMapValue((Map<String, String>) JSON.parseObject(value, stringMapType), keys, pickIndex);
    }

    private Object parseSimpleResult(String resultMapValue, Type returnType) {
        if (StringUtils.isEmpty(resultMapValue)) {
            return null;
        }
        if (String.class == returnType) {
            return resultMapValue;
        }
        if (returnType == Integer.class) {
            return Integer.parseInt(resultMapValue);
        }
        if (returnType == Boolean.class) {
            return Boolean.parseBoolean(resultMapValue);
        }
        if (returnType == Long.class) {
            return Long.parseLong(resultMapValue);
        }
        if (returnType == Float.class) {
            return Float.parseFloat(resultMapValue);
        }
        if (returnType == Double.class) {
            return Double.parseDouble(resultMapValue);
        }
        if (returnType == Byte.class) {
            return Byte.parseByte(resultMapValue);
        }
        if (returnType == Short.class) {
            return Short.parseShort(resultMapValue);
        }
        if (returnType == Character.class) {
            return resultMapValue.charAt(0);
        }
        return JSON.parseObject(resultMapValue, returnType);
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void setRequestUrlPrefix(String requestUrlPrefix) {
        this.requestUrlPrefix = requestUrlPrefix;
    }

    public void setOpenLog(boolean openLog) {
        this.openLog = openLog;
    }

    public void setInterceptors(List<HttpInvoker.Interceptor> interceptors) {
        this.interceptors = interceptors;
    }
}