package com.chenxiaojie.http.invoker.monitor;

/**
 * Created by chenxiaojie on 16/3/30.
 */
public class SimpleMonitor implements Monitor {

    @Override
    public void init() {

    }

    @Override
    public void logEvent(String type, String name, String status) {

    }

    @Override
    public void logError(String message, Throwable cause) {

    }

    @Override
    public MonitorTransaction newTransaction(String type, String name) {
        return new SimpleMonitorTransaction();
    }
}
