package com.wjs.schedule.component.quartz;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.wjs.schedule.component.cuckoo.CuckooJobExecutor;
import com.wjs.schedule.constant.CuckooJobConstant;
import com.wjs.schedule.dao.exec.CuckooJobDetailMapper;
import com.wjs.schedule.dao.exec.CuckooJobExecLogMapper;
import com.wjs.schedule.domain.exec.CuckooJobDetail;
import com.wjs.schedule.domain.exec.CuckooJobExecLog;
import com.wjs.schedule.exception.BaseException;
import com.wjs.schedule.service.Job.CuckooJobLogService;
import com.wjs.schedule.service.Job.CuckooJobService;

@Component("quartzJobExecutor")
public class QuartzJobExecutor extends QuartzJobBean {


	private static final Logger LOGGER = LoggerFactory.getLogger(QuartzJobExecutor.class);

	@Autowired
	CuckooJobDetailMapper cuckooJobDetailMapper;
	
	@Autowired
	CuckooJobExecLogMapper cuckooJobExecLogMapper;
	
	@Autowired
	CuckooJobExecutor cuckooJobExecutor;
	
	@Autowired
	CuckooJobService cuckooJobService;
	

	@Autowired
	CuckooJobLogService cuckooJobLogService;
	
	@Autowired
	QuartzManage quartzExec;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		
		
		Trigger trigger = context.getTrigger();
		JobKey jobKey = trigger.getJobKey();	
		
		JobDataMap data = context.getMergedJobDataMap();
		Object execIdObj = data.get(CuckooJobConstant.JOB_EXEC_ID);
		CuckooJobExecLog cuckooJobExecLog= null;
		
		if(null == execIdObj){
			// 如果日志ID为空，表示当前任务为CRON触发，新增执行日志(一般情况为任务调度节点的第一个任务)
			LOGGER.info("quartz trigger cron job, jobGroup:{},jobName:{},triggerType:{}", jobKey.getGroup(), jobKey.getName(),trigger.getClass());
			
			String quartzJobGroup = jobKey.getGroup();
			String[] quartzJobNameArr = jobKey.getName().split(CuckooJobConstant.QUARTZ_JOBNAME_JOINT);
			if (quartzJobNameArr.length < 2) {
				LOGGER.error("Unformat quartz Job ,group:{},name:{} ", quartzJobGroup, jobKey.getName());
				throw new BaseException("Unformat quartz Job ,group:{},name:{} ", quartzJobGroup, jobKey.getName());
			}
			Long cuckooJobId = Long.valueOf(quartzJobNameArr[1]);

			// 根据jobId找到任务信息
			final CuckooJobDetail cuckooJobDetail = cuckooJobDetailMapper.selectByPrimaryKey(cuckooJobId);
			if (null == cuckooJobDetail) {
				LOGGER.error("can not find cuckoojob in quartzExecutor by jobGroup:{},jobName:{}", jobKey.getGroup(),
						jobKey.getName());
				throw new BaseException("can not find cuckoojob in quartzExecutor by jobGroup:{},jobName:{}",
						jobKey.getGroup(), jobKey.getName());
			}
			cuckooJobExecLog = cuckooJobLogService.initSysCronJobLog(cuckooJobId ,cuckooJobDetail);
			
		}else{
			LOGGER.info("quartz trigger flow job, jobGroup:{},jobName:{},execIdObj:{},triggerType:{}", jobKey.getGroup(), jobKey.getName(), execIdObj,trigger.getClass());
			
			Long execId = Long.valueOf(String.valueOf(execIdObj));
			// 如果日志ID不为空，表示当前日志是通过上级任务触发或者是有等待执行的任务
			cuckooJobExecLog = cuckooJobExecLogMapper.selectByPrimaryKey(execId);
		}
		 

		

		if(!cuckooJobExecutor.executeQuartzJob(cuckooJobExecLog)){
			
			cuckooJobService.rependingJob(cuckooJobExecLog);
		};

	}
	

	public static void main(String[] args) {
		JobDataMap data = new JobDataMap();
		
		System.out.println(data.getBooleanFromString(CuckooJobConstant.NEED_TRIGGLE_NEXT));
		System.out.println(data.getIntegerFromString(CuckooJobConstant.DAILY_JOB_TXDATE));

	}

}
