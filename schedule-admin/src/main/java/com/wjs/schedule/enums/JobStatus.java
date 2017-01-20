package com.wjs.schedule.enums;

/**
 * 任务状态
 * @author Silver
 *
 */
public enum JobStatus {
	
 
	PAUSE("PAUSE", "暂停"), 
	RUNNING("RUNNING", "启动"); 
	
	private final String value;
	
	private final String description;
	
	JobStatus(String value, String description) {

		this.value = value;
		this.description = description;

	}
	

	public String getValue() {
		return value;
	}



	public String getDescription() {
		return description;
	}

	public static JobStatus fromName(String input) {

		for (JobStatus item : JobStatus.values()) {
			if (item.name().equalsIgnoreCase(input))
				return item;
		}
		
		return null;
	}	

}

