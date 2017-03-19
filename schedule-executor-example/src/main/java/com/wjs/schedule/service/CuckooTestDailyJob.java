package com.wjs.schedule.service;

import com.wjs.schedule.bean.JobInfoBean;

public interface CuckooTestDailyJob {


	public void testCronDailySucced(JobInfoBean jobInfo);
	

	public void testCronDailyFailed(JobInfoBean jobInfo);
}
