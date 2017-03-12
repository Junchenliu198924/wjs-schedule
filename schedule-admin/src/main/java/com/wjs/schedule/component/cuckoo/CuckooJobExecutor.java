package com.wjs.schedule.component.cuckoo;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wjs.schedule.bean.JobInfoBean;
import com.wjs.schedule.component.quartz.QuartzManage;
import com.wjs.schedule.constant.CuckooJobConstant;
import com.wjs.schedule.dao.exec.CuckooJobDetailMapper;
import com.wjs.schedule.dao.exec.CuckooJobExecLogMapper;
import com.wjs.schedule.domain.exec.CuckooClientJobDetail;
import com.wjs.schedule.domain.exec.CuckooJobDetail;
import com.wjs.schedule.domain.exec.CuckooJobExecLog;
import com.wjs.schedule.enums.CuckooIsTypeDaily;
import com.wjs.schedule.enums.CuckooJobExecStatus;
import com.wjs.schedule.enums.CuckooJobStatus;
import com.wjs.schedule.exception.BaseException;
import com.wjs.schedule.exception.JobCanNotRunningException;
import com.wjs.schedule.service.Job.CuckooJobDependencyService;
import com.wjs.schedule.service.Job.CuckooJobLogService;
import com.wjs.schedule.service.Job.CuckooJobNextService;
import com.wjs.schedule.service.Job.CuckooJobService;
import com.wjs.schedule.service.server.CuckooServerService;
import com.wjs.util.DateUtil;

@Component("cuckooJobExecutor")
public class CuckooJobExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(CuckooJobExecutor.class);

	@Autowired
	CuckooJobDetailMapper cuckooJobDetailMapper;

	@Autowired
	CuckooJobExecLogMapper cuckooJobExecLogsMapper;

	@Autowired
	CuckooServerService cuckooServerService;

	@Autowired
	CuckooJobNextService cuckooJobNextService;
	
	@Autowired
	CuckooJobDependencyService cuckooJobDependencyService;

	@Autowired
	CuckooJobLogService cuckooJobLogService;

	@Autowired
	CuckooJobService cuckooJobService;
	
	@Autowired
	QuartzManage quartzExec;
	
	
	/**
	 * quartz任务执行器
	 * 
	 * @param jobId
	 * @param forceJob
	 * @param needTrigglerNext
	 * @param txdate
	 * @return 任务是否执行成功
	 */
	@Transactional
	public Boolean executeQuartzJob(CuckooJobExecLog jobLog) {

		if (!CuckooJobExecStatus.PENDING.getValue().equals(jobLog.getExecJobStatus())) {

			LOGGER.error("invalid job exec status:{},jobLogInfo:{}", jobLog.getExecJobStatus(), jobLog);
		}

		if (checkJobCanRunning(jobLog)) {

			return executeJob(jobLog);
		} else {
			return false;
		}
	}


	// 执行任务
	private Boolean executeJob(CuckooJobExecLog jobLog) {
		
		LOGGER.info("job start execjob,jobLog:{}", jobLog);
		
		boolean rtn = true;

		cuckooJobExecLogsMapper.lockByPrimaryKey(jobLog.getId());


		String remark = "";
		String execJobStatus = CuckooJobExecStatus.RUNNING.getValue();
		// 初始化执行日志
		String cuckooClientIp = null;
		String cuckooClientTag = null;
		
		try {
			// 查询远程执行器-- 考虑负载均衡 ,如果可执行客户端没有的话，放到数据库队列里面去。用于客户端重连等操作完成后操作
			List<CuckooClientJobDetail>  remoteExecutors = cuckooServerService.getExecRemotesId(jobLog.getJobId());
			if(CollectionUtils.isEmpty(remoteExecutors)){
				LOGGER.error("no executor fund, add job into todo queue,jobLog:{}", jobLog);
				rtn = false;
			}
			

			// 调用日志执行单元(远程调用)
			JobInfoBean jobBean = new JobInfoBean();
			jobBean.setFlowCurrTime(jobLog.getFlowCurTime());
			jobBean.setFlowLastTime(jobLog.getFlowLastTime());
			jobBean.setJobId(jobLog.getId());
			jobBean.setJobName(jobLog.getJobName());
			jobBean.setTxDate(jobLog.getTxDate());
			jobBean.setJobLogId(jobLog.getId());
			
			CuckooClientJobDetail remoteExecutor = cuckooServerService.execRemoteJob(remoteExecutors, jobBean);
			if(null == remoteExecutor){
				// 执行器断线等特殊情况,放入待执行队列中 
				LOGGER.error("no executor fund, add job into todo queue,jobLog:{}", jobLog);
				rtn = false;
			}else{ 
				cuckooClientIp = remoteExecutor.getCuckooClientIp();
				cuckooClientTag = remoteExecutor.getCuckooClientTag();
			}
			 
		} catch (Exception e) {
			// 未知异常，报错处理
			execJobStatus = CuckooJobExecStatus.FAILED.getValue();
			remark = e.getMessage();
			LOGGER.error("failed job exec,err:{},jobInfo:{}", e.getMessage(), jobLog, e);
			rtn = false;
		}
		
		// 插入执行日志
		jobLog.setCuckooClientIp(cuckooClientIp);
		jobLog.setCuckooClientTag(cuckooClientTag);
		jobLog.setRemark(remark);
		jobLog.setExecJobStatus(execJobStatus);
		cuckooJobExecLogsMapper.updateByPrimaryKeySelective(jobLog);
		return rtn;

	}

	public static void main(String[] args) {
		JobDataMap data = new JobDataMap();
		data.put(CuckooJobConstant.NEED_TRIGGLE_NEXT,"1");
		System.out.println(BooleanUtils.toBoolean(data.getString(CuckooJobConstant.NEED_TRIGGLE_NEXT)));
	}

	

