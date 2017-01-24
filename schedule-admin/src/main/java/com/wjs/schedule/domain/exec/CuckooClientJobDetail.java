package com.wjs.schedule.domain.exec;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class CuckooClientJobDetail implements Serializable {
    /**
     * 标准ID -- cuckoo_client_job_detail.id
     * 
     */
    private Long id;

    /**
     * 作业执行应用名 -- cuckoo_client_job_detail.job_class_application
     * 
     */
    private String jobClassApplication;

    /**
     * ip地址 -- cuckoo_client_job_detail.ip
     * 
     */
    private String ip;

    /**
     * 任务ID -- cuckoo_client_job_detail.job_id
     * 
     */
    private Long jobId;

    /**
     * 客户端标识 -- cuckoo_client_job_detail.cuckoo_client_tag
     * 
     */
    private String cuckooClientTag;

    /**
     * 客户端状态 -- cuckoo_client_job_detail.cuckoo_client_status
     * 
     */
    private String cuckooClientStatus;

    /**
     * cuckoo_client_job_detail表的操作属性:serialVersionUID
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 数据字段 cuckoo_client_job_detail.id的get方法 
     * 
     */
    public Long getId() {
        return id;
    }

    /**
     * 数据字段 cuckoo_client_job_detail.id的set方法
     * 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 数据字段 cuckoo_client_job_detail.job_class_application的get方法 
     * 
     */
    public String getJobClassApplication() {
        return jobClassApplication;
    }

    /**
     * 数据字段 cuckoo_client_job_detail.job_class_application的set方法
     * 
     */
    public void setJobClassApplication(String jobClassApplication) {
        this.jobClassApplication = jobClassApplication == null ? null : jobClassApplication.trim();
    }

    /**
     * 数据字段 cuckoo_client_job_detail.ip的get方法 
     * 
     */
    public String getIp() {
        return ip;
    }

    /**
     * 数据字段 cuckoo_client_job_detail.ip的set方法
     * 
     */
    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    /**
     * 数据字段 cuckoo_client_job_detail.job_id的get方法 
     * 
     */
    public Long getJobId() {
        return jobId;
    }

    /**
     * 数据字段 cuckoo_client_job_detail.job_id的set方法
     * 
     */
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    /**
     * 数据字段 cuckoo_client_job_detail.cuckoo_client_tag的get方法 
     * 
     */
    public String getCuckooClientTag() {
        return cuckooClientTag;
    }

    /**
     * 数据字段 cuckoo_client_job_detail.cuckoo_client_tag的set方法
     * 
     */
    public void setCuckooClientTag(String cuckooClientTag) {
        this.cuckooClientTag = cuckooClientTag == null ? null : cuckooClientTag.trim();
    }

    /**
     * 数据字段 cuckoo_client_job_detail.cuckoo_client_status的get方法 
     * 
     */
    public String getCuckooClientStatus() {
        return cuckooClientStatus;
    }

    /**
     * 数据字段 cuckoo_client_job_detail.cuckoo_client_status的set方法
     * 
     */
    public void setCuckooClientStatus(String cuckooClientStatus) {
        this.cuckooClientStatus = cuckooClientStatus == null ? null : cuckooClientStatus.trim();
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}