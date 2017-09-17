package com.chenxiaojie.http.invoker.monitor;

/**
 * Created by chenxiaojie on 16/3/30.
 */
public interface MonitorTransaction {

    void success();

    void failure(Throwable cause);

    void complete();
}
