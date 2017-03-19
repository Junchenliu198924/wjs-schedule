package com.wjs.schedule.vo.job;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.wjs.schedule.domain.exec.CuckooClientJobDetail;

public class CuckooClientJobExecResult {
	
	/**
	 * true 成功，FALSE失败
	 */
	private boolean success = false;
	
	/**
	 * 失败原因
	 */
	private String remark;
	
	private CuckooClientJobDetail clientJobInfo;

	

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark.length() > 490 ? remark.substring(0, 490) : remark;
	}

	public CuckooClientJobDetail getClientJobInfo() {
		return clientJobInfo;
	}

	public void setClientJobInfo(CuckooClientJobDetail clientJobInfo) {
		this.clientJobInfo = clientJobInfo;
	}
	
	
	
	@Override
	public String toString() {
		
		return ReflectionToStringBuilder.toString(this);
	}
}
