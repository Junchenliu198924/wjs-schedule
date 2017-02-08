package com.wjs.schedule.service.Job;

import com.wjs.schedule.enums.JobExecStatus;
import com.wjs.schedule.vo.job.JobInfo;

/**
 * 任务执行服务接口
 * @author Silver
 *
 */
public interface CuckooJobService {
	
	
	
	/**
	 * 新增一个任务,返回任务id
	 */
	public Long addJob(JobInfo jobInfo);
	

	/**
	 * 删除一个任务
	 */
	public void removeJob(Long id);
	
	/**
	 * 修改一个任务
	 */
	public void modifyJob(JobInfo jobInfo);
	
	
	
	/**
	 * 暂停一个任务
	 */
	public void pauseOnejob(Long id);
	
	/**
	 * 暂停所有任务
	 */
	public void pauseAllJob();
	
	/**
	 * 恢复一个任务
	 */
	public void resumeOnejob(Long id);
	
	/**
	 * 恢复所有任务
	 */
	public void resumeAllJob();
	
	/**
	 * 手工触发一个Cron任务
	 * @param id
	 * @param triggleNext
	 */
	public void forceTriggleCronJob(Long id,Boolean needTrigglerNext,Integer tx_date);
	 
	/**
	 * 手工触发一个Flow任务
	 * @param id
	 * @param triggleNext
	 */
	public void forceTriggleFlowJob(Long id,Boolean needTrigglerNext,Long startTime,Long endTime);


	/**
	 * 修改任务执行状态
	 * @param jobId
	 * @param succed
	 */
	public void updateJobStatusById(Long jobId, JobExecStatus succed);
	 
	
}
