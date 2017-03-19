package com.wjs.schedule.service;

import com.wjs.schedule.bean.JobInfoBean;

public interface CuckooTestUnDailyJob {
	
	public void testCronUnDailySucced(JobInfoBean jobInfo);
	public void testCronUnDailyFailed(JobInfoBean jobInfo);

}
