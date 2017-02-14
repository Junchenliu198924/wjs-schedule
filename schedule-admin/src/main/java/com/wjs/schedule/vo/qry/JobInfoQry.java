package com.wjs.schedule.vo.qry;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class JobInfoQry {

	private Long jobGroupId;
	
	public Long getJobGroupId() {
		return jobGroupId;
	}


	public void setJobGroupId(Long jobGroupId) {
		this.jobGroupId = jobGroupId;
	}


	@Override
	public String toString() {

		return ReflectionToStringBuilder.toString(this);
	}
}
