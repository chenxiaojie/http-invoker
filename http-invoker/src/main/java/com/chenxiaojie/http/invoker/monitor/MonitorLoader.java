package com.chenxiaojie.http.invoker.monitor;

import com.chenxiaojie.http.invoker.utils.ExtensionLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chenxiaojie on 16/3/30.
 */
public class MonitorLoader {

    private static Monitor monitor = ExtensionLoader.getExtension(Monitor.class);
    private static final Logger logger = LoggerFactory.getLogger(MonitorLoader.class);

    static {
        if (monitor == null) {
            monitor = new SimpleMonitor();
        }
        logger.info("monitor:" + monitor);
        monitor.init();
    }

    public static Monitor getMonitor() {
        return monitor;
    }
}
