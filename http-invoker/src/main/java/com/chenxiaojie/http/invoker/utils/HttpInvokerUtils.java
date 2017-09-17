package com.chenxiaojie.http.invoker.utils;

import com.chenxiaojie.http.invoker.HttpInvoker;
import com.chenxiaojie.http.invoker.annotation.*;
import com.chenxiaojie.http.invoker.entity.HttpInvokerMethod;
import com.chenxiaojie.http.invoker.entity.HttpInvokerMethodResult;
import com.chenxiaojie.http.invoker.entity.HttpResult;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.HttpClient;
import org.apache.http.util.Args;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by chenxiaojie on 16/3/30.
 */
@ThreadSafe
public abstract class HttpInvokerUtils {

    public static HttpInvokerMethod generateHttpInvokerMethod(HttpClient httpClient, String requestUrlPrefix, List<HttpInvoker.Interceptor> interceptors, final Method method, final Object[] arguments) throws FileNotFoundException {

        Args.notNull(method, "method can not be null");
        Args.notNull(requestUrlPrefix, "requestUrlPrefix can not be null");

        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        Args.notNull(requestMapping, "The method " + method.getName() + " must be annotated by @RequestMapping");

        HttpInvoker httpInvoker = httpClient == null ? HttpInvoker.builder() : HttpInvoker.builder(httpClient);

        StringBuilder url = new StringBuilder(requestUrlPrefix);
        url.append(requestMapping.value());

        if (arguments != null) {
            Class<?>[] paramTypes = method.getParameterTypes();
            Annotation[][] paramAnnotations = getParameterAnnotations(method);

            for (int i = 0; i < paramTypes.length; i++) {

                Annotation[] paramAnnotation = paramAnnotations[i];
                if (paramTypes.length == 1 && paramAnnotation.length == 0) {
                    httpInvoker.json(arguments[i]);
                    break;
                }

                for (Annotation annotation : paramAnnotation) {
                    if (annotation instanceof RequestParam) {
                        RequestParam requestParam = (RequestParam) annotation;
                        httpInvoker.data(requestParam.value(), arguments[i] == null ? requestParam.defaultValue() : toString(arguments[i]));
                        break;
                    } else if (annotation instanceof RequestBody) {
                        httpInvoker.json(arguments[i]);
                        break;
                    } else if (annotation instanceof PathVariable) {
                        PathVariable pathVariable = (PathVariable) annotation;
                        replacePathVariable(url, pathVariable.value(), arguments[i] == null ? pathVariable.defaultValue() : toString(arguments[i]));
                        break;
                    } else if (annotation instanceof RequestFile) {
                        RequestFile requestFile = (RequestFile) annotation;
                        InputStream in = null;
                        if (arguments[i] instanceof InputStream) {
                            in = (InputStream) arguments[i];
                        } else if (arguments[i] instanceof File) {
                            in = new FileInputStream((File) arguments[i]);
                        } else if (arguments[i] instanceof String) {
                            byte[] bytes = Base64.decodeBase64(((String) arguments[i]).getBytes());
                            in = new ByteArrayInputStream(bytes);
                        } else if (arguments[i] instanceof byte[]) {
                            in = new ByteArrayInputStream((byte[]) arguments[i]);
                        } else {
                            throw new IllegalArgumentException("RequestFile parameter must be inputstream or file or base64 or byte[]");
                        }
                        httpInvoker.data(requestFile.inputName(), requestFile.value(), in);
                        break;
                    }
                }
            }
        }

        httpInvoker.uri(url.toString());
        httpInvoker.method(requestMapping.method());
        httpInvoker.charset(requestMapping.charset());

        if (interceptors != null && interceptors.size() > 0) {
            for (HttpInvoker.Interceptor interceptor : interceptors) {
                httpInvoker.interceptor(interceptor);
            }
        }

        return new HttpInvokerMethod(httpInvoker, requestMapping.retryTimes(), requestMapping.resultJsonPath(), getHttpInvokerMethodResult(method));
    }

    /**
     * 将对象转成String
     *
     * @param obj
     * @return
     */
    private static String toString(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof Number) {
            return String.valueOf(obj);
        } else if (obj instanceof Collection) {
            return StringUtils.join((Collection) obj, ',');
        } else {
            return obj.toString();
        }
    }


    /**
     * 替换url
     *
     * @param src
     * @param key
     * @param value
     */
    private static void replacePathVariable(StringBuilder src, String key, String value) {
        String realKey = "{" + key + "}";
        int start = src.indexOf(realKey);
        if (start == -1) {
            return;
        }
        src.replace(start, start + realKey.length(), value);
    }

    /**
     * 构造HttpInvokerMethodResult
     *
     * @param method
     * @return
     */
    private static HttpInvokerMethodResult generateHttpInvokerMethodResult(Method method) {
        boolean isReturnHttpResult = method.getReturnType() == HttpResult.class;
        Type returnType = method.getGenericReturnType();
        if (isReturnHttpResult) {
            if (returnType instanceof ParameterizedType) {
                Type[] types = ((ParameterizedType) returnType).getActualTypeArguments();
                if (types != null && types.length == 1) {
                    returnType = types[0];
                }
            }
        }
        return new HttpInvokerMethodResult(basicTypeToPackageType(returnType), isReturnHttpResult);
    }

    /**
     * 将基本类型转换包装类型
     *
     * @param basicType
     * @return
     */
    private static Type basicTypeToPackageType(Type basicType) {
        if (basicType == int.class) {
            return Integer.class;
        }
        if (basicType == boolean.class) {
            return Boolean.class;
        }
        if (basicType == long.class) {
            return Long.class;
        }
        if (basicType == float.class) {
            return Float.class;
        }
        if (basicType == double.class) {
            return Double.class;
        }
        if (basicType == byte.class) {
            return Byte.class;
        }
        if (basicType == short.class) {
            return Short.class;
        }
        if (basicType == char.class) {
            return Character.class;
        }
        return basicType;
    }

    /***************
     * 缓存 *
     **************/
    private static final ConcurrentMap<Method, Annotation[][]> methodParamAnnotationsCached = new ConcurrentHashMap();
    private static final ConcurrentMap<Method, HttpInvokerMethodResult> methodResultCached = new ConcurrentHashMap();

    private static HttpInvokerMethodResult getHttpInvokerMethodResult(Method method) {
        HttpInvokerMethodResult httpInvokerMethodResult = methodResultCached.get(method);
        if (httpInvokerMethodResult == null) {
            synchronized (method) {
                httpInvokerMethodResult = methodResultCached.get(method);
                if (httpInvokerMethodResult == null) {
                    httpInvokerMethodResult = generateHttpInvokerMethodResult(method);
                    methodResultCached.put(method, httpInvokerMethodResult);
                }
            }
        }
        return httpInvokerMethodResult;
    }

    private static Annotation[][] getParameterAnnotations(Method method) {
        Annotation[][] annotations = methodParamAnnotationsCached.get(method);
        if (annotations == null) {
            synchronized (method) {
                annotations = methodParamAnnotationsCached.get(method);
                if (annotations == null) {
                    annotations = method.getParameterAnnotations();
                    methodParamAnnotationsCached.put(method, annotations);
                }
            }
        }
        return annotations;
    }

}