package com.wjs.schedule.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wjs.schedule.bean.JobInfoBean;
import com.wjs.schedule.executor.annotation.CuckooTask;
import com.wjs.schedule.service.CuckooTestUnDailyJob;

@Service
public class CuckooTestUnDailyJobImpl implements CuckooTestUnDailyJob {


	private static final Logger LOGGER = LoggerFactory.getLogger(CuckooTestUnDailyJobImpl.class);
	@Override
	@CuckooTask("testCronUnDailySucced")
	public void testCronUnDailySucced(JobInfoBean jobInfo) {
		// 测试完成
		LOGGER.info("Client exec done ,testCronUnDailySucced:{}" , jobInfo );
	}

	@Override
	@CuckooTask("testCronUnDailyFailed")
	public void testCronUnDailyFailed(JobInfoBean jobInfo) {
		// 测试完成
		LOGGER.info("Client exec done ,testCronUnDailyFailed:{}",  jobInfo);
		throw new RuntimeException("client throw a exception");
	}

	@Override
	public void testFlowUnDailySucced(JobInfoBean jobInfo) {
		// TODO 待测试
		
	}

	@Override
	public void testFlowUnDailyFailed(JobInfoBean jobInfo) {
		// TODO 待测试
		
	}

	@Override
	public void testFlowUnDailyDependencySucced(JobInfoBean jobInfo) {
		// TODO 待测试
		
	}

}
