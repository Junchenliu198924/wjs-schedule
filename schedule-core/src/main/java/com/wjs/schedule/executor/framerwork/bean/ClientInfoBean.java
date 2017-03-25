package com.wjs.schedule.executor.framerwork.bean;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 客户端信息，用于存储clientTag
 * @author Silver
 *
 */
public class ClientInfoBean {


	private static String appName;
	
	private ClientInfoBean(){
		
	}


	public static String getAppName() {
		return appName;
	}

	public static void setAppName(String appName) {
		ClientInfoBean.appName = appName;
	}

	@Override
	public String toString() {
		
		return ReflectionToStringBuilder.toString(this);
	}

}
