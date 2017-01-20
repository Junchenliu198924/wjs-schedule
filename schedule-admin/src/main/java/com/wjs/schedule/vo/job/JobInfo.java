package com.wjs.schedule.vo.job;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 任务信息
 * @author Silver
 *
 */
public class JobInfo {
	
	/**
	 * ID
	 */
	private Long id;
	/**
	 * 分组ID
	 */
	private Long groupId;
	/**
	 * 任务名称
	 */
	private String jobName;
	/**
	 * 任务执行应用名称
	 */
	private String jobClassApplication;
	/**
	 * 任务执行方法接口名称
	 */
	private String jobClassName;
	
	/**
	 * 触发类型 CRON/FLOW
	 */
	private String triggerType;
	/**
	 * 偏移天数
	 */
	private Integer offSet = 0;
	/**
	 * 任务状态（默认暂停）
	 */
	private String jobStatus;
	
	/**
	 * Cron类型任务表达式
	 */
	private String cronExpression;
	/**
	 * 任务描述 
	 */
	private String jobDesc;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getJobClassApplication() {
		return jobClassApplication;
	}
	public void setJobClassApplication(String jobClassApplication) {
		this.jobClassApplication = jobClassApplication;
	}
	public String getJobClassName() {
		return jobClassName;
	}
	public void setJobClassName(String jobClassName) {
		this.jobClassName = jobClassName;
	}
	public String getTriggerType() {
		return triggerType;
	}
	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}
	public Integer getOffSet() {
		return offSet;
	}
	public void setOffSet(Integer offSet) {
		this.offSet = offSet;
	}
	public String getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public String getJobDesc() {
		return jobDesc;
	}
	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}
	
	
	@Override
	public String toString() {
		
		return ReflectionToStringBuilder.toString(this);
	}
	
}
