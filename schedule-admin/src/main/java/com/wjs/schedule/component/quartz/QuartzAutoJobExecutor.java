package com.wjs.schedule.component.quartz;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.wjs.schedule.dao.exec.CuckooJobDetailMapper;
import com.wjs.schedule.domain.exec.CuckooJobDetail;
import com.wjs.schedule.domain.exec.CuckooJobDetailCriteria;
import com.wjs.schedule.enums.CuckooJobStatus;
import com.wjs.schedule.enums.CuckooJobTriggerType;

@Component("quartzAutoJobExecutor")
public class QuartzAutoJobExecutor extends QuartzJobBean {


	private static final Logger LOGGER = LoggerFactory.getLogger(QuartzAutoJobExecutor.class);

	@Autowired
	CuckooJobDetailMapper cuckooJobDetailMapper;
	
	@Autowired
	QuartzManage quartzManage;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		 
		// 检查未初始化的Cron任务
		cronQuartzInit();
		
	}

	private void cronQuartzInit() {
		
		CuckooJobDetailCriteria crt = new CuckooJobDetailCriteria();
		crt.createCriteria().andTriggerTypeEqualTo(CuckooJobTriggerType.CRON.getValue());
		List<CuckooJobDetail> jobs = cuckooJobDetailMapper.selectByExample(crt);
		
		for (CuckooJobDetail jobDetail : jobs) {
			if(!quartzManage.checkExists(String.valueOf(jobDetail.getGroupId()), String.valueOf(jobDetail.getId()))){
				// CRON任务在Cuckoo中有，但是在quartz中没有
				quartzManage.addCronJob(String.valueOf(jobDetail.getGroupId()), String.valueOf(jobDetail.getId()), jobDetail.getCronExpression(), CuckooJobStatus.fromName(jobDetail.getJobStatus()));
			}
		}
		
	}
	
 

}
