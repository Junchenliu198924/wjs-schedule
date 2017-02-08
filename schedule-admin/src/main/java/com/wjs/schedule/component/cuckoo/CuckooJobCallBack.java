package com.wjs.schedule.component.cuckoo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wjs.schedule.bean.JobInfoBean;
import com.wjs.schedule.domain.exec.CuckooJobExecLogs;
import com.wjs.schedule.enums.JobExecStatus;
import com.wjs.schedule.service.Job.CuckooJobLogService;
import com.wjs.schedule.service.Job.CuckooJobService;

/**
 * 客户端任务执行完成，调用方法
 * @author Silver
 *
 */
@Component("wjsCuckooJobCallBack")
public class CuckooJobCallBack  {


	private static final Logger LOGGER = LoggerFactory.getLogger(CuckooJobCallBack.class);
	@Autowired
	CuckooJobService cuckooJobService;
	
	@Autowired
	CuckooJobLogService cuckooJobLogService;
	
	@Autowired
	CuckooJobExecutor cuckooJobExecutor;
	/**
	 *  客户端任务执行成功回调
	 * @param jobInfo
	 */
	@Transactional
	public void execJobSuccedCallBack(JobInfoBean jobInfo) {
		
		CuckooJobExecLogs jobLog = cuckooJobLogService.getJobLogByLogId(jobInfo.getJobLogId());
		
		// 修改任务状态
		cuckooJobService.updateJobStatusById(jobLog.getJobId(), JobExecStatus.SUCCED);
		// 更新日志
		cuckooJobLogService.updateJobLogStatusById(jobLog.getId(), JobExecStatus.SUCCED);
		
		// 触发下级任务
		cuckooJobExecutor.executeNextJob(jobInfo);
	}
	
	/**
	 * 客户端任务执行失败回调
	 * @param jobInfo
	 */
	@Transactional
	public void execJobFailedCallBack(JobInfoBean jobInfo) {

		CuckooJobExecLogs jobLog = cuckooJobLogService.getJobLogByLogId(jobInfo.getJobLogId());
		
		// 修改任务状态
		cuckooJobService.updateJobStatusById(jobLog.getJobId(), JobExecStatus.FAILED);
		// 更新日志
		cuckooJobLogService.updateJobLogStatusById(jobLog.getId(), JobExecStatus.FAILED);
	}

}
