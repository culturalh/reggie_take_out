package com.itheima.reggie.common;

/**
 * 获取当前session值的封装类
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void set(Long id){
        threadLocal.set(id);
    }

    public static Long get(){
        return threadLocal.get();
    }

}
