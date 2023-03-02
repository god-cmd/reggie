package com.lcj.reggie.common;/*
    @author lcj
    @create -
*/

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/*
*  通用对象
* */
@Data
public class R<T> {
    private Integer code;
    private String msg;
    private T data;
    private Map map = new HashMap();

    public static <T> R success(T data){
        R<T> r = new R<>();
        r.code = 1;
        r.data = data;
        return r;
    }

    public static <T> R error(String msg){
        R<T> r = new R<>();
        r.code = 0;
        r.msg = msg;
        return r;
    }

    public R<T> add(String key,Object value){
        map.put(key,value);
        return this;
    }
}
