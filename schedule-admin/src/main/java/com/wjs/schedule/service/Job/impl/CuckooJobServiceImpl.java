package com.wjs.schedule.service.Job.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wjs.schedule.component.cuckoo.CuckooJobExecutor;
import com.wjs.schedule.component.quartz.QuartzManage;
import com.wjs.schedule.dao.exec.CuckooJobDetailMapper;
import com.wjs.schedule.dao.exec.CuckooJobExecLogMapper;
import com.wjs.schedule.dao.exec.CuckooJobGroupMapper;
import com.wjs.schedule.domain.exec.CuckooJobDetail;
import com.wjs.schedule.domain.exec.CuckooJobDetailCriteria;
import com.wjs.schedule.domain.exec.CuckooJobExecLog;
import com.wjs.schedule.domain.exec.CuckooJobGroup;
import com.wjs.schedule.enums.CuckooJobExecStatus;
import com.wjs.schedule.enums.CuckooJobStatus;
import com.wjs.schedule.enums.CuckooJobTriggerType;
import com.wjs.schedule.exception.BaseException;
import com.wjs.schedule.service.Job.CuckooJobLogService;
import com.wjs.schedule.service.Job.CuckooJobNextService;
import com.wjs.schedule.service.Job.CuckooJobService;
import com.wjs.schedule.util.CuckBeanUtil;
import com.wjs.schedule.vo.job.CuckooJobDetailVo;
import com.wjs.schedule.vo.qry.JobInfoQry;
import com.wjs.util.bean.PropertyUtil;
import com.wjs.util.dao.PageDataList;

@Service("cuckooJobService")
public class CuckooJobServiceImpl implements CuckooJobService{



	private static final Logger LOGGER = LoggerFactory.getLogger(CuckooJobServiceImpl.class);
	@Autowired
	CuckooJobGroupMapper cuckooJobGroupMapper;
	
	@Autowired
	CuckooJobDetailMapper cuckooJobDetailMapper;
	
	@Autowired
	CuckooJobExecLogMapper cuckooJobExecLogMapper;
	
	@Autowired
	CuckooJobExecutor cuckooJobExecutor;
	
	@Autowired
	QuartzManage quartzManage;
	
	@Autowired
	CuckooJobNextService cuckooJobNextService;
	
	@Autowired
	CuckooJobLogService cuckooJobLogService;
	
	@Override
	@Transactional
	public Long addJob(CuckooJobDetailVo jobInfo) {
		
		if(null == jobInfo || null == jobInfo.getGroupId()){
			
			throw new BaseException("jobInfo should not be null");
		}
		// 校验分组是否存在
		CuckooJobGroup cuckooJobGroup = cuckooJobGroupMapper.selectByPrimaryKey(jobInfo.getGroupId());
		if(null == cuckooJobGroup){
			throw new BaseException("can not find jobgroup by groupId:"+jobInfo.getGroupId());
		}
		// 校验任务名称是否重复
//		CuckooJobDetailCriteria jobNameCrt = new CuckooJobDetailCriteria();
//		jobNameCrt.createCriteria().andGroupIdEqualTo(jobInfo.getGroupId()).andJobNameEqualTo(jobInfo.getJobName());
//		List<CuckooJobDetail> jobNameChecks = cuckooJobDetailMapper.selectByExample(jobNameCrt);
//		if(CollectionUtils.isNotEmpty(jobNameChecks)){
//			throw new BaseException("job name has already exists:" + jobInfo.getJobName());
//		}
		// 如果是cron，校验cron是否正确
		if(CuckooJobTriggerType.CRON.getValue().equals(jobInfo.getTriggerType())){
			jobInfo.setCronExpression(StringUtils.trim(jobInfo.getCronExpression()));
			if(!CronExpression.isValidExpression(jobInfo.getCronExpression())){

				throw new BaseException("cronexpression is not valid:"+jobInfo.getCronExpression());
			}
		}
		// 新增wjs_schedule_cockoo_job_details 数据，默认暂停
		CuckooJobDetail cuckooJobDetail = CuckBeanUtil.parseJob(jobInfo);
		
		cuckooJobDetailMapper.insertSelective(cuckooJobDetail);
		Long jobId = cuckooJobDetailMapper.lastInsertId();
		if(jobId == null){

			throw new BaseException("cuckoo_job_details insert error,can not get autoincriment id");
		}
		
		// 如果是cron类型的，调用quartzAPI,新增任务，并且设置任务为暂停
		if(CuckooJobTriggerType.CRON.getValue().equals(jobInfo.getTriggerType())){
			quartzManage.addCronJob(String.valueOf(jobInfo.getGroupId()), String.valueOf(jobId), jobInfo.getCronExpression(), CuckooJobStatus.fromName(jobInfo.getJobStatus()));
		}
		
		return jobId;
		
	}

