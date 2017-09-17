package com.chenxiaojie.http.invoker.spring;

import org.apache.commons.lang3.ClassUtils;
import org.apache.http.annotation.NotThreadSafe;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Created by chenxiaojie on 16/3/30.
 */
@NotThreadSafe
public class HttpProxyFactoryBean implements FactoryBean {

    private String proxyInterfaces;
    private InvocationHandler invocationHandler;
    private Object proxy;
    private Class<?> proxyType;

    public void init() throws ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        proxyType = ClassUtils.getClass(classLoader, proxyInterfaces.trim());
        proxy = Proxy.newProxyInstance(classLoader, new Class[]{proxyType}, invocationHandler);
    }

    @Override
    public Object getObject() throws Exception {
        return proxy;
    }

    @Override
    public Class<?> getObjectType() {
        return proxyType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setProxyInterfaces(String proxyInterfaces) {
        this.proxyInterfaces = proxyInterfaces;
    }

    public void setInvocationHandler(InvocationHandler invocationHandler) {
        this.invocationHandler = invocationHandler;
    }
}
