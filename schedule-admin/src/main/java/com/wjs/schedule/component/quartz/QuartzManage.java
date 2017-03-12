package com.wjs.schedule.component.quartz;

import java.util.Date;

import javax.annotation.Resource;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.wjs.schedule.constant.CuckooJobConstant;
import com.wjs.schedule.domain.exec.CuckooJobExecLog;
import com.wjs.schedule.enums.CuckooJobStatus;
import com.wjs.schedule.exception.BaseException;

@Component("quartzManage")
public class QuartzManage {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QuartzManage.class);

	@Resource(name = "quartzScheduler")
	private Scheduler scheduler;

	static final String quartzCronGroup = "quartz_cron";
	static final String quartzSimpleGroup = "quartz_simple";
	public void addCronJob(String jobGroup, String jobName, String cronExpression, CuckooJobStatus jobStatus){
		String quartzJobName = jobGroup + CuckooJobConstant.QUARTZ_JOBNAME_JOINT + jobName;
		// TriggerKey : name + group
		TriggerKey triggerKey = TriggerKey.triggerKey(quartzJobName, quartzCronGroup);
		JobKey jobKey = new JobKey(quartzJobName, quartzCronGroup);

		// JobDetail : jobClass
		Class<? extends Job> jobClass_ = QuartzJobExecutor.class; // Class.forName(jobInfo.getJobClass());

		JobDetail jobDetail = JobBuilder.newJob(jobClass_).withIdentity(jobKey).build();

		// CronTrigger : TriggerKey + cronExpression //
		// withMisfireHandlingInstructionDoNothing 忽略掉调度终止过程中忽略的调度
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression)
				.withMisfireHandlingInstructionDoNothing();
		CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cronScheduleBuilder)
				.build();

		try {
			scheduler.scheduleJob(jobDetail, cronTrigger);
			if(CuckooJobStatus.PAUSE.equals(jobStatus)){

				scheduler.pauseTrigger(triggerKey);
			}
		} catch (SchedulerException e) {
			LOGGER.error("新增CRON任务调度失败:{}", e.getMessage(), e);
			throw new BaseException(e.getMessage());
		}
	}
	
