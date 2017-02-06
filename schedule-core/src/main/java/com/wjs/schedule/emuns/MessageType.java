package com.wjs.schedule.emuns;

public enum MessageType {
	
 
	
	REGIST("REGIST", "任务注册:ClientTaskInfoBean"),
	JOBDOING("JOBDOING", "任务执行:JobInfoBean"),
	JOBSUCCED("JOBSUCCED","任务执行成功:JobInfoBean"),
	JOBFAILED("JOBFAILED","任务执行失败:JobInfoBean"); 
	
	private final String value;
	
	private final String description;
	
	MessageType(String value, String description) {

		this.value = value;
		this.description = description;

	}
	

	public String getValue() {
		return value;
	}



	public String getDescription() {
		return description;
	}

	public static MessageType fromName(String input) {

		for (MessageType item : MessageType.values()) {
			if (item.name().equalsIgnoreCase(input))
				return item;
		}
		
		return null;
	}	

}

