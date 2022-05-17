package com.xhtech.tool.jenkins.util;

import hudson.model.TaskListener;

/**
 * 日志工具类
 */
public class LogUtil {
    public static TaskListener taskListener;

    public static void println(Object log) {
        if (null != taskListener) {
            taskListener.getLogger().println(log);
        } else {
            System.out.println(log);
        }
    }
}