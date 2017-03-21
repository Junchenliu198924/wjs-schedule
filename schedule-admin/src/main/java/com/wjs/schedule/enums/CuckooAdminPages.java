package com.wjs.schedule.enums;

public enum CuckooAdminPages {
	
 
	
	INDEX("/jobinfo/index", "首页"), 
	LOGIN("/login", "登录页"),
	ERROR("/common/common.exception", "报错页面"); 
	
	private final String value;
	
	private final String description;
	
	CuckooAdminPages(String value, String description) {

		this.value = value;
		this.description = description;

	}
	

	public String getValue() {
		return value;
	}



	public String getDescription() {
		return description;
	}

	public static CuckooAdminPages fromName(String input) {

		for (CuckooAdminPages item : CuckooAdminPages.values()) {
			if (item.name().equalsIgnoreCase(input))
				return item;
		}
		
		return null;
	}	

}

