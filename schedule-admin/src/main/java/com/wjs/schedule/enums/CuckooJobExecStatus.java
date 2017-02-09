package com.wjs.schedule.enums;

/**
 * 任务执行状态
 * @author Silver
 *
 */
public enum CuckooJobExecStatus {


	PENDING("PENDING", "等待执行中"), 
	RUNNING("RUNNING", "正在执行中"), 
	FAILED("FAILED", "执行失败"),
	SUCCED("SUCCED", "执行成功"),
	BREAK("BREAK", "断线"); 
	
	private final String value;
	
	private final String description;
	
	CuckooJobExecStatus(String value, String description) {

		this.value = value;
		this.description = description;

	}
	

	public String getValue() {
		return value;
	}



	public String getDescription() {
		return description;
	}

	public static CuckooJobExecStatus fromName(String input) {

		for (CuckooJobExecStatus item : CuckooJobExecStatus.values()) {
			if (item.name().equalsIgnoreCase(input))
				return item;
		}
		
		return null;
	}	

}

