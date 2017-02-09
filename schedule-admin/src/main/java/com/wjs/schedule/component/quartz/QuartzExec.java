package com.wjs.schedule.component.quartz;

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
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.wjs.schedule.constant.CuckooJobConstant;
import com.wjs.schedule.enums.CuckooJobStatus;
import com.wjs.schedule.exception.BaseException;

@Component("quartzExec")
public class QuartzExec {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QuartzExec.class);

	@Resource(name = "quartzScheduler")
	private Scheduler scheduler;

	public void addCronJob(String jobGroup, String jobName, String cronExpression, CuckooJobStatus jobStatus){

		// TriggerKey : name + group
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
		JobKey jobKey = new JobKey(jobName, jobGroup);

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
			LOGGER.error("新增任务调度失败:{}", e.getMessage(), e);
			throw new BaseException(e.getMessage());
		}
	}

	public void deleteJob(String jobGroup, String jobName) {

		JobKey jobKey = new JobKey(jobName, jobGroup);
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
		
		JobKey jobKey = new JobKey(jobName, jobGroup);
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

		JobKey jobKey = new JobKey(jobName, jobGroup);
		try {
			if(scheduler.checkExists(jobKey) ){
				scheduler.pauseJob(jobKey);
			}
		} catch (SchedulerException e) {
			LOGGER.error("暂停任务调度失败:jobGroup:{},jobName:{},{}", jobGroup, jobName, e.getMessage(), e);
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
		
		JobKey jobKey = new JobKey(jobName, jobGroup);
		try {
			if(scheduler.checkExists(jobKey) ){
				scheduler.resumeJob(jobKey);
			}
		} catch (SchedulerException e) {
			LOGGER.error("恢复任务调度失败:jobGroup:{},jobName:{},{}", jobGroup, jobName, e.getMessage(), e);
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
	public void triggerJob(String jobGroup, String jobName, boolean forceJob, Boolean needTrigglerNext, Integer txdate) {

		JobKey jobKey = new JobKey(jobName, jobGroup);
		JobDataMap data = new JobDataMap();
		data.put(CuckooJobConstant.FORCE_JOB, forceJob);
		data.put(CuckooJobConstant.NEED_TRIGGLE_NEXT, needTrigglerNext);
		data.put(CuckooJobConstant.DAILY_JOB_TXDATE, txdate);
		try {
			if(scheduler.checkExists(jobKey) ){
				scheduler.triggerJob(jobKey, data);
			}
		} catch (SchedulerException e) {
			LOGGER.error("执行任务调度失败:jobGroup:{},jobName:{},{}", jobGroup, jobName, e.getMessage(), e);
			throw new BaseException(e.getMessage());
		}
	}
	
	
}
