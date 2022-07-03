package com.itheima.reggie.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置通用返回结果，服务器端响应的数据都会封装为此对象
 *
 * @author anfen
 * @date 2022/07/02
 */
@Data
public class R<T> {
    private Integer code; // 响应码
    private String msg; // 错误信息
    private T data; // 返回数据
    private Map<String, Object> map = new HashMap<>(); // 动态数据

    public static <T> R<T> success(T object) {
        R<T> r = new R<>();
        r.code = 1;
        r.data = object;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R<T> r = new R<>();
        r.code = 0;
        r.msg = msg;
        return r;
    }

    public R<T> add(String key,Object value) {
        this.map.put(key, value);
        return this;
    }
}
