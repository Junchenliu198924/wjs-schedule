package com.wjs.schedule.service.Job;

import com.wjs.schedule.vo.job.JobGroup;

public interface CuckooGroupService {
	
	/**
	 * 新增分组，返回新增分组自增长主键
	 * @param group
	 * @return primarykey
	 */
	public Long addGroup(JobGroup group);
	
}
