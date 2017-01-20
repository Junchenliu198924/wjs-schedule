package com.wjs.schedule.service;

import javax.print.attribute.standard.JobState;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wjs.schedule.ServiceUnitBaseTest;
import com.wjs.schedule.enums.JobStatus;
import com.wjs.schedule.enums.JobTriggerType;
import com.wjs.schedule.service.Job.CuckooJobService;
import com.wjs.schedule.vo.job.JobInfo;

public class CuckooJobServiceTest  extends ServiceUnitBaseTest{

	@Autowired
	CuckooJobService cuckooJobService;
	
	@Test
	public void testAddCronJob(){

		JobInfo jobInfo = new JobInfo();
		jobInfo.setGroupId(4L);
		jobInfo.setCronExpression("0/5 * * * * ?");
		jobInfo.setJobClassApplication("com.member");
		jobInfo.setJobClassName("execJob");
		jobInfo.setJobDesc("描述：测试cron任务");
		jobInfo.setJobName("测试cron任务");
		jobInfo.setJobStatus(JobStatus.RUNNING.getValue());
		jobInfo.setOffSet(-1);
		jobInfo.setTriggerType(JobTriggerType.CRON.getValue());
		
		cuckooJobService.addJob(jobInfo);
	}
	
	
	
	@Test
	public void testAddSimpleJob(){

		JobInfo jobInfo = new JobInfo();
		jobInfo.setGroupId(4L);
		jobInfo.setJobClassApplication("com.member");
		jobInfo.setJobClassName("execJob");
		jobInfo.setJobDesc("描述：测试flow任务");
		jobInfo.setJobName("测试flow任务");
		jobInfo.setJobStatus(JobStatus.RUNNING.getValue());
		jobInfo.setTriggerType(JobTriggerType.FLOW.getValue());
		
		cuckooJobService.addJob(jobInfo);
	}
}
