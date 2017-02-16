package com.wjs.schedule.vo.qry;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class JobInfoQry {

	private Long jobGroupId;
	
	private String jobClassApplication ;
	private Long jobId ;
	private String jobStatus ;
	private String jobExecStatus ;
	
	public Long getJobGroupId() {
		return jobGroupId;
	}


	public void setJobGroupId(Long jobGroupId) {
		this.jobGroupId = jobGroupId;
	}
	

	public String getJobClassApplication() {
		return jobClassApplication;
	}


	public void setJobClassApplication(String jobClassApplication) {
		this.jobClassApplication = jobClassApplication;
	}


	public Long getJobId() {
		return jobId;
	}


	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}


	public String getJobStatus() {
		return jobStatus;
	}


	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}


	public String getJobExecStatus() {
		return jobExecStatus;
	}


	public void setJobExecStatus(String jobExecStatus) {
		this.jobExecStatus = jobExecStatus;
	}


	@Override
	public String toString() {

		return ReflectionToStringBuilder.toString(this);
	}
}
