package com.chenxiaojie.http.invoker.utils;

import org.apache.http.annotation.ThreadSafe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chenxiaojie on 16/3/30.
 */
@ThreadSafe
public abstract class ExtensionLoader {

    private static final Map<Class<?>, Object> extensionMap = new ConcurrentHashMap<Class<?>, Object>();

    private static final Map<Class<?>, List<?>> extensionListMap = new ConcurrentHashMap<Class<?>, List<?>>();

    public static <T> T getExtension(Class<T> clazz) {
        T extension = (T) extensionMap.get(clazz);
        if (extension == null) {
            extension = newExtension(clazz);
            if (extension != null) {
                extensionMap.put(clazz, extension);
            }
        }
        return extension;
    }

    public static <T> List<T> getExtensionList(Class<T> clazz) {
        List<T> extensions = (List<T>) extensionListMap.get(clazz);
        if (extensions == null) {
            extensions = newExtensionList(clazz);
            if (!extensions.isEmpty()) {
                extensionListMap.put(clazz, extensions);
            }
        }
        return extensions;
    }

    public static <T> T newExtension(Class<T> clazz) {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz);
        for (T service : serviceLoader) {
            return service;
        }
        return null;
    }

    public static <T> List<T> newExtensionList(Class<T> clazz) {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz);
        List<T> extensions = new ArrayList<T>();
        for (T service : serviceLoader) {
            extensions.add(service);
        }
        return extensions;
    }
}
