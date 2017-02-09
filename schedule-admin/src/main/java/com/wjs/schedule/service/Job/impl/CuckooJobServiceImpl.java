package com.wjs.schedule.service.Job.impl;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wjs.schedule.bean.JobInfoBean;
import com.wjs.schedule.component.cuckoo.CuckooJobExecutor;
import com.wjs.schedule.component.quartz.QuartzExec;
import com.wjs.schedule.constant.CuckooJobConstant;
import com.wjs.schedule.dao.exec.CuckooJobDetailsMapper;
import com.wjs.schedule.dao.exec.CuckooJobGroupMapper;
import com.wjs.schedule.dao.exec.CuckooLocksMapper;
import com.wjs.schedule.domain.exec.CuckooJobDetails;
import com.wjs.schedule.domain.exec.CuckooJobDetailsCriteria;
import com.wjs.schedule.domain.exec.CuckooJobGroup;
import com.wjs.schedule.domain.exec.CuckooLocks;
import com.wjs.schedule.domain.exec.CuckooLocksCriteria;
import com.wjs.schedule.enums.CuckooJobExecStatus;
import com.wjs.schedule.enums.CuckooJobStatus;
import com.wjs.schedule.enums.CuckooJobTriggerType;
import com.wjs.schedule.exception.BaseException;
import com.wjs.schedule.service.Job.CuckooJobLogService;
import com.wjs.schedule.service.Job.CuckooJobNextService;
import com.wjs.schedule.service.Job.CuckooJobService;
import com.wjs.schedule.util.CuckBeanUtil;
import com.wjs.schedule.vo.job.JobInfo;

@Service("cuckooJobService")
public class CuckooJobServiceImpl implements CuckooJobService{



	private static final Logger LOGGER = LoggerFactory.getLogger(CuckooJobServiceImpl.class);
	@Autowired
	CuckooJobGroupMapper cuckooJobGroupMapper;
	
	@Autowired
	CuckooJobDetailsMapper cuckooJobDetailsMapper;
	
	@Autowired
	CuckooLocksMapper cuckooLocksMapper;
	
	@Autowired
	CuckooJobExecutor cuckooJobExecutor;
	
	@Autowired
	QuartzExec quartzExec;
	
	@Autowired
	CuckooJobNextService cuckooJobNextService;
	
	@Autowired
	CuckooJobLogService cuckooJobLogService;
	
