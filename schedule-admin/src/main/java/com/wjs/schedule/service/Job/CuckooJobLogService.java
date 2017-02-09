package com.wjs.schedule.service.Job;

import com.wjs.schedule.domain.exec.CuckooJobDetails;
import com.wjs.schedule.domain.exec.CuckooJobExecLogs;
import com.wjs.schedule.enums.CuckooJobExecStatus;

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
	public void updateJobLogStatusById(Long id, CuckooJobExecStatus succed);

	/**
	 * 从日志中查询是否触发下级任务
	 * @param preJobInfo
	 * @return
	 */
	public Boolean getJobNeedTriglerByJobInfo(CuckooJobDetails preJobInfo);

	
}
