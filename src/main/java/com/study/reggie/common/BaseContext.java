package com.study.reggie.common;

public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }
public static long getCurrentId() {
        return threadLocal.get();
}
    }

