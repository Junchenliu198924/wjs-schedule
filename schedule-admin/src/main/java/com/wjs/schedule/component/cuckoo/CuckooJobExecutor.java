package com.wjs.schedule.component.cuckoo;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wjs.schedule.bean.JobInfoBean;
import com.wjs.schedule.constant.CuckooJobConstant;
import com.wjs.schedule.dao.exec.CuckooJobDetailsMapper;
import com.wjs.schedule.dao.exec.CuckooJobExecLogsMapper;
import com.wjs.schedule.domain.exec.CuckooClientJobDetail;
import com.wjs.schedule.domain.exec.CuckooJobDetails;
import com.wjs.schedule.domain.exec.CuckooJobExecLogs;
import com.wjs.schedule.domain.exec.CuckooJobExecLogsCriteria;
import com.wjs.schedule.enums.CuckooJobExecStatus;
import com.wjs.schedule.enums.CuckooJobTriggerType;
import com.wjs.schedule.exception.BaseException;
import com.wjs.schedule.exception.JobDependencyException;
import com.wjs.schedule.service.Job.CuckooJobDependencyService;
import com.wjs.schedule.service.Job.CuckooJobLogService;
import com.wjs.schedule.service.Job.CuckooJobService;
import com.wjs.schedule.service.server.CuckooServerService;
import com.wjs.util.DateUtil;

