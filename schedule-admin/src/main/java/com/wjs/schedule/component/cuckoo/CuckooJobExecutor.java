package com.wjs.schedule.component.cuckoo;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
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
import com.wjs.schedule.enums.JobExecStatus;
import com.wjs.schedule.exception.BaseException;
import com.wjs.schedule.exception.JobDependencyException;
import com.wjs.schedule.service.Job.CuckooJobDependencyService;
import com.wjs.schedule.service.Job.CuckooJobLogService;
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
				if (!JobExecStatus.SUCCED.getValue().equals(latestCuckooJobLog.getExecJobStatus())) {
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
		executeJob(jobInfo, data);

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
				if (!JobExecStatus.SUCCED.getValue().equals(latestCuckooJobLog.getExecJobStatus())) {
					LOGGER.error("上一次任务尚未执行成功：jobName:{},jobId:{}", jobInfo.getJobName(), jobInfo.getId());
					throw new BaseException("上一次任务尚未执行成功：jobName:{},jobId:{}", jobInfo.getJobName(), jobInfo.getId());
				}
			}
		}

		JobDataMap data = new JobDataMap();
		data.put(CuckooJobConstant.FORCE_JOB, forceJob);
		data.put(CuckooJobConstant.NEED_TRIGGLE_NEXT, needTrigglerNext);
		data.put(CuckooJobConstant.QUARTZ_CRON_EXP, cronExpression);

		executeJob(jobInfo, data);

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
				if (!JobExecStatus.SUCCED.getValue().equals(latestCuckooJobLog.getExecJobStatus())) {
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
					// 有可能配置节假日情况
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

		executeJob(jobInfo, data);

	}

	// 执行任务
	private void executeJob(CuckooJobDetails jobInfo, JobDataMap data) {
		
		LOGGER.info("job start triggle,jobinfo:{},data:{}", jobInfo, data);

		cuckooJobDetailsMapper.lockByPrimaryKey(jobInfo.getId());

		CuckooJobExecLogs cuckooJobExecLogs = new CuckooJobExecLogs();

		String remark = "";
		String execJobStatus = JobExecStatus.RUNNING.getValue();
		// 初始化执行日志
		String cuckooClientIp = null;
		String cuckooClientTag = null;
		cuckooJobExecLogs.setCheckTimes(0);
		Object cronExp = data.get(CuckooJobConstant.QUARTZ_CRON_EXP);
		cuckooJobExecLogs.setCronExpression(cronExp == null ? null : String.valueOf(cronExp));
		cuckooJobExecLogs.setGroupId(jobInfo.getGroupId());
		cuckooJobExecLogs.setJobClassApplication(jobInfo.getJobClassApplication());
		cuckooJobExecLogs.setJobClassName(jobInfo.getJobClassName());
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
			if (JobExecStatus.RUNNING.getValue().equals(jobInfo.getExecJobStatus())) {

				cuckooJobExecLogs.setExecJobStatus(JobExecStatus.FAILED.getValue());
				LOGGER.error("job is aready running,please stop first, jobInfo:{}", jobInfo);
				throw new BaseException("job is aready running, jobInfo:{}", jobInfo);
			}
			// 校验任务依赖状态
			cuckooJobDependencyService.checkDepedencyJobFinished(jobInfo, data);

		} catch (JobDependencyException e) {
			// 如果是依赖任务的未完成话，不做报错处理。等待下次调度
			execJobStatus = JobExecStatus.RUNNING.getValue();
			remark = e.getMessage();
			// throw new BaseException(e.getMessage());
		} catch (BaseException e) {
			// 业务异常，报错处理
			execJobStatus = JobExecStatus.FAILED.getValue();
			remark = e.getMessage();
			LOGGER.error("任务执行报错,err:{},jobInfo:{}", e.getMessage(), jobInfo, e);
			// throw new BaseException(e.getMessage());
		} catch (Exception e) {
			// 未知异常，报错处理
			execJobStatus = JobExecStatus.FAILED.getValue();
			remark = e.getMessage();
			LOGGER.error("任务执行未知报错,err:{},jobInfo:{}", e.getMessage(), jobInfo, e);
			// throw new BaseException("任务执行未知报错,jobInfo:{},err:{}", jobInfo, e.getMessage());
		}

		
		try {
			// 查询远程执行器 -- 考虑负载均衡 
			List<CuckooClientJobDetail>  remoteExecutors = cuckooServerService.getExecRemotesId(jobInfo.getId());
			
			// 调用日志执行单元(远程调用)
			JobInfoBean jobBean = new JobInfoBean();
			jobBean.setFlowCurrTime(flowCurrTime);
			jobBean.setFlowLastTime(flowLastTime);
			jobBean.setJobLogId(jobInfo.getId());
			jobBean.setJobName(jobInfo.getJobName());
			jobBean.setForceJob(data.getBooleanFromString(CuckooJobConstant.FORCE_JOB));
			jobBean.setNeedTrigglerNext(data.getBooleanFromString(CuckooJobConstant.NEED_TRIGGLE_NEXT));
			jobBean.setTxDate(txDate);
			
			if(CollectionUtils.isEmpty(remoteExecutors)){
				throw new BaseException("can not find remote executor");
			}
			CuckooClientJobDetail remoteExecutor = cuckooServerService.execRemoteJob(remoteExecutors, jobBean);
			if(null == remoteExecutor){
				// 执行器断线等特殊情况
				throw new BaseException("can not find remote executor");
			}
			cuckooClientIp = remoteExecutor.getIp();
			cuckooClientTag = remoteExecutor.getCuckooClientTag();
		} catch (Exception e) {
			// 未知异常，报错处理
			execJobStatus = JobExecStatus.FAILED.getValue();
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

		System.out.println(data.getBooleanFromString(CuckooJobConstant.FLOW_JOB_END_TIME));
	}

}
