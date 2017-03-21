package com.wjs.schedule.vo.qry;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class JobClientQry {
	
	private Integer start;
	
	private Integer limit;
	
	private String jobClassApplication;
	
	

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
	
	
	@Override
	public String toString() {
		
		return ReflectionToStringBuilder.toString(this);
	}
}
