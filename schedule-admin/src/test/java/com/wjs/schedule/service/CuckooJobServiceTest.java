package com.wjs.schedule.service;

import javax.print.attribute.standard.JobState;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wjs.schedule.ServiceUnitBaseTest;
import com.wjs.schedule.enums.CuckooJobStatus;
import com.wjs.schedule.enums.CuckooJobTriggerType;
import com.wjs.schedule.service.Job.CuckooJobService;
import com.wjs.schedule.vo.job.CuckooJobDetailsVo;

public class CuckooJobServiceTest  extends ServiceUnitBaseTest{

	@Autowired
	CuckooJobService cuckooJobService;
	
	private static Long groupId = 4L;
	@Test
	public void testAddCronJob(){

		CuckooJobDetailsVo jobInfo = new CuckooJobDetailsVo();
		jobInfo.setGroupId(groupId);
		jobInfo.setCronExpression("0/5 * * * * ?");
		jobInfo.setJobClassApplication("com.cif");
		jobInfo.setJobClassName("execJob");
		jobInfo.setJobDesc("描述：测试cron任务");
		jobInfo.setJobName("测试cron任务");
		jobInfo.setJobStatus(CuckooJobStatus.RUNNING.getValue());
		jobInfo.setOffset(-1);
		jobInfo.setTriggerType(CuckooJobTriggerType.CRON.getValue());
		
		cuckooJobService.addJob(jobInfo);
	}
	
	
	
	@Test
	public void testAddSimpleJob(){

		CuckooJobDetailsVo jobInfo = new CuckooJobDetailsVo();
		jobInfo.setGroupId(groupId);
		jobInfo.setJobClassApplication("com.member");
		jobInfo.setJobClassName("execJob");
		jobInfo.setJobDesc("描述：测试flow任务");
		jobInfo.setJobName("测试flow任务");
		jobInfo.setJobStatus(CuckooJobStatus.RUNNING.getValue());
		jobInfo.setTriggerType(CuckooJobTriggerType.FLOW.getValue());
		
		cuckooJobService.addJob(jobInfo);
	}
}
