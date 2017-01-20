package com.wjs.schedule.quartz;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.wjs.schedule.ServiceUnitBaseTest;
import com.wjs.schedule.component.quartz.QuartzJobExecutor;

public class QuartzApiTest extends ServiceUnitBaseTest {

	static String jobName1 = "testJobName";
	static String jobGroup = "jobGroup";

	@Resource(name = "quartzScheduler")
	private Scheduler scheduler;

	/**
	 * 新增Cron调度
	 */
	@Test
	public void testAddCronJob() {

		// TriggerKey : name + group
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName1, jobGroup);
		JobKey jobKey = new JobKey(jobName1, jobGroup);

		// JobDetail : jobClass
		Class<? extends Job> jobClass_ = QuartzJobExecutor.class; // Class.forName(jobInfo.getJobClass());

		JobDetail jobDetail = JobBuilder.newJob(jobClass_).withIdentity(jobKey).build();

		String cronExpression = "0 0/5 * * * ?";
		// CronTrigger : TriggerKey + cronExpression //
		// withMisfireHandlingInstructionDoNothing 忽略掉调度终止过程中忽略的调度
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression)
				.withMisfireHandlingInstructionDoNothing();
		CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cronScheduleBuilder)
				.build();

		// schedule : jobDetail + cronTrigger
		try {
			Date date = scheduler.scheduleJob(jobDetail, cronTrigger);
			System.out.println(date);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 新增Cron调度
	 */
	@Test
	public void testAddSimpleJob() {
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName1, jobGroup);
		JobKey jobKey = new JobKey(jobName1, jobGroup);
		Class<? extends Job> jobClass_ = QuartzJobExecutor.class; // Class.forName(jobInfo.getJobClass());
		JobDetail jobDetail = JobBuilder.newJob(jobClass_).withIdentity(jobKey).build();

		SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.repeatMinutelyForTotalCount(1)
				.withMisfireHandlingInstructionIgnoreMisfires();
		SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
				.withSchedule(simpleScheduleBuilder).build();
		try {
			Date date = scheduler.scheduleJob(jobDetail, simpleTrigger);
			System.out.println(date);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

	}

}
