package com.itheima.reggie.utils;

import java.util.UUID;

public class StringUtils {

    public static String getRandomImageName(String fileName) {
        int index = fileName.lastIndexOf(".");

        if (fileName.isEmpty() || index == -1) {
            throw new IllegalArgumentException();
        }

        //获取文件后缀
        String suffix = fileName.substring(index).toLowerCase();
        //生成上传至云服务器的路径
        return UUID.randomUUID().toString().replaceAll("-", "") + suffix;
    }
}
