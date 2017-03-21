package com.wjs.schedule.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wjs.schedule.bean.JobInfoBean;
import com.wjs.schedule.exception.BaseException;
import com.wjs.schedule.executor.annotation.CuckooTask;
import com.wjs.schedule.service.CuckooTestDailyJob;

@Service
public class CuckooTestDailyJobImpl implements CuckooTestDailyJob {


	private static final Logger LOGGER = LoggerFactory.getLogger(CuckooTestDailyJobImpl.class);

	@Override
	@CuckooTask("testCronDailySucced")
	public void testCronDailySucced(JobInfoBean jobInfo) {
		// 测试完成

		LOGGER.info("Client exec done ,testCronDailySucced:{}",  jobInfo);
	}

	@Override
	@CuckooTask("testCronDailyFailed")
	public void testCronDailyFailed(JobInfoBean jobInfo) {
		// 测试完成

		LOGGER.info("Client exec done ,testCronDailySucced:{}",  jobInfo);
		throw new BaseException("client throw a exception ");
	}

	@Override
	@CuckooTask("testCronDailyDependencySucced")
	public void testCronDailyDependencySucced(JobInfoBean jobInfo) {

		LOGGER.info("Client exec done ,testCronDailyDependencySucced:{}",  jobInfo);
	}

	@Override
	@CuckooTask("testFlowDailySucced")
	public void testFlowDailySucced(JobInfoBean jobInfo) {

		LOGGER.info("Client exec done ,testFlowDailySucced:{}",  jobInfo);
		
	}

	@Override
	@CuckooTask("testFlowDailyFailed")
	public void testFlowDailyFailed(JobInfoBean jobInfo) {
		
		LOGGER.info("Client exec done ,testFlowDailyFailed:{}",  jobInfo);
		throw new BaseException();
		
	}

	@Override
	@CuckooTask("testFlowDailyDependencySucced")
	public void testFlowDailyDependencySucced(JobInfoBean jobInfo) {

		LOGGER.info("Client exec done ,testFlowDailyDependencySucced:{}",  jobInfo);
		
	}

}
