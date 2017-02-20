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
import com.wjs.schedule.domain.exec.CuckooJobDetail;
import com.wjs.schedule.exception.BaseException;
import com.wjs.schedule.service.Job.CuckooJobService;

@Component("quartzJobExecutor")
public class QuartzJobExecutor extends QuartzJobBean {


	private static final Logger LOGGER = LoggerFactory.getLogger(QuartzJobExecutor.class);

	@Autowired
	CuckooJobDetailMapper cuckooJobDetailMapper;
	
	@Autowired
	CuckooJobExecutor cuckooJobExecutor;
	
	@Autowired
	CuckooJobService cuckooJobService;
	
	@Autowired
	QuartzManage quartzExec;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		Trigger trigger = context.getTrigger();

		JobKey jobKey = trigger.getJobKey();

		LOGGER.info("quartz trigger job, jobGroup:{},jobName:{},triggerType:{}", jobKey.getGroup(), jobKey.getName(),trigger.getClass());
		String quartzJobGroup = jobKey.getGroup();
		String[] quartzJobNameArr = jobKey.getName().split(CuckooJobConstant.QUARTZ_JOBNAME_JOINT);
		if (quartzJobNameArr.length != 2) {
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

		// if(trigger instanceof CronTrigger){
		// cron or daily 任务触发
		if(!cuckooJobExecutor.executeQuartzJob(cuckooJobDetail)){
			// 未执行成功的，需要放到pending中，等待执行
//			new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
				cuckooJobService.pendingJob(cuckooJobDetail.getGroupId(), cuckooJobDetail.getId());
//				}
//			}).start();
		};

		// }
	}
	
	public static void main(String[] args) {
		JobDataMap data = new JobDataMap();
		
		System.out.println(data.getBooleanFromString(CuckooJobConstant.NEED_TRIGGLE_NEXT));
		System.out.println(data.getIntegerFromString(CuckooJobConstant.DAILY_JOB_TXDATE));

	}

}
