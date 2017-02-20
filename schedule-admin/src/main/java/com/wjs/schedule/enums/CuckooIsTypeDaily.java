package com.wjs.schedule.enums;

import java.util.Arrays;

/**
 * 是否为日切任务
 * @author Silver
 *
 */
public enum CuckooIsTypeDaily {
	

	NULL("", "全部/无"), 
	NO("NO", "非日切任务"),
	YES("YES", "是日切任务"); 
	
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
	
	public static CuckooIsTypeDaily[] valuesNoNull() {

		CuckooIsTypeDaily[] result = CuckooIsTypeDaily.values();
		for (int i = 0; i < result.length; i++) {
			if(CuckooIsTypeDaily.NULL.equals(result[i])){
				// 提出null元素 --用最后一个元素替换，然后删除最后一个元素
				result[i]= result[result.length-1];
				//数组缩容
				result = Arrays.copyOf(result, result.length-1);
			}
		}
		return result;
	}	

}

