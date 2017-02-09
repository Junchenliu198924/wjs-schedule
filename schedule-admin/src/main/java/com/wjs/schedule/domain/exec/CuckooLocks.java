package com.wjs.schedule.domain.exec;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class CuckooLocks implements Serializable {
    /**
     * 标准ID -- cuckoo_locks.id
     * 
     */
    private Long id;

    /**
     * 锁名称 -- cuckoo_locks.lock_name
     * 
     */
    private String lockName;

    /**
     * 服务器IP -- cuckoo_locks.cuckoo_server_ip
     * 
     */
    private String cuckooServerIp;

    /**
     * cuckoo_locks表的操作属性:serialVersionUID
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 数据字段 cuckoo_locks.id的get方法 
     * 
     */
    public Long getId() {
        return id;
    }

    /**
     * 数据字段 cuckoo_locks.id的set方法
     * 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 数据字段 cuckoo_locks.lock_name的get方法 
     * 
     */
    public String getLockName() {
        return lockName;
    }

    /**
     * 数据字段 cuckoo_locks.lock_name的set方法
     * 
     */
    public void setLockName(String lockName) {
        this.lockName = lockName == null ? null : lockName.trim();
    }

    /**
     * 数据字段 cuckoo_locks.cuckoo_server_ip的get方法 
     * 
     */
    public String getCuckooServerIp() {
        return cuckooServerIp;
    }

    /**
     * 数据字段 cuckoo_locks.cuckoo_server_ip的set方法
     * 
     */
    public void setCuckooServerIp(String cuckooServerIp) {
        this.cuckooServerIp = cuckooServerIp == null ? null : cuckooServerIp.trim();
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}