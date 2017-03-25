package com.wjs.schedule.vo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class QryBase implements Serializable {

	private Integer start;
	private Integer limit;
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