	@Override
	@Transactional
	public void removeJob(Long id) {
		
		if(null == id){
			throw new BaseException("id should not be null");
		}
		// 根据ID查询任务信息
		CuckooJobDetail cuckooJobDetail = cuckooJobDetailMapper.selectByPrimaryKey(id);
		// 根据id删除cuckoo数据
		if(null != cuckooJobDetail){
			cuckooJobDetailMapper.deleteByPrimaryKey(id);
			// 根据任务信息删除quartz信息
			quartzManage.deleteCronJob(String.valueOf(cuckooJobDetail.getGroupId()), String.valueOf(cuckooJobDetail.getId()));
		}
			
	}

	@Override
	@Transactional
	public void modifyJob(CuckooJobDetailVo jobInfo) {
		
		if(null == jobInfo || null == jobInfo.getId()){
			throw new BaseException("jobinfo should not be null");
		}

		// 根据ID查询任务信息
		CuckooJobDetail orginJobDetail = cuckooJobDetailMapper.selectByPrimaryKey(jobInfo.getId());

		if(null == orginJobDetail){
			throw new BaseException("can not find jobinfo by id : " + jobInfo.getId());
		}
		CuckooJobDetail targetJobDetail = CuckBeanUtil.parseJob(jobInfo);
		
		// 修改cuckoo任务
		if(orginJobDetail.getGroupId().equals(targetJobDetail.getGroupId())){
			// 如果原始任务组编号与目前任务组编号相同
			cuckooJobDetailMapper.updateByPrimaryKeySelective(targetJobDetail);
			if(CuckooJobTriggerType.JOB.getValue().equals(orginJobDetail.getTriggerType()) && CuckooJobTriggerType.CRON.getValue().equals(targetJobDetail.getTriggerType())){
				// 原来任务类型为job触发 且新任务为Cron，那么需要新增quartz。否则不做处理
				quartzManage.addCronJob(String.valueOf(targetJobDetail.getGroupId()), String.valueOf(targetJobDetail), jobInfo.getCronExpression(), CuckooJobStatus.fromName(targetJobDetail.getJobStatus()));
				
			}else if(CuckooJobTriggerType.CRON.getValue().equals(orginJobDetail.getTriggerType()) ){
				// 如果原来任务类型为Cron，那么修改一条任务
				if(CuckooJobTriggerType.CRON.getValue().equals(targetJobDetail.getTriggerType())){
					// 且新任务为Cron，那么需要修改quartz
					quartzManage.modfyCronJob(String.valueOf(targetJobDetail.getGroupId()), String.valueOf(targetJobDetail), jobInfo.getCronExpression(), CuckooJobStatus.fromName(targetJobDetail.getJobStatus()));
				}else{
					// 且新任务为NORMAL，那么需要删除quartz
					quartzManage.deleteCronJob(String.valueOf(orginJobDetail.getGroupId()), String.valueOf(orginJobDetail.getJobName()));
				}
			}else {
				throw new BaseException("unknow job triggle type : "+ jobInfo.getTriggerType());
			}
		}else{
			// 如果原始任务组编号与目前任务组编号不同
			// 删除任务
			quartzManage.deleteCronJob(String.valueOf(orginJobDetail.getGroupId()), String.valueOf(orginJobDetail.getJobName()));
			// 新增任务
			if(CuckooJobTriggerType.CRON.getValue().equals(targetJobDetail.getTriggerType())){
				// 新任务为Cron，那么需要新增quartz。否则不做处理
				quartzManage.addCronJob(String.valueOf(targetJobDetail.getGroupId()), String.valueOf(targetJobDetail), jobInfo.getCronExpression(), CuckooJobStatus.fromName(targetJobDetail.getJobStatus()));
			}
		}
		
	}



	@Override
	public void pauseOnejob(Long id) {
		
		// 根据ID查询Cuckoo
		if(null == id){
			throw new BaseException("id should not be null");
		}

		// 根据ID查询任务信息
		CuckooJobDetail orginJobDetail = cuckooJobDetailMapper.selectByPrimaryKey(id);
		orginJobDetail.setJobStatus(CuckooJobStatus.PAUSE.getValue());
		
		// 更新cuckoo状态
		cuckooJobDetailMapper.updateByPrimaryKeySelective(orginJobDetail);
		
		// 更新quartz任务状态
		if(CuckooJobTriggerType.CRON.getValue().equals(orginJobDetail.getTriggerType())){
			quartzManage.pauseJob(String.valueOf(orginJobDetail.getGroupId()),String.valueOf(orginJobDetail.getId()));
		}
	}

