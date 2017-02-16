package com.wjs.schedule.component.cuckoo;

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
import com.wjs.schedule.exception.BaseException;
import com.wjs.schedule.exception.JobDependencyException;
import com.wjs.schedule.service.Job.CuckooJobDependencyService;
import com.wjs.schedule.service.Job.CuckooJobLogService;
import com.wjs.schedule.service.Job.CuckooJobNextService;
import com.wjs.schedule.service.Job.CuckooJobService;
import com.wjs.schedule.service.server.CuckooServerService;

@Component("wjsCuckooJobExecutor")
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
	 * 流式任务执行器
	 * 
	 * @param jobId
	 * @param forceJob
	 *            true表示强制执行，不校验依赖状态
	 * @param needTrigglerNext
	 * @param startTime
	 * @param endTime
	 */
//	@Transactional
//	public void executeFlowJob(CuckooJobDetail jobInfo, Boolean needTrigglerNext, Long startTime,
//			Long endTime) {
//
//		// 不是强制执行的任务，需要校验上一次任务成功状态.并且流式任务需要时间具有连贯性，此处不连贯，可以后续来补充。
//		CuckooJobExecLogCriteria latestCrt = new CuckooJobExecLogCriteria();
//		latestCrt.createCriteria().andJobIdEqualTo(jobInfo.getId());
//		latestCrt.setOrderByClause(" id desc ");
//		latestCrt.setStart(0);
//		latestCrt.setLimit(1);
//		List<CuckooJobExecLog> latestJob = cuckooJobExecLogsMapper.selectByExample(latestCrt);
//		if (CollectionUtils.isNotEmpty(latestJob)) {
//			CuckooJobExecLog latestCuckooJobLog = latestJob.get(0);
//			if (!CuckooJobExecStatus.SUCCED.getValue().equals(latestCuckooJobLog.getExecJobStatus())) {
//				LOGGER.error("上一次任务尚未执行成功：jobName:{},jobId:{},startTime:{}", jobInfo.getJobName(), jobInfo.getId(),
//						startTime);
//				throw new BaseException("上一次任务尚未执行成功：jobName:{},jobId:{},startTime:{}", jobInfo.getJobName(),
//						jobInfo.getId(), startTime);
//			} else {
//
//				CuckooJobExecLogCriteria endTimeCrt = new CuckooJobExecLogCriteria();
//				endTimeCrt.createCriteria().andJobIdEqualTo(jobInfo.getId()).andLatestCheckTimeEqualTo(startTime);
//				endTimeCrt.setOrderByClause(" id desc ");
//				List<CuckooJobExecLog> endTimeCrtJob = cuckooJobExecLogsMapper.selectByExample(endTimeCrt);
//
//				if (CollectionUtils.isEmpty(endTimeCrtJob)) {
//					LOGGER.error("流式任务执行不连贯：jobName:{},jobId:{},startTime:{}", jobInfo.getJobName(), jobInfo.getId(),
//							startTime);
//				}
//			}
//		}
//
//		JobDataMap data = new JobDataMap();
//		data.put(CuckooJobConstant.NEED_TRIGGLE_NEXT, needTrigglerNext);
//		data.put(CuckooJobConstant.FLOW_JOB_START_TIME, startTime);
//		data.put(CuckooJobConstant.FLOW_JOB_END_TIME, endTime);
//
//		if (checkJobDependency(jobInfo, data)) {
//
//			executeJob(jobInfo, data);
//		}
//
//	}

	
	/**
	 * quartz任务执行器
	 * 
	 * @param jobId
	 * @param forceJob
	 * @param needTrigglerNext
	 * @param txdate
	 */
	@Transactional
	public void executeQuartzJob(CuckooJobDetail jobInfo) {
		
		// 查看作业状态，
		if(CuckooJobExecStatus.PENDING.getValue().equals(jobInfo.getExecJobStatus())){
			// 如果是pending状态的，取当前任务参数
			// do nothing
		}else if(CuckooJobExecStatus.SUCCED.getValue().equals(jobInfo.getExecJobStatus())){
			// 查看是否有触发任务。
			Long triggerJobId = cuckooJobNextService.findJobIdByNextJobId(jobInfo.getId());
			if(null != triggerJobId){
				// 如果有那么取触发任务的执行参数
				CuckooJobDetail triggerJobInfo = cuckooJobService.getJobById(triggerJobId);
				jobInfo.setExecJobStatus(CuckooJobExecStatus.PENDING.getValue());
				jobInfo.setTxDate(triggerJobInfo.getTxDate());
				jobInfo.setFlowLastTime(triggerJobInfo.getFlowLastTime());
				jobInfo.setFlowCurTime(triggerJobInfo.getFlowCurTime());
			}else{
				// 否则根据取任务上一次执行情况
				jobInfo.setExecJobStatus(CuckooJobExecStatus.PENDING.getValue());
				jobInfo.setTxDate(jobInfo.getTxDate());
				jobInfo.setFlowLastTime(jobInfo.getFlowCurTime());
				jobInfo.setFlowCurTime(System.currentTimeMillis());
			}
			
		}else{
			// 其他状态，加入到quartz待执行任务中，等待下次调度
			LOGGER.error("job 【{}】 status 【{}】 error,can not running job,this job will trigger 1 minute later,jobInfo:{}",jobInfo.getId(), jobInfo.getExecJobStatus(), jobInfo);
			return;
		}
		
		if (checkJobDependency(jobInfo)) {

			executeJob(jobInfo);
		}
	}


	// 执行任务
	private void executeJob(CuckooJobDetail jobInfo) {
		
		LOGGER.info("job start triggle,jobinfo:{}", jobInfo);

		cuckooJobDetailMapper.lockByPrimaryKey(jobInfo.getId());

		CuckooJobExecLog cuckooJobExecLogs = new CuckooJobExecLog();

		String remark = "";
		String execJobStatus = CuckooJobExecStatus.RUNNING.getValue();
		// 初始化执行日志
		String cuckooClientIp = null;
		String cuckooClientTag = null;
		cuckooJobExecLogs.setNeedTriggleNext(jobInfo.getNeedTriggleNext());
		cuckooJobExecLogs.setCronExpression(jobInfo.getCronExpression());
		cuckooJobExecLogs.setGroupId(jobInfo.getGroupId());
		cuckooJobExecLogs.setJobClassApplication(jobInfo.getJobClassApplication());
		cuckooJobExecLogs.setFlowLastTime(jobInfo.getFlowLastTime());
		cuckooJobExecLogs.setFlowCurTime(jobInfo.getFlowCurTime());
		cuckooJobExecLogs.setJobId(jobInfo.getId());
		cuckooJobExecLogs.setJobStartTime(System.currentTimeMillis());
		cuckooJobExecLogs.setRemark(remark);
		cuckooJobExecLogs.setTriggerType(jobInfo.getTriggerType());
		cuckooJobExecLogs.setTxDate(jobInfo.getTxDate());

		try {
			// 检验任务状态 -- 任务状态不能为执行中，否则报错
			if (CuckooJobExecStatus.RUNNING.getValue().equals(jobInfo.getExecJobStatus())) {

				cuckooJobExecLogs.setExecJobStatus(CuckooJobExecStatus.FAILED.getValue());
				LOGGER.error("job is aready running,please stop first, jobInfo:{}", jobInfo);
				throw new BaseException("job is aready running, jobInfo:{}", jobInfo);
			}

		} catch (BaseException e) {
			// 业务异常，报错处理
			execJobStatus = CuckooJobExecStatus.FAILED.getValue();
			remark = e.getMessage();
			LOGGER.error("任务执行报错,err:{},jobInfo:{}", e.getMessage(), jobInfo, e);
			// throw new BaseException(e.getMessage());
		} catch (Exception e) {
			// 未知异常，报错处理
			execJobStatus = CuckooJobExecStatus.FAILED.getValue();
			remark = e.getMessage();
			LOGGER.error("任务执行未知报错,err:{},jobInfo:{}", e.getMessage(), jobInfo, e);
			// throw new BaseException("任务执行未知报错,jobInfo:{},err:{}", jobInfo, e.getMessage());
		}

		
		try {
			// 查询远程执行器-- 考虑负载均衡 ,如果可执行客户端没有的话，放到数据库队列里面去。用于客户端重连等操作完成后操作
			List<CuckooClientJobDetail>  remoteExecutors = cuckooServerService.getExecRemotesId(jobInfo.getId());
			
			// 调用日志执行单元(远程调用)
			JobInfoBean jobBean = new JobInfoBean();
			jobBean.setFlowCurrTime(jobInfo.getFlowCurTime());
			jobBean.setFlowLastTime(jobInfo.getFlowLastTime());
			jobBean.setJobLogId(jobInfo.getId());
			jobBean.setJobId(jobInfo.getId());
			jobBean.setJobName(jobInfo.getJobName());
			jobBean.setTxDate(jobInfo.getTxDate());
			
			if(CollectionUtils.isEmpty(remoteExecutors)){

				// 执行器断线等特殊情况,放入待执行队列中 
				quartzExec.addSimpleJob(String.valueOf(jobInfo.getGroupId()), String.valueOf(jobInfo.getId()));
				LOGGER.warn("no executor fund, add job into todo queue,job:{}", jobInfo);
			}
			CuckooClientJobDetail remoteExecutor = cuckooServerService.execRemoteJob(remoteExecutors, jobBean);
			if(null == remoteExecutor){
				// 执行器断线等特殊情况,放入待执行队列中 
				quartzExec.addSimpleJob(String.valueOf(jobInfo.getGroupId()), String.valueOf(jobInfo.getId()));
				LOGGER.warn("no executor fund, add job into todo queue,job:{}", jobInfo);
			}
			cuckooClientIp = remoteExecutor.getCuckooClientIp();
			cuckooClientTag = remoteExecutor.getCuckooClientTag();
		} catch (Exception e) {
			// 未知异常，报错处理
			execJobStatus = CuckooJobExecStatus.FAILED.getValue();
			remark = e.getMessage();
			LOGGER.error("任务执行未知报错,err:{},jobInfo:{}", e.getMessage(), jobInfo, e);
		}
		// 更新任务状态
		jobInfo.setExecJobStatus(execJobStatus);
		cuckooJobDetailMapper.updateByPrimaryKeySelective(jobInfo);
				
		// 插入执行日志
		cuckooJobExecLogs.setCuckooClientIp(cuckooClientIp);
		cuckooJobExecLogs.setCuckooClientTag(cuckooClientTag);
		cuckooJobExecLogs.setRemark(remark);
		cuckooJobExecLogs.setExecJobStatus(execJobStatus);
		cuckooJobLogService.insertSelective(cuckooJobExecLogs);

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
		 
		CuckooJobDetail jobInfoFather = cuckooJobService.getJobById(jobInfoBean.getJobId());
		
		// 根据jobInfoBean查询下一个任务
		List<CuckooJobDetail> jobInfoNexts = cuckooJobService.getNextJobById(jobInfoBean.getJobId()); 
		
		if(CollectionUtils.isNotEmpty(jobInfoNexts)){
			JobDataMap data = new JobDataMap(); 
			data.put(CuckooJobConstant.DAILY_JOB_TXDATE, jobInfoBean.getTxDate());
			data.put(CuckooJobConstant.FLOW_JOB_END_TIME, jobInfoBean.getFlowCurrTime());
			data.put(CuckooJobConstant.FLOW_JOB_START_TIME, jobInfoBean.getFlowLastTime());
		
			for (CuckooJobDetail cuckooJobDetail : jobInfoNexts) {
				
				//  判断任务类型，修改任务状态为PENDING，放入到PENDING任务队列中
				if(CuckooIsTypeDaily.YES.getValue().equals(cuckooJobDetail.getTypeDaily())){
					cuckooJobService.pendingDailyJob(cuckooJobDetail.getId(), jobInfoFather.getNeedTriggleNext(), jobInfoFather.getTxDate());
				}else{
					cuckooJobService.pendingUnDailyJob(cuckooJobDetail.getId(), jobInfoFather.getNeedTriggleNext(), jobInfoFather.getFlowLastTime(), jobInfoFather.getFlowCurTime());
				}
			}
		}
	}
	
	private boolean checkJobDependency(CuckooJobDetail jobInfo) {

		// 校验任务依赖状态
		try {
			cuckooJobDependencyService.checkDepedencyJobFinished(jobInfo);
			return true;
		} catch (JobDependencyException e) {
			// 依赖任务未完成，放入待执行队列里面，等待调度 
			LOGGER.warn("Job dependencies not ready,jobInfo:{}", jobInfo);
//			addJobTodoCache(jobInfo, data);
			quartzExec.addSimpleJob(String.valueOf(jobInfo.getGroupId()), String.valueOf(jobInfo.getId()));
			return false;
		}
	}



}
