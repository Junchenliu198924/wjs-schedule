package com.wjs.schedule.service;

import javax.print.attribute.standard.JobState;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wjs.schedule.ServiceUnitBaseTest;
import com.wjs.schedule.enums.CuckooIsTypeDaily;
import com.wjs.schedule.enums.CuckooJobStatus;
import com.wjs.schedule.enums.CuckooJobTriggerType;
import com.wjs.schedule.service.Job.CuckooJobService;
import com.wjs.schedule.vo.job.CuckooJobDetailVo;

public class CuckooJobServiceTest  extends ServiceUnitBaseTest{

	@Autowired
	CuckooJobService cuckooJobService;
	
	private static Long groupId = 1L;
	@Test
	public void testAddCronJob(){

		CuckooJobDetailVo jobInfo = new CuckooJobDetailVo();
		jobInfo.setGroupId(groupId);
		jobInfo.setCronExpression("0/5 * * * * ?");
		jobInfo.setJobClassApplication("com.cif");
		jobInfo.setJobDesc("描述：测试cron任务");
		jobInfo.setJobName("jobName1");
		jobInfo.setJobStatus(CuckooJobStatus.RUNNING.getValue());
		jobInfo.setTypeDaily(CuckooIsTypeDaily.YES.getValue());
		jobInfo.setOffset(-1);
		jobInfo.setTriggerType(CuckooJobTriggerType.CRON.getValue());
		
		cuckooJobService.addJob(jobInfo);
	}
	
	
	
	@Test
	public void testAddSimpleJob(){

		CuckooJobDetailVo jobInfo = new CuckooJobDetailVo();
		jobInfo.setGroupId(groupId);
		jobInfo.setJobClassApplication("com.member");
		jobInfo.setJobDesc("描述：测试flow任务");
		jobInfo.setJobName("execSimpleJob1");
		jobInfo.setTypeDaily(CuckooIsTypeDaily.YES.getValue());
		jobInfo.setJobStatus(CuckooJobStatus.RUNNING.getValue());
		jobInfo.setTriggerType(CuckooJobTriggerType.JOB.getValue());
		
		cuckooJobService.addJob(jobInfo);
	}
}
