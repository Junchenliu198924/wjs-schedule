package com.wjs.schedule.vo.qry;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class JobClientQry {
	
	private Integer start;
	
	private Integer limit;
	
	private String jobClassApplication;
	
	private String jobName;
	
	

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getJobClassApplication() {
		return jobClassApplication;
	}

	public void setJobClassApplication(String jobClassApplication) {
		this.jobClassApplication = jobClassApplication;
	}
	
	
	
	
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	@Override
	public String toString() {
		
		return ReflectionToStringBuilder.toString(this);
	}
}