@Component("wjsCuckooJobExecutor")
public class CuckooJobExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(CuckooJobExecutor.class);

	@Autowired
	CuckooJobDetailsMapper cuckooJobDetailsMapper;

	@Autowired
	CuckooJobExecLogsMapper cuckooJobExecLogsMapper;

	@Autowired
	CuckooServerService cuckooServerService;

	@Autowired
	CuckooJobDependencyService cuckooJobDependencyService;

	@Autowired
	CuckooJobLogService cuckooJobLogService;

	@Autowired
	CuckooJobService cuckooJobService;
	
	
	@PostConstruct
	public void initPendingJobExec(){
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for (;;) {
					cuckooJobService.tryTrigglePendingJob();
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// ignore
					}
				}
				
			}
		});
		t.start();
	}
	
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
	@Transactional
	public void executeFlowJob(CuckooJobDetails jobInfo, Boolean forceJob, Boolean needTrigglerNext, Long startTime,
			Long endTime) {

		if (!forceJob) {
			// 不是强制执行的任务，需要校验上一次任务成功状态.并且流式任务需要时间具有连贯性，此处不连贯，可以后续来补充。
			CuckooJobExecLogsCriteria latestCrt = new CuckooJobExecLogsCriteria();
			latestCrt.createCriteria().andJobIdEqualTo(jobInfo.getId());
			latestCrt.setOrderByClause(" id desc ");
			latestCrt.setStart(0);
			latestCrt.setLimit(1);
			List<CuckooJobExecLogs> latestJob = cuckooJobExecLogsMapper.selectByExample(latestCrt);
			if (CollectionUtils.isNotEmpty(latestJob)) {
				CuckooJobExecLogs latestCuckooJobLog = latestJob.get(0);
				if (!CuckooJobExecStatus.SUCCED.getValue().equals(latestCuckooJobLog.getExecJobStatus())) {
					LOGGER.error("上一次任务尚未执行成功：jobName:{},jobId:{},startTime:{}", jobInfo.getJobName(), jobInfo.getId(),
							startTime);
					throw new BaseException("上一次任务尚未执行成功：jobName:{},jobId:{},startTime:{}", jobInfo.getJobName(),
							jobInfo.getId(), startTime);
				} else {

					CuckooJobExecLogsCriteria endTimeCrt = new CuckooJobExecLogsCriteria();
					endTimeCrt.createCriteria().andJobIdEqualTo(jobInfo.getId()).andLatestCheckTimeEqualTo(startTime);
					endTimeCrt.setOrderByClause(" id desc ");
					List<CuckooJobExecLogs> endTimeCrtJob = cuckooJobExecLogsMapper.selectByExample(endTimeCrt);

					if (CollectionUtils.isEmpty(endTimeCrtJob)) {
						LOGGER.error("流式任务执行不连贯：jobName:{},jobId:{},startTime:{}", jobInfo.getJobName(),
								jobInfo.getId(), startTime);
					}
				}
			}
		}

		JobDataMap data = new JobDataMap();
		data.put(CuckooJobConstant.FORCE_JOB, forceJob);
		data.put(CuckooJobConstant.NEED_TRIGGLE_NEXT, needTrigglerNext);
		data.put(CuckooJobConstant.FLOW_JOB_START_TIME, startTime);
		data.put(CuckooJobConstant.FLOW_JOB_END_TIME, endTime);
		
		if(checkJobDependency(jobInfo, data)){

			executeJob(jobInfo, data);
		}
		

	}

	
	/**
	 * 普通Cron任务执行器
	 * 
	 * @param jobId
	 * @param forceJob
	 * @param needTrigglerNext
	 * @param txdate
	 */
	@Transactional
	public void executeCronJob(CuckooJobDetails jobInfo, Boolean forceJob, Boolean needTrigglerNext,
			String cronExpression) {

		if (!forceJob) {
			// 不是强制执行的任务，需要校验上一次任务成功状态.普通cron只需要检查上一次执行情况即可。
			CuckooJobExecLogsCriteria crt = new CuckooJobExecLogsCriteria();
			crt.createCriteria().andJobIdEqualTo(jobInfo.getId());
			crt.setOrderByClause(" id desc ");
			crt.setStart(0);
			crt.setLimit(1);
			List<CuckooJobExecLogs> result = cuckooJobExecLogsMapper.selectByExample(crt);
			if (CollectionUtils.isNotEmpty(result)) {
				CuckooJobExecLogs latestCuckooJobLog = result.get(0);
				if (!CuckooJobExecStatus.SUCCED.getValue().equals(latestCuckooJobLog.getExecJobStatus())) {
					LOGGER.error("上一次任务尚未执行成功：jobName:{},jobId:{}", jobInfo.getJobName(), jobInfo.getId());
					throw new BaseException("上一次任务尚未执行成功：jobName:{},jobId:{}", jobInfo.getJobName(), jobInfo.getId());
				}
			}
		}

		JobDataMap data = new JobDataMap();
		data.put(CuckooJobConstant.FORCE_JOB, forceJob);
		data.put(CuckooJobConstant.NEED_TRIGGLE_NEXT, needTrigglerNext);
		data.put(CuckooJobConstant.QUARTZ_CRON_EXP, cronExpression);

		if(checkJobDependency(jobInfo, data)){

			executeJob(jobInfo, data);
		}
	}

	/**
	 * 日切Cron任务执行器
	 * 
	 * @param jobId
	 * @param forceJob
	 * @param needTrigglerNext
	 * @param txdate
	 */
	@Transactional
	public void executeDailyJob(CuckooJobDetails jobInfo, Boolean forceJob, Boolean needTrigglerNext, Integer txdate,
			String cronExpression) {

		if (!forceJob) {
			// 不是强制执行的任务，需要校验上一次任务成功状态.日切cron需要检查上一天执行情况。
			CuckooJobExecLogsCriteria yesterdayCrt = new CuckooJobExecLogsCriteria();
			yesterdayCrt.createCriteria().andJobIdEqualTo(jobInfo.getId())
					.andTxDateEqualTo(DateUtil.addIntDate(txdate, -1));
			yesterdayCrt.setOrderByClause(" id desc ");
			List<CuckooJobExecLogs> yesterdayResult = cuckooJobExecLogsMapper.selectByExample(yesterdayCrt);
			if (CollectionUtils.isNotEmpty(yesterdayResult)) {
				CuckooJobExecLogs latestCuckooJobLog = yesterdayResult.get(0);
				if (!CuckooJobExecStatus.SUCCED.getValue().equals(latestCuckooJobLog.getExecJobStatus())) {
					LOGGER.error("上一次任务尚未执行成功：jobName:{},jobId:{},txdate:{}", jobInfo.getJobName(), jobInfo.getId(),
							txdate);
					throw new BaseException("上一次任务尚未执行成功：jobName:{},jobId:{},txdate:{}", jobInfo.getJobName(),
							jobInfo.getId(), txdate);
				}
			} else {
				// 如果上一天记录为空，但是记录不为空。表示缺少一天数据
				CuckooJobExecLogsCriteria crt = new CuckooJobExecLogsCriteria();
				crt.createCriteria().andJobIdEqualTo(jobInfo.getId());
				crt.setStart(0);
				crt.setLimit(1);
				List<CuckooJobExecLogs> result = cuckooJobExecLogsMapper.selectByExample(crt);
				if (CollectionUtils.isNotEmpty(result)) {
					// 有可能配置节假日情况，此处不做处理，暂通过业务进行控制
					LOGGER.warn("缺少任务执行缺少上一天的数据：jobName:{},jobId:{},txdate:{}", jobInfo.getJobName(), jobInfo.getId(),
							txdate);
				}
			}
		}

		JobDataMap data = new JobDataMap();
		data.put(CuckooJobConstant.FORCE_JOB, forceJob);
		data.put(CuckooJobConstant.NEED_TRIGGLE_NEXT, needTrigglerNext);
		data.put(CuckooJobConstant.QUARTZ_CRON_EXP, cronExpression);
		data.put(CuckooJobConstant.DAILY_JOB_TXDATE, txdate);

		if(checkJobDependency(jobInfo, data)){

			executeJob(jobInfo, data);
		}

	}

	// 执行任务
	private void executeJob(CuckooJobDetails jobInfo, JobDataMap data) {
		
		LOGGER.info("job start triggle,jobinfo:{},data:{}", jobInfo, data);

		cuckooJobDetailsMapper.lockByPrimaryKey(jobInfo.getId());

		CuckooJobExecLogs cuckooJobExecLogs = new CuckooJobExecLogs();

		String remark = "";
		String execJobStatus = CuckooJobExecStatus.RUNNING.getValue();
		// 初始化执行日志
		String cuckooClientIp = null;
		String cuckooClientTag = null;
		cuckooJobExecLogs.setNeedTriggleNext(data.getBooleanValue(CuckooJobConstant.NEED_TRIGGLE_NEXT));
		Object cronExp = data.get(CuckooJobConstant.QUARTZ_CRON_EXP);
		cuckooJobExecLogs.setCronExpression(cronExp == null ? null : String.valueOf(cronExp));
		cuckooJobExecLogs.setGroupId(jobInfo.getGroupId());
		cuckooJobExecLogs.setJobClassApplication(jobInfo.getJobClassApplication());
		Object curTime = data.get(CuckooJobConstant.FLOW_JOB_END_TIME);
		Long flowCurrTime = (curTime == null ? null : Long.valueOf(String.valueOf(curTime)));
		cuckooJobExecLogs.setJobEndTime(flowCurrTime);
		cuckooJobExecLogs.setJobId(jobInfo.getId());
		Object latestTime = data.get(CuckooJobConstant.FLOW_JOB_START_TIME);
		Long flowLastTime = (latestTime == null ? System.currentTimeMillis()
				: Long.valueOf(String.valueOf(latestTime)));
		cuckooJobExecLogs.setJobStartTime(flowLastTime);
		cuckooJobExecLogs.setLatestCheckTime(null);
		cuckooJobExecLogs.setRemark(remark);
		cuckooJobExecLogs.setTriggerType(jobInfo.getTriggerType());
		Object bizDate = data.get(CuckooJobConstant.DAILY_JOB_TXDATE);
		Integer txDate = (bizDate == null ? null : Integer.valueOf(String.valueOf(bizDate)));
		cuckooJobExecLogs.setTxDate(txDate);

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
			jobBean.setFlowCurrTime(flowCurrTime);
			jobBean.setFlowLastTime(flowLastTime);
			jobBean.setJobLogId(jobInfo.getId());
			jobBean.setJobId(jobInfo.getId());
			jobBean.setJobName(jobInfo.getJobName());
			jobBean.setTriggerType(CuckooJobTriggerType.fromName(jobInfo.getTriggerType()));
			jobBean.setForceJob(data.getBooleanFromString(CuckooJobConstant.FORCE_JOB));
			jobBean.setNeedTrigglerNext(data.getBooleanFromString(CuckooJobConstant.NEED_TRIGGLE_NEXT));
			jobBean.setTxDate(txDate);
			
			if(CollectionUtils.isEmpty(remoteExecutors)){

				// 执行器断线等特殊情况,放入待执行队列中 
				addJobTodoCache(jobInfo, data);
				LOGGER.warn("no executor fund, add job into todo queue,job:{},data:{}", jobInfo, data);
			}
			CuckooClientJobDetail remoteExecutor = cuckooServerService.execRemoteJob(remoteExecutors, jobBean);
			if(null == remoteExecutor){
				// 执行器断线等特殊情况,放入待执行队列中 
				addJobTodoCache(jobInfo, data);
				LOGGER.warn("no executor fund, add job into todo queue,job:{},data:{}", jobInfo, data);
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
		cuckooJobDetailsMapper.updateByPrimaryKeySelective(jobInfo);
				
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

	

	/**
	 * 当前任务触发
	 * @param jobInfoBean
	 */
	public void executeCurJob(JobInfoBean jobInfoBean) {
		
		if(null == jobInfoBean){
			return;
		}
		// 根据jobInfoBean查询当前任务
		CuckooJobDetails jobInfoNext = cuckooJobService.getJobById(jobInfoBean.getJobId());
		JobDataMap data = new JobDataMap();
		data.put(CuckooJobConstant.DAILY_JOB_TXDATE, jobInfoBean.getTxDate());
		data.put(CuckooJobConstant.FLOW_JOB_END_TIME, jobInfoBean.getFlowCurrTime());
		data.put(CuckooJobConstant.FLOW_JOB_START_TIME, jobInfoBean.getFlowLastTime());
		data.put(CuckooJobConstant.NEED_TRIGGLE_NEXT, jobInfoBean.getNeedTrigglerNext());
		// 获取job性质
		if (checkJobDependency(jobInfoNext, data)) {

			executeJob(jobInfoNext, data);
		}
	}


	/**
	 * 下级任务触发，调用任务执行功能
	 * @param jobInfo
	 */
	public void executeNextJob(JobInfoBean jobInfoBean) {
		
		// 根据jobInfoBean查询下一个任务
		List<CuckooJobDetails> jobInfoNexts = cuckooJobService.getNextJobById(jobInfoBean.getJobId()); 
		
		if(CollectionUtils.isNotEmpty(jobInfoNexts)){
			JobDataMap data = new JobDataMap(); 
			data.put(CuckooJobConstant.DAILY_JOB_TXDATE, jobInfoBean.getTxDate());
			data.put(CuckooJobConstant.FLOW_JOB_END_TIME, jobInfoBean.getFlowCurrTime());
			data.put(CuckooJobConstant.FLOW_JOB_START_TIME, jobInfoBean.getFlowLastTime());
			data.put(CuckooJobConstant.NEED_TRIGGLE_NEXT, jobInfoBean.getNeedTrigglerNext());
		
			for (CuckooJobDetails cuckooJobDetails : jobInfoNexts) {
					// 获取job性质
				if(checkJobDependency(cuckooJobDetails, data)){ 

					executeJob(cuckooJobDetails, data);
				}
				
			}
		}
	}
	
	private boolean checkJobDependency(CuckooJobDetails jobInfo, JobDataMap data) {

		// 校验任务依赖状态
		try {
			cuckooJobDependencyService.checkDepedencyJobFinished(jobInfo, data);
			return true;
		} catch (JobDependencyException e) {
			// 依赖任务未完成，放入待执行队列里面，等待调度 
			LOGGER.warn("Job dependencies not ready,jobInfo:{},data:{}", jobInfo, data);
			addJobTodoCache(jobInfo, data);
			return false;
		}
	}

	
	

	/**
	 * 增加任务回调，放入任务执行队列中，用于：
	 * 1.下级任务已经被触发，但是还有部分依赖上级任务还没有完成.
	 * 2.由定时任务触发，但是又同时依赖上级任务的完成
	 * @param jobInfo
	 */
	private void addJobTodoCache(CuckooJobDetails jobInfo, JobDataMap data) {
		// 更新任务状态为Pending
		cuckooJobService.updateJobStatusById(jobInfo.getId(), CuckooJobExecStatus.PENDING);
		
	}

}
