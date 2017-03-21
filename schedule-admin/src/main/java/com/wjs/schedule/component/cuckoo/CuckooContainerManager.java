package com.wjs.schedule.component.cuckoo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class CuckooContainerManager implements ApplicationListener<ContextRefreshedEvent> {


	private static final Logger LOGGER = LoggerFactory.getLogger(CuckooJobCallBack.class);
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (event.getApplicationContext().getParent() == null) {
			// donothing 
			LOGGER.info("项目启动完成");
		}
	}

	 
}
