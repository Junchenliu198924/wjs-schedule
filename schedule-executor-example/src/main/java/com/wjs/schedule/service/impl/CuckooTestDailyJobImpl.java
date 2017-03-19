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
	public void testCronDailyDependencySucced(JobInfoBean jobInfo) {
		// TODO 待测试
		
	}

	@Override
	public void testFlowDailySucced(JobInfoBean jobInfo) {
		// TODO 待测试
		
	}

	@Override
	public void testFlowDailyFailed(JobInfoBean jobInfo) {
		// TODO 待测试
		
	}

	@Override
	public void testFlowDailyDependencySucced(JobInfoBean jobInfo) {
		// TODO 待测试
		
	}

}