	@Override
	public void pauseAllJob() {

		// 更新cuckoo状态
		CuckooJobDetail orginJobDetail = new CuckooJobDetail();
		orginJobDetail.setJobStatus(CuckooJobStatus.PAUSE.getValue());
		cuckooJobDetailMapper.updateByExampleSelective(orginJobDetail, new CuckooJobDetailCriteria());
		
		// 更新quartz任务状态
		quartzManage.pauseAll();
	}

	@Override
	public void resumeOnejob(Long id) {

		// 根据ID查询Cuckoo
		if (null == id) {
			throw new BaseException("id should not be null");
		}

		// 根据ID查询任务信息
		CuckooJobDetail orginJobDetail = cuckooJobDetailMapper.selectByPrimaryKey(id);
		orginJobDetail.setJobStatus(CuckooJobStatus.RUNNING.getValue());

		// 更新cuckoo状态
		cuckooJobDetailMapper.updateByPrimaryKeySelective(orginJobDetail);

		// 更新quartz任务状态
		if (CuckooJobTriggerType.CRON.getValue().equals(orginJobDetail.getTriggerType()) ) {
			quartzManage.resumeJob(String.valueOf(orginJobDetail.getGroupId()), String.valueOf(orginJobDetail.getId()));
		}

	}

	@Override
	public void resumeAllJob() {
		
		quartzManage.resumeAll();
	}

//	@Override
//	public void pendingDailyJob(Long id, Boolean needTriggleNext, Integer txDate) {
//
//		// 根据ID查询Cuckoo
//		if (null == id) {
//			throw new BaseException("id should not be null");
//		}
//
//		// 根据ID查询任务信息
//		CuckooJobDetail orginJobDetail = cuckooJobDetailMapper.selectByPrimaryKey(id);
//		if(null == orginJobDetail){
//			throw new BaseException("can not find job by id:" + id);
//		}
//		
//		// 日切任务 -- 放到PENDING任务队列中
//		orginJobDetail.setExecJobStatus(CuckooJobExecStatus.PENDING.getValue());
//		orginJobDetail.setTxDate(txDate);
//		orginJobDetail.setNeedTriggleNext(needTriggleNext);
//		cuckooJobDetailMapper.updateByPrimaryKeySelective(orginJobDetail);
//
//		LOGGER.info("pending daily Job,{}", orginJobDetail);
//		// 使用Quartz.simpleJob进行触发 
//		quartzManage.addSimpleJob(String.valueOf(orginJobDetail.getGroupId()), String.valueOf(orginJobDetail.getId()));
//	}
	

	
//	@Override
//	public void pendingUnDailyJob(Long id, Boolean needTriggleNext, Long startTime, Long endTime) {
//		
//		if (null == id) {
//			throw new BaseException("id should not be null");
//		}
//
//		// 根据ID查询任务信息
//		CuckooJobDetail cuckooJobDetails = cuckooJobDetailMapper.selectByPrimaryKey(id);
//		if(null == cuckooJobDetails){
//
//			throw new BaseException("can not find job by id:" + id);
//		}
//		// 非日切任务 -- 放到PENDING任务队列中
//		cuckooJobDetails.setExecJobStatus(CuckooJobExecStatus.PENDING.getValue());
//		cuckooJobDetails.setFlowLastTime(startTime);
//		cuckooJobDetails.setFlowCurTime(endTime);
//		cuckooJobDetails.setNeedTriggleNext(needTriggleNext);
//		cuckooJobDetailMapper.updateByPrimaryKeySelective(cuckooJobDetails);
//		
//		LOGGER.info("pending UnDaily Job,{}", cuckooJobDetails);
//		// 使用Quartz.simpleJob进行触发 
//		quartzManage.addSimpleJob(String.valueOf(cuckooJobDetails.getGroupId()), String.valueOf(cuckooJobDetails.getId()));
//	}

	


	

	@Override
	public CuckooJobDetail getJobById(Long jobId) {

		return cuckooJobDetailMapper.selectByPrimaryKey(jobId);
	}

	
	 
	@Override
	public List<CuckooJobDetail> getNextJobById(Long jobId) {

		List<Long> nextJobids = cuckooJobNextService.findNextJobIdByJobId(jobId);
		if(CollectionUtils.isEmpty(nextJobids)){
			return new ArrayList<>(0);
		}
		CuckooJobDetailCriteria jobCrt = new CuckooJobDetailCriteria();
		jobCrt.createCriteria().andIdIn(nextJobids);
		
		return cuckooJobDetailMapper.selectByExample(jobCrt);
	}