	@Override
	@Transactional
	public Long addJob(JobInfo jobInfo) {
		
		if(null == jobInfo || null == jobInfo.getGroupId()){
			
			throw new BaseException("jobInfo should not be null");
		}
		// 校验分组是否存在
		CuckooJobGroup cuckooJobGroup = cuckooJobGroupMapper.selectByPrimaryKey(jobInfo.getGroupId());
		if(null == cuckooJobGroup){
			throw new BaseException("can not find jobgroup by groupId:"+jobInfo.getGroupId());
		}
		// 校验任务名称是否重复
//		CuckooJobDetailsCriteria jobNameCrt = new CuckooJobDetailsCriteria();
//		jobNameCrt.createCriteria().andGroupIdEqualTo(jobInfo.getGroupId()).andJobNameEqualTo(jobInfo.getJobName());
//		List<CuckooJobDetails> jobNameChecks = cuckooJobDetailsMapper.selectByExample(jobNameCrt);
//		if(CollectionUtils.isNotEmpty(jobNameChecks)){
//			throw new BaseException("job name has already exists:" + jobInfo.getJobName());
//		}
		// 如果是cron，校验cron是否正确
		if(CuckooJobTriggerType.CRON.getValue().equals(jobInfo.getTriggerType()) || CuckooJobTriggerType.DAILY.getValue().equals(jobInfo.getTriggerType()) ){
			jobInfo.setCronExpression(StringUtils.trim(jobInfo.getCronExpression()));
			if(!CronExpression.isValidExpression(jobInfo.getCronExpression())){

				throw new BaseException("cronexpression is not valid:"+jobInfo.getCronExpression());
			}
		}
		// 新增wjs_schedule_cockoo_job_details 数据，默认暂停
		CuckooJobDetails cuckooJobDetails = CuckBeanUtil.parseJob(jobInfo);
		cuckooJobDetails.setJobStatus(CuckooJobStatus.PAUSE.getValue());
		cuckooJobDetails.setExecJobStatus(CuckooJobExecStatus.SUCCED.getValue());
		cuckooJobDetailsMapper.insert(cuckooJobDetails);
		Long jobId = cuckooJobDetailsMapper.lastInsertId();
		if(jobId == null){

			throw new BaseException("cuckoo_job_details insert error,can not get autoincriment id");
		}
		
		// 如果是cron类型的，调用quartzAPI,新增任务，并且设置任务为暂停
		if(CuckooJobTriggerType.CRON.getValue().equals(jobInfo.getTriggerType()) || CuckooJobTriggerType.DAILY.getValue().equals(jobInfo.getTriggerType())){
			quartzExec.addCronJob(String.valueOf(jobInfo.getGroupId()), String.valueOf(jobId), jobInfo.getCronExpression(), CuckooJobStatus.fromName(jobInfo.getJobStatus()));
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
		CuckooJobDetails cuckooJobDetail = cuckooJobDetailsMapper.selectByPrimaryKey(id);
		// 根据id删除cuckoo数据
		if(null != cuckooJobDetail){
			cuckooJobDetailsMapper.deleteByPrimaryKey(id);
			// 根据任务信息删除quartz信息
			quartzExec.deleteJob(String.valueOf(cuckooJobDetail.getGroupId()), String.valueOf(cuckooJobDetail.getId()));
		}
			
	}

	@Override
	@Transactional
	public void modifyJob(JobInfo jobInfo) {
		
		if(null == jobInfo || null == jobInfo.getId()){
			throw new BaseException("jobinfo should not be null");
		}

		// 根据ID查询任务信息
		CuckooJobDetails orginJobDetail = cuckooJobDetailsMapper.selectByPrimaryKey(jobInfo.getId());

		if(null == orginJobDetail){
			throw new BaseException("can not find jobinfo by id : " + jobInfo.getId());
		}
		CuckooJobDetails targetJobDetail = CuckBeanUtil.parseJob(jobInfo);
		
		// 修改cuckoo任务
		if(orginJobDetail.getGroupId().equals(targetJobDetail.getGroupId())){
			// 如果原始任务组编号与目前任务组编号相同
			cuckooJobDetailsMapper.updateByPrimaryKeySelective(targetJobDetail);
			if(CuckooJobTriggerType.FLOW.getValue().equals(orginJobDetail.getTriggerType()) ){
				// 原来任务类型为Flow
				if(CuckooJobTriggerType.CRON.getValue().equals(targetJobDetail.getTriggerType()) || CuckooJobTriggerType.DAILY.getValue().equals(targetJobDetail.getTriggerType()) ){
					// 且新任务为Cron，那么需要新增quartz。否则不做处理
					quartzExec.addCronJob(String.valueOf(targetJobDetail.getGroupId()), String.valueOf(targetJobDetail), jobInfo.getCronExpression(), CuckooJobStatus.fromName(targetJobDetail.getJobStatus()));
				}
				
			}else if(CuckooJobTriggerType.CRON.getValue().equals(orginJobDetail.getTriggerType()) || CuckooJobTriggerType.DAILY.getValue().equals(orginJobDetail.getTriggerType()) ){
				// 如果原来任务类型为Cron，那么修改一条任务
				if(CuckooJobTriggerType.CRON.getValue().equals(targetJobDetail.getTriggerType()) || CuckooJobTriggerType.DAILY.getValue().equals(targetJobDetail.getTriggerType()) ){
					// 且新任务为Cron，那么需要修改quartz
					quartzExec.modfyCronJob(String.valueOf(targetJobDetail.getGroupId()), String.valueOf(targetJobDetail), jobInfo.getCronExpression(), CuckooJobStatus.fromName(targetJobDetail.getJobStatus()));
				}else{
					// 且新任务为FLOW，那么需要删除quartz
					quartzExec.deleteJob(String.valueOf(orginJobDetail.getGroupId()), String.valueOf(orginJobDetail.getJobName()));
				}
			}else {
				throw new BaseException("unknow job triggle type : "+ jobInfo.getTriggerType());
			}
		}else{
			// 如果原始任务组编号与目前任务组编号不同
			// 删除任务
			quartzExec.deleteJob(String.valueOf(orginJobDetail.getGroupId()), String.valueOf(orginJobDetail.getJobName()));
			// 新增任务
			if(CuckooJobTriggerType.CRON.getValue().equals(targetJobDetail.getTriggerType()) || CuckooJobTriggerType.DAILY.getValue().equals(targetJobDetail.getTriggerType()) ){
				// 且新任务为Cron，那么需要新增quartz。否则不做处理
				quartzExec.addCronJob(String.valueOf(targetJobDetail.getGroupId()), String.valueOf(targetJobDetail), jobInfo.getCronExpression(), CuckooJobStatus.fromName(targetJobDetail.getJobStatus()));
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
		CuckooJobDetails orginJobDetail = cuckooJobDetailsMapper.selectByPrimaryKey(id);
		orginJobDetail.setJobStatus(CuckooJobStatus.PAUSE.getValue());
		
		// 更新cuckoo状态
		cuckooJobDetailsMapper.updateByPrimaryKeySelective(orginJobDetail);
		
		// 更新quartz任务状态
		if(CuckooJobTriggerType.CRON.getValue().equals(orginJobDetail.getTriggerType()) || CuckooJobTriggerType.DAILY.getValue().equals(orginJobDetail.getTriggerType())){
			quartzExec.pauseJob(String.valueOf(orginJobDetail.getGroupId()),String.valueOf(orginJobDetail.getId()));
		}
	}

	@Override
	public void pauseAllJob() {

		// 更新cuckoo状态
		CuckooJobDetails orginJobDetail = new CuckooJobDetails();
		orginJobDetail.setJobStatus(CuckooJobStatus.PAUSE.getValue());
		cuckooJobDetailsMapper.updateByExampleSelective(orginJobDetail, new CuckooJobDetailsCriteria());
		
		// 更新quartz任务状态
		quartzExec.pauseAll();
	}

	@Override
	public void resumeOnejob(Long id) {

		// 根据ID查询Cuckoo
		if (null == id) {
			throw new BaseException("id should not be null");
		}

		// 根据ID查询任务信息
		CuckooJobDetails orginJobDetail = cuckooJobDetailsMapper.selectByPrimaryKey(id);
		orginJobDetail.setJobStatus(CuckooJobStatus.RUNNING.getValue());

		// 更新cuckoo状态
		cuckooJobDetailsMapper.updateByPrimaryKeySelective(orginJobDetail);

		// 更新quartz任务状态
		if (CuckooJobTriggerType.CRON.getValue().equals(orginJobDetail.getTriggerType()) || CuckooJobTriggerType.DAILY.getValue().equals(orginJobDetail.getTriggerType())) {
			quartzExec.resumeJob(String.valueOf(orginJobDetail.getGroupId()), String.valueOf(orginJobDetail.getId()));
		}

	}

	@Override
	public void resumeAllJob() {
		
		quartzExec.resumeAll();
	}

	@Override
	public void forceTriggleCronJob(Long id, Boolean needTrigglerNext, Integer tx_date) {

		// 根据ID查询Cuckoo
		if (null == id) {
			throw new BaseException("id should not be null");
		}

		// 根据ID查询任务信息
		CuckooJobDetails orginJobDetail = cuckooJobDetailsMapper.selectByPrimaryKey(id);
		if(null == orginJobDetail){
			throw new BaseException("can not find job by id:" + id);
		}
		// 触发Cron任务
		quartzExec.triggerJob(String.valueOf(orginJobDetail.getGroupId()), String.valueOf(orginJobDetail.getId()), true, needTrigglerNext, tx_date);

	}

	@Override
	public void forceTriggleFlowJob(Long id, Boolean needTrigglerNext, Long startTime, Long endTime) {
		
		if (null == id) {
			throw new BaseException("id should not be null");
		}

		// 根据ID查询任务信息
		CuckooJobDetails cuckooJobDetails = cuckooJobDetailsMapper.selectByPrimaryKey(id);
		if(null == cuckooJobDetails){

			throw new BaseException("can not find job by id:" + id);
		}
		cuckooJobExecutor.executeFlowJob(cuckooJobDetails, true, needTrigglerNext, startTime, endTime);
	}

	@Override
	public void updateJobStatusById(Long jobId, CuckooJobExecStatus execStatus) {

		CuckooJobDetails cuckooJobDetails = new CuckooJobDetails();
		cuckooJobDetails.setId(jobId);
		cuckooJobDetails.setExecJobStatus(execStatus.getValue());
		cuckooJobDetailsMapper.updateByPrimaryKey(cuckooJobDetails);
		
	}

	@Override
	public CuckooJobDetails getJobById(Long jobId) {

		return cuckooJobDetailsMapper.selectByPrimaryKey(jobId);
	}

	@Override
	public void tryTrigglePendingJob() {
		
//		1.通过Lock表进行加锁pending任务锁，
		CuckooLocksCriteria lockCrt = new CuckooLocksCriteria();
		lockCrt.createCriteria().andLockNameEqualTo(CuckooJobConstant.LOCK_NAME_PENDING_JOB);
		CuckooLocks locks = cuckooLocksMapper.lockByExample(lockCrt);
		if(null == locks){
//			如果没有锁，那么insert一条记录，如果是异常的话（唯一索引造成），需要catch
			CuckooLocks newLock = new CuckooLocks();
			try {
				newLock.setCuckooServerIp(InetAddress.getLocalHost().getHostAddress());
				newLock.setLockName(CuckooJobConstant.LOCK_NAME_PENDING_JOB);
				cuckooLocksMapper.insertSelective(newLock);
			} catch (Exception e) {
				LOGGER.error("add new pending lock error:{}",e.getMessage() ,e);
			}
		}
//		存在锁记录继续
		
//		4.查询任务表中状态为pending的任务，获取任务执行
		CuckooJobDetailsCriteria jobCrt = new CuckooJobDetailsCriteria();
		jobCrt.createCriteria().andExecJobStatusEqualTo(CuckooJobExecStatus.PENDING.getValue());
		List<CuckooJobDetails> pendingJobs = cuckooJobDetailsMapper.selectByExample(jobCrt);
		if(CollectionUtils.isNotEmpty(pendingJobs)){
			for (CuckooJobDetails cuckooJobDetails : pendingJobs) {
				// 当前任务pending的任务为被触发的任务，查询依赖任务执行情况，参数取自触发任务的参数
				JobInfoBean jobInfoBean = extractJobparam(cuckooJobDetails);
				cuckooJobExecutor.executeCurJob(jobInfoBean);
			}
		}
	}

	/**
	 * 当前任务pending的任务为被触发的任务，参数取自触发任务的参数
	 * @param cuckooJobDetails
	 * @return
	 */
	private JobInfoBean extractJobparam(CuckooJobDetails cuckooJobDetails) {
		JobInfoBean jobInfo = new JobInfoBean();
		
		jobInfo.setCuckooParallelJobArgs(cuckooJobDetails.getCuckooParallelJobArgs());
		jobInfo.setForceJob(false);
		jobInfo.setJobId(cuckooJobDetails.getId());
		jobInfo.setJobName(cuckooJobDetails.getJobName());
		jobInfo.setTriggerType(CuckooJobTriggerType.fromName(cuckooJobDetails.getTriggerType()));
		
		// 查询被触发任务信息
		Long preJobId = cuckooJobNextService.findJobIdByNextJobId(cuckooJobDetails.getId());
		if(null ==  preJobId){
			return null;
		}
		CuckooJobDetails preJobInfo = getJobById(preJobId);
		jobInfo.setFlowCurrTime(preJobInfo.getFlowCurTime());
		jobInfo.setFlowLastTime(preJobInfo.getFlowLastTime());
		jobInfo.setNeedTrigglerNext(cuckooJobLogService.getJobNeedTriglerByJobInfo(preJobInfo));
		jobInfo.setTxDate(preJobInfo.getTxDate());
		
		
		return jobInfo;
	}

	@Override
	public List<CuckooJobDetails> getNextJobById(Long jobId) {

		List<Long> nextJobids = cuckooJobNextService.findNextJobIdByJobId(jobId);
		if(CollectionUtils.isEmpty(nextJobids)){
			return new ArrayList<>(0);
		}
		CuckooJobDetailsCriteria jobCrt = new CuckooJobDetailsCriteria();
		jobCrt.createCriteria().andIdIn(nextJobids);
		
		return cuckooJobDetailsMapper.selectByExample(jobCrt);
	}



}
