package com.wjs.schedule.executor.framerwork;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class CuckooClient implements ApplicationContextAware {

	private  ApplicationContext applicationContext; // Spring应用上下文环境
	
	private String configLocation;
	
	/**
	 * appname
	 */
	/**
	 * clientTag
	 */
	
	
	

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

		this.applicationContext = applicationContext;
	}

	public String getConfigLocation() {
		return configLocation;
	}

	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
	}
	/*
	 * 1.扫描task注解，并且task.value不能为空，不能重复，否则报错
	 */
	
	/*
	 * 2.在task注解上面，动态增加aspectj
	 * AspectJProxyFactory factory = new AspectJProxyFactory(targetService);  
	 * factory.addAspect(DeclareParentsSeller.class);  
	 * targetService = factory.getProxy();  
	 */
	
	/*
	 * 3.相关的类和执行方法放到缓存中
	 */
	
	/*
	 * 4.相关的执行
	 */

}