//	/**
//	 * 当前任务触发
//	 * @param jobInfoBean
//	 */
//	public void executeCurJob(JobInfoBean jobInfoBean) {
//		
//		if(null == jobInfoBean){
//			return;
//		}
//		// 根据jobInfoBean查询当前任务
//		CuckooJobDetail jobInfoNext = cuckooJobService.getJobById(jobInfoBean.getJobId());
//		JobDataMap data = new JobDataMap();
//		data.put(CuckooJobConstant.DAILY_JOB_TXDATE, jobInfoBean.getTxDate());
//		data.put(CuckooJobConstant.FLOW_JOB_END_TIME, jobInfoBean.getFlowCurrTime());
//		data.put(CuckooJobConstant.FLOW_JOB_START_TIME, jobInfoBean.getFlowLastTime());
//		data.put(CuckooJobConstant.NEED_TRIGGLE_NEXT, jobInfoBean.getNeedTrigglerNext());
//		// 获取job性质
//		if (checkJobDependency(jobInfoNext, data)) {
//
//			executeJob(jobInfoNext, data);
//		}
//	}


	/**
	 * 下级任务触发，调用任务执行功能
	 * @param jobInfo
	 */
	public void executeNextJob(JobInfoBean jobInfoBean) {
		 
		

		CuckooJobExecLog jobLog = cuckooJobExecLogsMapper.selectByPrimaryKey(jobInfoBean.getJobLogId());
		
		// 根据jobInfoBean查询下一个任务
		List<CuckooJobDetail> jobInfoNexts = cuckooJobService.getNextJobById(jobLog.getJobId()); 
		
		if(CollectionUtils.isNotEmpty(jobInfoNexts)){
			

			JobDataMap data = new JobDataMap(); 
			data.put(CuckooJobConstant.JOB_EXEC_ID, jobLog.getId());
			
			for (CuckooJobDetail cuckooJobDetail : jobInfoNexts) {
				//  判断任务类型，修改任务状态为PENDING，放入到PENDING任务队列中
				cuckooJobService.pendingJob(cuckooJobDetail, jobLog);
				
			}
		}
	}
	
	private boolean checkJobCanRunning(CuckooJobExecLog jobLog){
		
		// 查询任务信息
		CuckooJobDetail jobInfo = cuckooJobDetailMapper.selectByPrimaryKey(jobLog.getJobId());

		if(!jobLog.getForceTriggle()){
			// 如果任务不是手工调度的情况下，状态为暂停，等待下次调度（手工调度任务，不需要关注任务执行状态是否为暂停）
			if (CuckooJobStatus.PAUSE.getValue().equals(jobInfo.getJobStatus())) {
				LOGGER.info("job is paush,triggle next time, jobInfo:{}", jobInfo);
				jobLog.setRemark("job is paush,triggle next time");
				cuckooJobExecLogsMapper.updateByPrimaryKeySelective(jobLog);
				return false;
			}
		}
		

		// 校验任务依赖状态
		try {
			cuckooJobDependencyService.checkDepedencyJobFinished(jobLog);
			return true;
		} catch (JobCanNotRunningException e) {
			// 依赖任务未完成，放入待执行队列里面，等待调度
			jobLog.setRemark(e.getMessage());
			cuckooJobExecLogsMapper.updateByPrimaryKeySelective(jobLog);
			return false;
		}
	}



}
