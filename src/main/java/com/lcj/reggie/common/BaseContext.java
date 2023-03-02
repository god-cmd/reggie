package com.lcj.reggie.common;/*
    @author lcj
    @create -
*/

import org.springframework.stereotype.Component;

public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static Long getCurrentId(){
        return threadLocal.get();
    }

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
}
