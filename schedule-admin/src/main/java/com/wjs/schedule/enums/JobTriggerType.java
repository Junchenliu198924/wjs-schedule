package com.wjs.schedule.enums;

public enum JobTriggerType {
	
 
	
	CRON("CRON", "普通Cron任务"), 
	DAILY("DAILY", "日切Cron任务"),
	FLOW("FLOW", "流式调度任务"); 
	
	private final String value;
	
	private final String description;
	
	JobTriggerType(String value, String description) {

		this.value = value;
		this.description = description;

	}
	

	public String getValue() {
		return value;
	}



	public String getDescription() {
		return description;
	}

	public static JobTriggerType fromName(String input) {

		for (JobTriggerType item : JobTriggerType.values()) {
			if (item.name().equalsIgnoreCase(input))
				return item;
		}
		
		return null;
	}	

}

