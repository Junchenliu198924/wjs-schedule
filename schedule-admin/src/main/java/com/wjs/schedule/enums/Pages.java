package com.wjs.schedule.enums;

public enum Pages {
	
 
	
	INDEX("/index", "首页"), 
	LOGIN("/login", "登录页"),
	ERROR("/error", "报错页面"); 
	
	private final String value;
	
	private final String description;
	
	Pages(String value, String description) {

		this.value = value;
		this.description = description;

	}
	

	public String getValue() {
		return value;
	}



	public String getDescription() {
		return description;
	}

	public static Pages fromName(String input) {

		for (Pages item : Pages.values()) {
			if (item.name().equalsIgnoreCase(input))
				return item;
		}
		
		return null;
	}	

}

