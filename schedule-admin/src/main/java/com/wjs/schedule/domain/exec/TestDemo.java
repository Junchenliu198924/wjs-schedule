package com.wjs.schedule.domain.exec;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class TestDemo implements Serializable {
    /**
     * 标准ID -- test_demo.id
     * 
     */
    private Long id;

    /**
     * http请求uri -- test_demo.uri
     * 
     */
    private String uri;

    /**
     * 最大请求数 -- test_demo.access_count_max
     * 
     */
    private Integer accessCountMax;

    /**
     * test_demo表的操作属性:serialVersionUID
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 数据字段 test_demo.id的get方法 
     * 
     */
    public Long getId() {
        return id;
    }

    /**
     * 数据字段 test_demo.id的set方法
     * 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 数据字段 test_demo.uri的get方法 
     * 
     */
    public String getUri() {
        return uri;
    }

    /**
     * 数据字段 test_demo.uri的set方法
     * 
     */
    public void setUri(String uri) {
        this.uri = uri == null ? null : uri.trim();
    }

    /**
     * 数据字段 test_demo.access_count_max的get方法 
     * 
     */
    public Integer getAccessCountMax() {
        return accessCountMax;
    }

    /**
     * 数据字段 test_demo.access_count_max的set方法
     * 
     */
    public void setAccessCountMax(Integer accessCountMax) {
        this.accessCountMax = accessCountMax;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}