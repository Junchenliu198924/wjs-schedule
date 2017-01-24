package com.wjs.schedule.domain.exec;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class CuckooJobExecLogs implements Serializable {
    /**
     * 任务ID -- cockoo_job_exec_logs.job_id
     * 
     */
    private Long jobId;

    /**
     * 标准ID -- cockoo_job_exec_logs.id
     * 
     */
    private Long id;

    /**
     * 分组ID -- cockoo_job_exec_logs.group_id
     * 
     */
    private Long groupId;

    /**
     * 作业执行应用名 -- cockoo_job_exec_logs.job_class_application
     * 
     */
    private String jobClassApplication;

    /**
     * 作业执行远程类名称 -- cockoo_job_exec_logs.job_class_name
     * 
     */
    private String jobClassName;

    /**
     * 触发类型 -- cockoo_job_exec_logs.trigger_type
     * 
     */
    private String triggerType;

    /**
     * cron任务表达式 -- cockoo_job_exec_logs.cron_expression
     * 
     */
    private String cronExpression;

    /**
     * 任务执行业务日期 -- cockoo_job_exec_logs.tx_date
     * 
     */
    private Integer txDate;

    /**
     * 任务开始时间 -- cockoo_job_exec_logs.job_start_time
     * 
     */
    private Long jobStartTime;

    /**
     * 任务结束时间 -- cockoo_job_exec_logs.job_end_time
     * 
     */
    private Long jobEndTime;

    /**
     * 执行状态 -- cockoo_job_exec_logs.exec_job_status
     * 
     */
    private String execJobStatus;

    /**
     * 执行器IP -- cockoo_job_exec_logs.cuckoo_client_ip
     * 
     */
    private String cuckooClientIp;

    /**
     * 客户端标识 -- cockoo_job_exec_logs.cuckoo_client_tag
     * 
     */
    private String cuckooClientTag;

    /**
     * 最近检查时间 -- cockoo_job_exec_logs.latest_check_time
     * 
     */
    private Long latestCheckTime;

    /**
     * 断线已检查次数 -- cockoo_job_exec_logs.check_times
     * 
     */
    private Integer checkTimes;

    /**
     * 备注 -- cockoo_job_exec_logs.remark
     * 
     */
    private String remark;

    /**
     * cockoo_job_exec_logs表的操作属性:serialVersionUID
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 数据字段 cockoo_job_exec_logs.job_id的get方法 
     * 
     */
    public Long getJobId() {
        return jobId;
    }

    /**
     * 数据字段 cockoo_job_exec_logs.job_id的set方法
     * 
     */
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    /**
     * 数据字段 cockoo_job_exec_logs.id的get方法 
     * 
     */
    public Long getId() {
        return id;
    }

    /**
     * 数据字段 cockoo_job_exec_logs.id的set方法
     * 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 数据字段 cockoo_job_exec_logs.group_id的get方法 
     * 
     */
    public Long getGroupId() {
        return groupId;
    }

    /**
     * 数据字段 cockoo_job_exec_logs.group_id的set方法
     * 
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /**
     * 数据字段 cockoo_job_exec_logs.job_class_application的get方法 
     * 
     */
    public String getJobClassApplication() {
        return jobClassApplication;
    }

    /**
     * 数据字段 cockoo_job_exec_logs.job_class_application的set方法
     * 
     */
    public void setJobClassApplication(String jobClassApplication) {
        this.jobClassApplication = jobClassApplication == null ? null : jobClassApplication.trim();
    }

    /**
     * 数据字段 cockoo_job_exec_logs.job_class_name的get方法 
     * 
     */
    public String getJobClassName() {
        return jobClassName;
    }

    /**
     * 数据字段 cockoo_job_exec_logs.job_class_name的set方法
     * 
     */
    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName == null ? null : jobClassName.trim();
    }

    /**
     * 数据字段 cockoo_job_exec_logs.trigger_type的get方法 
     * 
     */
    public String getTriggerType() {
        return triggerType;
    }

    /**
     * 数据字段 cockoo_job_exec_logs.trigger_type的set方法
     * 
     */
    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType == null ? null : triggerType.trim();
    }

    /**
     * 数据字段 cockoo_job_exec_logs.cron_expression的get方法 
     * 
     */
    public String getCronExpression() {
        return cronExpression;
    }

    /**
     * 数据字段 cockoo_job_exec_logs.cron_expression的set方法
     * 
     */
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression == null ? null : cronExpression.trim();
    }

    /**
     * 数据字段 cockoo_job_exec_logs.tx_date的get方法 
     * 
     */
    public Integer getTxDate() {
        return txDate;
    }

    /**
     * 数据字段 cockoo_job_exec_logs.tx_date的set方法
     * 
     */
    public void setTxDate(Integer txDate) {
        this.txDate = txDate;
    }

    /**
     * 数据字段 cockoo_job_exec_logs.job_start_time的get方法 
     * 
     */
    public Long getJobStartTime() {
        return jobStartTime;
    }

    /**
     * 数据字段 cockoo_job_exec_logs.job_start_time的set方法
     * 
     */
    public void setJobStartTime(Long jobStartTime) {
        this.jobStartTime = jobStartTime;
    }

    /**
     * 数据字段 cockoo_job_exec_logs.job_end_time的get方法 
     * 
     */
    public Long getJobEndTime() {
        return jobEndTime;
    }

    /**
     * 数据字段 cockoo_job_exec_logs.job_end_time的set方法
     * 
     */
    public void setJobEndTime(Long jobEndTime) {
        this.jobEndTime = jobEndTime;
    }

    /**
     * 数据字段 cockoo_job_exec_logs.exec_job_status的get方法 
     * 
     */
    public String getExecJobStatus() {
        return execJobStatus;
    }

    /**
     * 数据字段 cockoo_job_exec_logs.exec_job_status的set方法
     * 
     */
    public void setExecJobStatus(String execJobStatus) {
        this.execJobStatus = execJobStatus == null ? null : execJobStatus.trim();
    }

    /**
     * 数据字段 cockoo_job_exec_logs.cuckoo_client_ip的get方法 
     * 
     */
    public String getCuckooClientIp() {
        return cuckooClientIp;
    }

    /**
     * 数据字段 cockoo_job_exec_logs.cuckoo_client_ip的set方法
     * 
     */
    public void setCuckooClientIp(String cuckooClientIp) {
        this.cuckooClientIp = cuckooClientIp == null ? null : cuckooClientIp.trim();
    }

    /**
     * 数据字段 cockoo_job_exec_logs.cuckoo_client_tag的get方法 
     * 
     */
    public String getCuckooClientTag() {
        return cuckooClientTag;
    }

    /**
     * 数据字段 cockoo_job_exec_logs.cuckoo_client_tag的set方法
     * 
     */
    public void setCuckooClientTag(String cuckooClientTag) {
        this.cuckooClientTag = cuckooClientTag == null ? null : cuckooClientTag.trim();
    }

    /**
     * 数据字段 cockoo_job_exec_logs.latest_check_time的get方法 
     * 
     */
    public Long getLatestCheckTime() {
        return latestCheckTime;
    }

    /**
     * 数据字段 cockoo_job_exec_logs.latest_check_time的set方法
     * 
     */
    public void setLatestCheckTime(Long latestCheckTime) {
        this.latestCheckTime = latestCheckTime;
    }

    /**
     * 数据字段 cockoo_job_exec_logs.check_times的get方法 
     * 
     */
    public Integer getCheckTimes() {
        return checkTimes;
    }

    /**
     * 数据字段 cockoo_job_exec_logs.check_times的set方法
     * 
     */
    public void setCheckTimes(Integer checkTimes) {
        this.checkTimes = checkTimes;
    }

    /**
     * 数据字段 cockoo_job_exec_logs.remark的get方法 
     * 
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 数据字段 cockoo_job_exec_logs.remark的set方法
     * 
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}