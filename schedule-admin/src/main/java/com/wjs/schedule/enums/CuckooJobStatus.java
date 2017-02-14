package com.wjs.schedule.enums;

/**
 * 任务状态
 * @author Silver
 *
 */
public enum CuckooJobStatus {
	

	NULL("", "全部/无"),
	PAUSE("PAUSE", "暂停"), 
	RUNNING("RUNNING", "启动"); 
	
	private final String value;
	
	private final String description;
	
	CuckooJobStatus(String value, String description) {

		this.value = value;
		this.description = description;

	}
	

	public String getValue() {
		return value;
	}



	public String getDescription() {
		return description;
	}

	public static CuckooJobStatus fromName(String input) {

		for (CuckooJobStatus item : CuckooJobStatus.values()) {
			if (item.name().equalsIgnoreCase(input))
				return item;
		}
		
		return null;
	}	

}

