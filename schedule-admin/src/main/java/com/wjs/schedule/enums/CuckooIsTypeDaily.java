package com.wjs.schedule.enums;

/**
 * 是否为日切任务
 * @author Silver
 *
 */
public enum CuckooIsTypeDaily {
	

	YES("YES", "是日切任务"), 
	NO("NO", "不是日切任务"); 
	
	private final String value;
	
	private final String description;
	
	CuckooIsTypeDaily(String value, String description) {

		this.value = value;
		this.description = description;

	}
	

	public String getValue() {
		return value;
	}



	public String getDescription() {
		return description;
	}

	public static CuckooIsTypeDaily fromName(String input) {

		for (CuckooIsTypeDaily item : CuckooIsTypeDaily.values()) {
			if (item.name().equalsIgnoreCase(input))
				return item;
		}
		
		return null;
	}	

}

