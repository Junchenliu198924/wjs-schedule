package com.wjs.schedule.component.quartz;

import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.wjs.schedule.component.cuckoo.CuckooJobExecutor;
import com.wjs.schedule.constant.CuckooJobConstant;
import com.wjs.schedule.dao.exec.CuckooJobDetailsMapper;
import com.wjs.schedule.domain.exec.CuckooJobDetails;
import com.wjs.schedule.enums.CuckooJobTriggerType;
import com.wjs.schedule.exception.BaseException;

public class QuartzJobExecutor extends QuartzJobBean {


	private static final Logger LOGGER = LoggerFactory.getLogger(QuartzJobExecutor.class);

	@Autowired
	CuckooJobDetailsMapper cuckooJobDetailsMapper;
	
	@Autowired
	CuckooJobExecutor cuckooJobExecutor;
	
	@Autowired
	QuartzExec quartzExec;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		Trigger trigger = context.getTrigger();
		
		JobKey jobKey = trigger.getJobKey();
		

		// 根据jobId找到任务信息
		CuckooJobDetails cuckooJobDetails = cuckooJobDetailsMapper.selectByPrimaryKey(Long.valueOf(jobKey.getName()));
		if(null == cuckooJobDetails){
			LOGGER.error("can not find cuckoojob in quartzExecutor by jobGroup:{},jobName:{}", jobKey.getGroup(), jobKey.getName());
			throw new BaseException("can not find cuckoojob in quartzExecutor by jobGroup:{},jobName:{}", jobKey.getGroup(), jobKey.getName());
		}
		

		
		JobDataMap data = context.getMergedJobDataMap();
		
		Boolean needTrigglerNext = data.getBooleanFromString(CuckooJobConstant.NEED_TRIGGLE_NEXT);
		
		if(trigger instanceof CronTrigger){
			// cron or daily 任务触发
			String cronExpression = null;
			CronTrigger cronTrigger = (CronTrigger)trigger;
			cronExpression = cronTrigger.getCronExpression();
			
			
			if(CuckooJobTriggerType.CRON.getValue().equals(cuckooJobDetails.getTriggerType())){

				cuckooJobExecutor.executeCronJob(cuckooJobDetails, false, needTrigglerNext, cronExpression);
			}else if(CuckooJobTriggerType.DAILY.getValue().equals(cuckooJobDetails.getTriggerType())){
				Integer txdate = null;
				try {
					txdate = data.getIntegerFromString(CuckooJobConstant.DAILY_JOB_TXDATE);
				} catch (Exception e) {
					LOGGER.error("daily job can not get txdate,jobGroup:{},jobName:{}", jobKey.getGroup(), jobKey.getName(), e);
					throw new BaseException("daily job can not get txdate");
				}
				cuckooJobExecutor.executeDailyJob(cuckooJobDetails, false, needTrigglerNext, txdate, cronExpression);
			}else{
				
				LOGGER.error("not a cron type cuckoojob in quartzExecutor , jobGroup:{},jobName:{}", jobKey.getGroup(), jobKey.getName());
				throw new BaseException("not a cron type cuckoojob in quartzExecutor ");
			}
		}else{
			// TODO simple 任务触发
			
		}
		
		

		

		
	}
	
	public static void main(String[] args) {
		JobDataMap data = new JobDataMap();
		
		System.out.println(data.getBooleanFromString(CuckooJobConstant.NEED_TRIGGLE_NEXT));
		System.out.println(data.getIntegerFromString(CuckooJobConstant.DAILY_JOB_TXDATE));

	}

}
