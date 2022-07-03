package com.itheima.reggie.common;

/**
 * 自定义异常
 *
 * @author anfen
 * @date 2022/07/03
 */
public class CustomException extends RuntimeException{

    public CustomException(String message) {
        super(message);
    }
}
