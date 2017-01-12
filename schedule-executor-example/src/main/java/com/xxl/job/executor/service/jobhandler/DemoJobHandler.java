package com.xxl.job.executor.service.jobhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * 任务Handler的一个Demo（Bean模式）
 * 
 * 开发步骤：
 * 1、继承 “IJobHandler” ；
 * 2、装配到Spring，例如加 “@Service” 注解；
 * 3、加 “@JobHander” 注解，注解value值为新增任务生成的JobKey的值;多个JobKey用逗号分割;
 * 
 * @author xuxueli 2015-12-19 19:43:36
 */
@Service
public class DemoJobHandler{
	private static transient Logger logger = LoggerFactory.getLogger(DemoJobHandler.class);
	
	public void execute(String... params) throws Exception {
		logger.info("XXL-JOB, Hello World.");
		
	}
	
}
