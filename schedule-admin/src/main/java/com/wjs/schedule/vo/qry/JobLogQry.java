package com.wjs.schedule.vo.qry;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class JobLogQry {

	private Long groupId;
	private Long jobId;
	
	private List<String> jobStatus;

	//	2017-03-20 00:00:00 - 2017-03-21 00:00:00
	private String filterTime;
	
	private Integer start;
	private Integer limit;
	
	private Long startDateTime;
	private Long endDateTime;
	
	
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public Long getJobId() {
		return jobId;
	}
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
	
	
	public List<String> getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(List<String> jobStatus) {
		this.jobStatus = jobStatus;
	}
	public String getFilterTime() {
		return filterTime;
	}
	public void setFilterTime(String filterTime) {
		this.filterTime = filterTime;
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
	public Long getStartDateTime() {
		return startDateTime;
	}
	public void setStartDateTime(Long startDateTime) {
		this.startDateTime = startDateTime;
	}
	public Long getEndDateTime() {
		return endDateTime;
	}
	public void setEndDateTime(Long endDateTime) {
		this.endDateTime = endDateTime;
	}
	
	
	@Override
	public String toString() {
		
		return ReflectionToStringBuilder.toString(this);
	}
}
