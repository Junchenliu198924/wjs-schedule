package com.wjs.schedule.service.Job;

import com.wjs.schedule.domain.exec.CuckooJobDetail;
import com.wjs.schedule.domain.exec.CuckooJobExecLog;
import com.wjs.schedule.enums.CuckooJobExecStatus;

public interface CuckooJobLogService {
	
	/**
	 * 新增日志
	 * @param log
	 * @return 
	 */
	public Long insertSelective(CuckooJobExecLog log);

	/**
	 * 根据日志ID获取日志信息
	 * @param id
	 * @return
	 */
	public CuckooJobExecLog getJobLogByLogId(Long id);

	/**
	 * 根据主键更新日志状态
	 * @param id
	 * @param succed
	 * @param message 
	 */
	public void updateJobLogStatusById(Long id, CuckooJobExecStatus succed, String message);

	/**
	 * 从日志中查询是否触发下级任务
	 * @param preJobInfo
	 * @return
	 */
	public Boolean getJobNeedTriglerByJobInfo(CuckooJobDetail preJobInfo);

	/**
	 * 按主键修改日志
	 * @param cuckooJobExecLogs
	 */
	public void updateJobLogByPk(CuckooJobExecLog cuckooJobExecLogs);

	
}
