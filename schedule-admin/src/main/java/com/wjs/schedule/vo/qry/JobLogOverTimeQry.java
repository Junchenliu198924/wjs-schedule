package com.wjs.schedule.vo.qry;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class JobLogOverTimeQry {

	private Long overTime;
	private Integer start;
	private Integer limit;
	public Long getOverTime() {
		return overTime;
	}
	public void setOverTime(Long overTime) {
		this.overTime = overTime;
	}
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
	
	@Override
	public String toString() {
		
		return ReflectionToStringBuilder.toString(this);
	}
	
}
