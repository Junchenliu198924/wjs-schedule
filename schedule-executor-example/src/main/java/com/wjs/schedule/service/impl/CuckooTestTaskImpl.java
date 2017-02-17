package com.wjs.schedule.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wjs.schedule.bean.JobInfoBean;
import com.wjs.schedule.executor.annotation.CuckooTask;
import com.wjs.schedule.service.CuckooTestTask;

@Service
public class CuckooTestTaskImpl implements CuckooTestTask {


	private static final Logger LOGGER = LoggerFactory.getLogger(CuckooTestTaskImpl.class);
	@Override
	@CuckooTask("testJob")
	public void testJob(JobInfoBean jobInfo) {
		// TODO Auto-generated method stub

		LOGGER.info("Client exec done :testJob" );
	}

	@Override
	public void testJobTmp(JobInfoBean jobInfo) {
		// TODO Auto-generated method stub

		LOGGER.info("Client exec done :none" );
	}

}