	@Override
	public PageDataList<CuckooJobDetail> pageList(JobInfoQry jobInfo, Integer start, Integer limit) {
		
		
		CuckooJobDetailCriteria crt = new CuckooJobDetailCriteria();
		CuckooJobDetailCriteria.Criteria exp = crt.createCriteria();
		if(null != jobInfo.getGroupId()){
			exp.andGroupIdEqualTo(jobInfo.getGroupId());
		}
		if(null != jobInfo.getJobId()){
			exp.andIdEqualTo(jobInfo.getJobId());
		}
		if(StringUtils.isNotEmpty(jobInfo.getJobClassApplication())){
			exp.andJobClassApplicationEqualTo(jobInfo.getJobClassApplication());
		}
		if(StringUtils.isNotEmpty(jobInfo.getJobStatus())){
			exp.andJobStatusEqualTo(jobInfo.getJobStatus());
		}
		crt.setStart(start);
		crt.setLimit(limit);
		

		return cuckooJobDetailMapper.pageByExample(crt);
	}

	@Override
	public Map<String,String> findAllApps() {
		
		List<CuckooJobDetail> jobs = cuckooJobDetailMapper.selectByExample(new CuckooJobDetailCriteria());
		List<String> jobApps = PropertyUtil.fetchFieldList(jobs, "jobClassApplication");
		Map<String, String> rtn = new LinkedHashMap<>();
		if(CollectionUtils.isNotEmpty(jobApps)){
			for (String jobApp : jobApps) {
				rtn.put(jobApp, jobApp);
			}
		}
		return rtn;
	}
	
	
	
	

	@Override
	public void pendingJob(CuckooJobDetail jobDetail, CuckooJobExecLog fatherJobLog, JobDataMap data){
		
		LOGGER.info("add pending job ,jobDetail:{} , fatherJobLog:{}", jobDetail , fatherJobLog);
		CuckooJobExecLog jobLog = new CuckooJobExecLog();
		Long curTime =  System.currentTimeMillis();
		// 初始化任务日志信息
		PropertyUtil.copyProperties(jobLog, jobDetail);
		jobLog.setId(null);
		jobLog.setJobId(jobDetail.getId());
		jobLog.setJobStartTime(curTime);
		jobLog.setExecJobStatus(CuckooJobExecStatus.PENDING.getValue());
		jobLog.setLatestCheckTime(curTime);
		jobLog.setNeedTriggleNext(fatherJobLog.getNeedTriggleNext());
		jobLog.setForceTriggle(false);
		jobLog.setTxDate(fatherJobLog.getTxDate());
		jobLog.setFlowLastTime(fatherJobLog.getFlowLastTime());
		jobLog.setFlowCurTime(fatherJobLog.getFlowCurTime());
		
		cuckooJobExecLogMapper.insertSelective(jobLog);
		jobLog.setId(cuckooJobExecLogMapper.lastInsertId());
		// 使用Quartz.simpleJob进行触发 
		quartzManage.addSimpleJob(jobLog);

	}
	
	

	@Override
	public void rependingJob(CuckooJobExecLog jobLog) {
		LOGGER.info("repending job ,jobLog:{} ", jobLog);
		// 使用Quartz.simpleJob进行触发 
		quartzManage.addSimpleJob(jobLog);

	}
	

	@Override
	public void triggerUnDailyJob(Long jobId, Boolean needTriggleNext, Long lastTime, Long curTime) {

		CuckooJobDetail cuckooJobDetail = cuckooJobDetailMapper.selectByPrimaryKey(jobId);
		
		if(null ==  cuckooJobDetail){
			throw new BaseException("can not get jobinfo by id:{}", jobId);
		}
		CuckooJobExecLog jobLog = cuckooJobLogService.initUnDailyJobLog(cuckooJobDetail,  needTriggleNext, lastTime, curTime);
		quartzManage.addSimpleJob(jobLog);

		
	}

	@Override
	public void triggerDailyJob(Long jobId, Boolean needTriggleNext, Integer txDate) {
		

		CuckooJobDetail cuckooJobDetail = cuckooJobDetailMapper.selectByPrimaryKey(jobId);
		if(null ==  cuckooJobDetail){
			throw new BaseException("can not get jobinfo by id:{}", jobId);
		}
		
		CuckooJobExecLog jobLog = cuckooJobLogService.initDailyJobLog(cuckooJobDetail, needTriggleNext, txDate);
		quartzManage.addSimpleJob(jobLog);
	}




}
