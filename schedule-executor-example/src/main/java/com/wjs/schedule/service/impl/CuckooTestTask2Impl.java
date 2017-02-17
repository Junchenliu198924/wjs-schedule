package com.wjs.schedule.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wjs.schedule.bean.JobInfoBean;
import com.wjs.schedule.executor.annotation.CuckooTask;
import com.wjs.schedule.net.client.handle.CuckooClientHandler;
import com.wjs.schedule.service.CuckooTestTask2;

@Service
public class CuckooTestTask2Impl implements CuckooTestTask2 {


	private static final Logger LOGGER = LoggerFactory.getLogger(CuckooTestTask2Impl.class);
	@Override
	@CuckooTask("testJob2")
	public void testJob(JobInfoBean jobInfo) {

		
		LOGGER.info("Client exec done :testJob2" );
	}

}