//	/**
//	 * 简单触发器，用于触发手工执行任务，或者是由父任务触发的任务
//	 * @param groupId
//	 * @param id
//	 * @param running
//	 */
//	public void addSimpleJob(String jobGroup, String jobName) {
//		String quartzJobName = jobGroup + CuckooJobConstant.QUARTZ_JOBNAME_JOINT + jobName;
//		TriggerKey triggerKey = TriggerKey.triggerKey(quartzJobName, quartzSimpleGroup);
//		JobKey jobKey = new JobKey(quartzJobName, quartzSimpleGroup);
//		try {
//			
//			Class<? extends Job> jobClass_ = QuartzJobExecutor.class; // Class.forName(jobInfo.getJobClass());
//			JobDetail jobDetail = JobBuilder.newJob(jobClass_).withIdentity(jobKey).build();
//	
//			SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder
//					.repeatMinutelyForTotalCount(1) // 只触发一次
//					.withMisfireHandlingInstructionIgnoreMisfires();
//			SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
//					.withSchedule(simpleScheduleBuilder)
//					.startAt(new Date(System.currentTimeMillis() + 60000)) //  设置起始时间
//					.build();
//			if(scheduler.checkExists(jobKey) ){
////				System.err.println("rescheduleJob"+ quartzJobName);
////				scheduler.rescheduleJob(triggerKey, simpleTrigger);
//				scheduler.deleteJob(jobKey);
//				scheduler.scheduleJob(jobDetail, simpleTrigger);
////				System.err.println("rescheduleJob——"+ quartzJobName);
//			}else{
//
////				System.err.println("scheduleJob"+ quartzJobName);
//				scheduler.scheduleJob(jobDetail, simpleTrigger);
////				System.err.println("scheduleJob——"+ quartzJobName);
//			}
//		} catch (SchedulerException e) {
//			LOGGER.error("add simple job failed, groupName:{}, jobName:{},error:{}", quartzSimpleGroup, quartzJobName, e.getMessage(), e);
//			throw new BaseException(e.getMessage());
//		}
//		
//	}
	
	

	public void addSimpleJob(CuckooJobExecLog jobLog) {
		String quartzJobName = jobLog.getGroupId() + CuckooJobConstant.QUARTZ_JOBNAME_JOINT + jobLog.getJobId();
		TriggerKey triggerKey = TriggerKey.triggerKey(quartzJobName, quartzSimpleGroup);
		JobKey jobKey = new JobKey(quartzJobName, quartzSimpleGroup);
		try {
			
			Class<? extends Job> jobClass_ = QuartzJobExecutor.class; // Class.forName(jobInfo.getJobClass());
			JobDetail jobDetail = JobBuilder.newJob(jobClass_).withIdentity(jobKey).build();
			// 放入此次执行的日志参数
			jobDetail.getJobDataMap().put(CuckooJobConstant.JOB_EXEC_ID, jobLog.getId());
			
			SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder
					.repeatMinutelyForTotalCount(1) // 只触发一次
					.withMisfireHandlingInstructionIgnoreMisfires();
			SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
					.withSchedule(simpleScheduleBuilder)
					.startAt(new Date(System.currentTimeMillis() + 60000)) //  设置起始时间
					.build();
			
			if(scheduler.checkExists(jobKey) ){
//				System.err.println("rescheduleJob"+ quartzJobName);
//				scheduler.rescheduleJob(triggerKey, simpleTrigger);
				scheduler.deleteJob(jobKey);
				scheduler.scheduleJob(jobDetail, simpleTrigger);
//				System.err.println("rescheduleJob——"+ quartzJobName);
			}else{

//				System.err.println("scheduleJob"+ quartzJobName);
				scheduler.scheduleJob(jobDetail, simpleTrigger);
//				System.err.println("scheduleJob——"+ quartzJobName);
			}
		} catch (SchedulerException e) {
			LOGGER.error("add simple job failed, groupName:{}, jobName:{},error:{}", quartzSimpleGroup, quartzJobName, e.getMessage(), e);
			throw new BaseException(e.getMessage());
		}
		
	}
	

	public void deleteCronJob(String jobGroup, String jobName) {

		String quartzJobName = jobGroup + CuckooJobConstant.QUARTZ_JOBNAME_JOINT + jobName;
		JobKey jobKey = new JobKey(quartzJobName, quartzCronGroup);
		try {
			if(scheduler.checkExists(jobKey) ){
				scheduler.deleteJob(jobKey);
			}
		} catch (SchedulerException e) {
			LOGGER.error("删除任务调度失败:{}", e.getMessage(), e);
			throw new BaseException(e.getMessage());
		}

	}

	public void modfyCronJob(String jobGroup, String jobName, String cronExpression, CuckooJobStatus jobStatus) {

		String quartzJobName = jobGroup + CuckooJobConstant.QUARTZ_JOBNAME_JOINT + jobName;
		JobKey jobKey = new JobKey(quartzJobName, quartzCronGroup);
		try {
			if(scheduler.checkExists(jobKey) ){
				scheduler.deleteJob(jobKey);
				addCronJob(jobGroup, jobName, cronExpression, jobStatus);
			}
		} catch (SchedulerException e) {
			LOGGER.error("修改任务调度失败:{}", e.getMessage(), e);
			throw new BaseException(e.getMessage());
		}
	}

	public void pauseJob(String jobGroup, String jobName) {

		String quartzJobName = jobGroup + CuckooJobConstant.QUARTZ_JOBNAME_JOINT + jobName;
		JobKey jobKey = new JobKey(quartzJobName, quartzCronGroup);
		try {
			if(scheduler.checkExists(jobKey) ){
				scheduler.pauseJob(jobKey);
			}
		} catch (SchedulerException e) {
			LOGGER.error("暂停任务调度失败:jobGroup:{},jobName:{},{}", quartzCronGroup, quartzJobName, e.getMessage(), e);
			throw new BaseException(e.getMessage());
		}
		
	}

	public void pauseAll() {
		
		try {
			scheduler.pauseAll();
		} catch (SchedulerException e) {
			LOGGER.error("暂停所有任务调度失败:{}", e.getMessage(), e);
			throw new BaseException(e.getMessage());
		}
		
	}

	public void resumeJob(String jobGroup, String jobName) {

		String quartzJobName = jobGroup + CuckooJobConstant.QUARTZ_JOBNAME_JOINT + jobName;
		JobKey jobKey = new JobKey(quartzJobName, quartzCronGroup);
		try {
			if(scheduler.checkExists(jobKey) ){
				scheduler.resumeJob(jobKey);
			}
		} catch (SchedulerException e) {
			LOGGER.error("恢复任务调度失败:jobGroup:{},jobName:{},{}", quartzCronGroup, quartzJobName, e.getMessage(), e);
			throw new BaseException(e.getMessage());
		}
	}

	public void resumeAll() {
		
		try {
			scheduler.resumeAll();
		} catch (SchedulerException e) {
			LOGGER.error("恢复所有任务调度失败:{}", e.getMessage(), e);
			throw new BaseException(e.getMessage());
		}
	}
	
	
	/**
	 * 
	 * @param jobGroup
	 * @param jobName
	 * @param forceJob -- 是否强制启动（非强制启动，如果时间连接不上的话，会中断执行）
	 * @param needTrigglerNext
	 * @param tx_date
	 */
	public void triggerJob(String jobGroup, String jobName, Boolean needTrigglerNext, Integer txdate) {

		String quartzJobName = jobGroup + CuckooJobConstant.QUARTZ_JOBNAME_JOINT + jobName;
		JobKey jobKey = new JobKey(quartzJobName, quartzCronGroup);
		JobDataMap data = new JobDataMap();
		data.put(CuckooJobConstant.NEED_TRIGGLE_NEXT, needTrigglerNext);
		data.put(CuckooJobConstant.DAILY_JOB_TXDATE, txdate);
		try {
			if(scheduler.checkExists(jobKey) ){
				scheduler.triggerJob(jobKey, data);
			}
		} catch (SchedulerException e) {
			LOGGER.error("执行任务调度失败:jobGroup:{},jobName:{},{}", quartzCronGroup, quartzJobName, e.getMessage(), e);
			throw new BaseException(e.getMessage());
		}
	}


	
	
}
