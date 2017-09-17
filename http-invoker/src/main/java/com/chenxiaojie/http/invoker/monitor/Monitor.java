package com.chenxiaojie.http.invoker.monitor;

/**
 * Created by chenxiaojie on 16/3/30.
 */
public interface Monitor {

    void init();

    void logEvent(String type, String name, String status);

    void logError(String message, Throwable cause);

    MonitorTransaction newTransaction(String type, String name);
}