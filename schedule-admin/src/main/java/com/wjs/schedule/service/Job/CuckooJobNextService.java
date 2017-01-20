package com.wjs.schedule.service.Job;

import java.util.List;

import com.wjs.schedule.vo.job.JobNext;

public interface CuckooJobNextService {


	/**
	 * 设置触发的后续任务
	 */
	public void setNextJobConfig(List<JobNext> nextJobs);
}
