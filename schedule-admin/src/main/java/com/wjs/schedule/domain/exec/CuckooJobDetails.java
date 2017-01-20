package com.wjs.schedule.domain.exec;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class CuckooJobDetails implements Serializable {
    /**
     * 标准ID -- cockoo_job_details.id
     * 
     */
    private Long id;

    /**
     * 分组ID -- cockoo_job_details.group_id
     * 
     */
    private Long groupId;

    /**
     * 任务名称 -- cockoo_job_details.job_name
     * 
     */
    private String jobName;

    /**
     * 作业执行应用名 -- cockoo_job_details.job_class_application
     * 
     */
    private String jobClassApplication;

    /**
     * 作业执行远程类名称 -- cockoo_job_details.job_class_name
     * 
     */
    private String jobClassName;

    /**
     * 任务描述 -- cockoo_job_details.job_desc
     * 
     */
    private String jobDesc;

    /**
     * 触发类型 -- cockoo_job_details.trigger_type
     * 
     */
    private String triggerType;

    /**
     * 偏移量 -- cockoo_job_details.offset
     * 
     */
    private Integer offset;

    /**
     * 任务状态 -- cockoo_job_details.job_status
     * 
     */
    private String jobStatus;

    /**
     * 执行状态 -- cockoo_job_details.exec_job_status
     * 
     */
    private String execJobStatus;

    /**
     * cockoo_job_details表的操作属性:serialVersionUID
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 数据字段 cockoo_job_details.id的get方法 
     * 
     */
    public Long getId() {
        return id;
    }

    /**
     * 数据字段 cockoo_job_details.id的set方法
     * 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 数据字段 cockoo_job_details.group_id的get方法 
     * 
     */
    public Long getGroupId() {
        return groupId;
    }

    /**
     * 数据字段 cockoo_job_details.group_id的set方法
     * 
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /**
     * 数据字段 cockoo_job_details.job_name的get方法 
     * 
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * 数据字段 cockoo_job_details.job_name的set方法
     * 
     */
    public void setJobName(String jobName) {
        this.jobName = jobName == null ? null : jobName.trim();
    }

    /**
     * 数据字段 cockoo_job_details.job_class_application的get方法 
     * 
     */
    public String getJobClassApplication() {
        return jobClassApplication;
    }

    /**
     * 数据字段 cockoo_job_details.job_class_application的set方法
     * 
     */
    public void setJobClassApplication(String jobClassApplication) {
        this.jobClassApplication = jobClassApplication == null ? null : jobClassApplication.trim();
    }

    /**
     * 数据字段 cockoo_job_details.job_class_name的get方法 
     * 
     */
    public String getJobClassName() {
        return jobClassName;
    }

    /**
     * 数据字段 cockoo_job_details.job_class_name的set方法
     * 
     */
    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName == null ? null : jobClassName.trim();
    }

    /**
     * 数据字段 cockoo_job_details.job_desc的get方法 
     * 
     */
    public String getJobDesc() {
        return jobDesc;
    }

    /**
     * 数据字段 cockoo_job_details.job_desc的set方法
     * 
     */
    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc == null ? null : jobDesc.trim();
    }

    /**
     * 数据字段 cockoo_job_details.trigger_type的get方法 
     * 
     */
    public String getTriggerType() {
        return triggerType;
    }

    /**
     * 数据字段 cockoo_job_details.trigger_type的set方法
     * 
     */
    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType == null ? null : triggerType.trim();
    }

    /**
     * 数据字段 cockoo_job_details.offset的get方法 
     * 
     */
    public Integer getOffset() {
        return offset;
    }

    /**
     * 数据字段 cockoo_job_details.offset的set方法
     * 
     */
    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    /**
     * 数据字段 cockoo_job_details.job_status的get方法 
     * 
     */
    public String getJobStatus() {
        return jobStatus;
    }

    /**
     * 数据字段 cockoo_job_details.job_status的set方法
     * 
     */
    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus == null ? null : jobStatus.trim();
    }

    /**
     * 数据字段 cockoo_job_details.exec_job_status的get方法 
     * 
     */
    public String getExecJobStatus() {
        return execJobStatus;
    }

    /**
     * 数据字段 cockoo_job_details.exec_job_status的set方法
     * 
     */
    public void setExecJobStatus(String execJobStatus) {
        this.execJobStatus = execJobStatus == null ? null : execJobStatus.trim();
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}