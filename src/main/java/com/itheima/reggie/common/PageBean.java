package com.itheima.reggie.common;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class PageBean<T> {

    /**
     * 记录
     */
    protected List<T> records = Collections.emptyList();

    /**
     * 总数
     */
    protected long total;
}
