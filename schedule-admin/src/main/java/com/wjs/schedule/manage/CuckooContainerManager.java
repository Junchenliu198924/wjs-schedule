package com.wjs.schedule.manage;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.wjs.schedule.component.cuckoo.CuckooJobCallBack;
import com.wjs.schedule.component.quartz.QuartzManage;
import com.wjs.schedule.dao.exec.CuckooJobDetailMapper;
import com.wjs.schedule.domain.exec.CuckooJobDetail;
import com.wjs.schedule.domain.exec.CuckooJobDetailCriteria;
import com.wjs.schedule.enums.CuckooJobTriggerType;
import com.wjs.schedule.service.Job.CuckooJobService;

public class CuckooContainerManager implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	QuartzManage quartzManage;

	private static final Logger LOGGER = LoggerFactory.getLogger(CuckooJobCallBack.class);
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (event.getApplicationContext().getParent() == null) {
			// donothing 
			LOGGER.info("项目启动完成");
			quartzManage.addAutoJob();
		}
	}

	 
}
