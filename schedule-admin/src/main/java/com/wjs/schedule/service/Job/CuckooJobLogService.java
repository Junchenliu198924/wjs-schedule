package com.wjs.schedule.service.Job;

import com.wjs.schedule.domain.exec.CuckooJobExecLogs;
import com.wjs.schedule.enums.JobExecStatus;

public interface CuckooJobLogService {
	
	/**
	 * 新增日志
	 * @param log
	 */
	public void insertSelective(CuckooJobExecLogs log);

	/**
	 * 根据日志ID获取日志信息
	 * @param id
	 * @return
	 */
	public CuckooJobExecLogs getJobLogByLogId(Long id);

	/**
	 * 根据主键更新日志状态
	 * @param id
	 * @param succed
	 */
	public void updateJobLogStatusById(Long id, JobExecStatus succed);
}
