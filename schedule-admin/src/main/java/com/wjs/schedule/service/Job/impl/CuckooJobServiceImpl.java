package com.wjs.schedule.service.Job.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wjs.schedule.component.cuckoo.CuckooJobExecutor;
import com.wjs.schedule.component.quartz.QuartzManage;
import com.wjs.schedule.dao.exec.CuckooClientJobDetailMapper;
import com.wjs.schedule.dao.exec.CuckooJobDetailMapper;
import com.wjs.schedule.dao.exec.CuckooJobExecLogMapper;
import com.wjs.schedule.dao.exec.CuckooJobGroupMapper;
import com.wjs.schedule.domain.exec.CuckooClientJobDetail;
import com.wjs.schedule.domain.exec.CuckooClientJobDetailCriteria;
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
	
	@Autowired
	CuckooClientJobDetailMapper cuckooClientJobDetailMapper;
	
	@Override
	@Transactional
	public Long addJob(CuckooJobDetailVo jobDetail) {
		
		if(null == jobDetail || null == jobDetail.getGroupId()){
			
			throw new BaseException("jobInfo should not be null");
		}
		// 校验分组是否存在
		CuckooJobGroup cuckooJobGroup = cuckooJobGroupMapper.selectByPrimaryKey(jobDetail.getGroupId());
		if(null == cuckooJobGroup){
			throw new BaseException("can not find jobgroup by groupId:"+jobDetail.getGroupId());
		}
		
		// 检查唯一性索引 groupid,jobName
		CuckooJobDetailCriteria jobUkCrt = new CuckooJobDetailCriteria();
		jobUkCrt.createCriteria().andGroupIdEqualTo(jobDetail.getGroupId())
		.andJobNameEqualTo(jobDetail.getJobName());
		List<CuckooJobDetail> jobUkCheck = cuckooJobDetailMapper.selectByExample(jobUkCrt);
		if(CollectionUtils.isNotEmpty(jobUkCheck)){
			throw new BaseException("job has aready added ,groupId:{},jobName:{}", jobDetail.getGroupId() , jobDetail.getJobName());
		}
		
		// 如果是cron，校验cron是否正确
		if(CuckooJobTriggerType.CRON.getValue().equals(jobDetail.getTriggerType())){
			jobDetail.setCronExpression(StringUtils.trim(jobDetail.getCronExpression()));
			if(!CronExpression.isValidExpression(jobDetail.getCronExpression())){

				throw new BaseException("cronexpression is not valid:"+jobDetail.getCronExpression());
			}
		}
		// 新增wjs_schedule_cockoo_job_details 数据，默认暂停

		
		

		CuckooJobDetail cuckooJobDetail = new CuckooJobDetail();
		PropertyUtil.copyProperties(cuckooJobDetail, jobDetail);
		// 默认执行
		cuckooJobDetail.setJobStatus(CuckooJobStatus.RUNNING.getValue());
		cuckooJobDetailMapper.insertSelective(cuckooJobDetail);
		Long jobId = cuckooJobDetailMapper.lastInsertId();
		if(jobId == null){
			throw new BaseException("cuckoo_job_details insert error,can not get autoincriment id");
		}
		
		if(null != jobDetail.getPreJobId()){
			// 触发任务
			CuckooJobDetail jobPreTriggle = getJobById(jobDetail.getPreJobId());
			if(null == jobPreTriggle){
				throw new BaseException("can not find pre trigger job by preJobId:{}", jobDetail.getPreJobId());
			}
			cuckooJobNextService.add(jobDetail.getPreJobId(), jobId);
		}
		
		if(StringUtils.isNotEmpty(jobDetail.getDependencyIds())){
			// 依赖任务 TODO
		}
		
		
		if(CuckooJobTriggerType.CRON.getValue().equals(jobDetail.getTriggerType())){
			quartzManage.addCronJob(String.valueOf(jobDetail.getGroupId()), String.valueOf(jobId), jobDetail.getCronExpression(), CuckooJobStatus.fromName(jobDetail.getJobStatus()));
		}
		
		return jobId;
		
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
		CuckooJobDetail targetJobDetail = new CuckooJobDetail();

		PropertyUtil.copyProperties(targetJobDetail, jobInfo);
		
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
		
		
		// 任务触发关系修改 TODO
		
		// 任务依赖关系修改 TODO
		
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
		
		// 日志删除（暂时保留日志）
			
	}


	@Override
	@Transactional
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
	@Transactional
	public void pauseAllJob() {
		
		// 不能使用quartzManage.pauseAll()，该方法会将后续增加的任务或者强制执行的SimpleTrigger也锁住，此处对任务循环进行锁定
		CuckooJobDetailCriteria crt = new CuckooJobDetailCriteria();
		crt.createCriteria().andJobStatusEqualTo(CuckooJobStatus.RUNNING.getValue());
		crt.setOrderByClause("id asc ");
		
		List<CuckooJobDetail> jobs = cuckooJobDetailMapper.selectByExample(crt);
		if(CollectionUtils.isNotEmpty(jobs)){
			for (CuckooJobDetail cuckooJobDetail : jobs) {
				// 更新cuckoo状态
				cuckooJobDetail.setJobStatus(CuckooJobStatus.PAUSE.getValue());
				cuckooJobDetailMapper.updateByPrimaryKeySelective(cuckooJobDetail);
				// 更新quartz任务状态
				if(CuckooJobTriggerType.CRON.getValue().equals(cuckooJobDetail.getTriggerType())){
					quartzManage.pauseJob(String.valueOf(cuckooJobDetail.getGroupId()),String.valueOf(cuckooJobDetail.getId()));
				}
			}
		}
	}

	@Override
	@Transactional
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
	@Transactional
	public void resumeAllJob() {
		
		// 更新cuckoo状态
		CuckooJobDetail orginJobDetail = new CuckooJobDetail();
		orginJobDetail.setJobStatus(CuckooJobStatus.RUNNING.getValue());
		cuckooJobDetailMapper.updateByExampleSelective(orginJobDetail, new CuckooJobDetailCriteria());

		quartzManage.resumeAll();
	}

	
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
		
		List<CuckooClientJobDetail> jobs = cuckooClientJobDetailMapper.selectByExample(new CuckooClientJobDetailCriteria());
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
	public void pendingJob(CuckooJobDetail jobDetail, CuckooJobExecLog fatherJobLog){
		
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
		jobLog.setForceTriggle(fatherJobLog.getForceTriggle());
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
	public void triggerUnDailyJob(Long jobId, Boolean needTriggleNext, Long lastTime, Long curTime, boolean foreTriggle) {

		CuckooJobDetail cuckooJobDetail = cuckooJobDetailMapper.selectByPrimaryKey(jobId);
		
		if(null ==  cuckooJobDetail){
			throw new BaseException("can not get jobinfo by id:{}", jobId);
		}
		CuckooJobExecLog jobLog = cuckooJobLogService.initUnDailyJobLog(cuckooJobDetail,  needTriggleNext, lastTime, curTime, foreTriggle);
		quartzManage.addSimpleJob(jobLog);

		
	}

	@Override
	public void triggerDailyJob(Long jobId, Boolean needTriggleNext, Integer txDate, boolean foreTriggle) {
		

		CuckooJobDetail cuckooJobDetail = cuckooJobDetailMapper.selectByPrimaryKey(jobId);
		if(null ==  cuckooJobDetail){
			throw new BaseException("can not get jobinfo by id:{}", jobId);
		}
		
		CuckooJobExecLog jobLog = cuckooJobLogService.initDailyJobLog(cuckooJobDetail, needTriggleNext, txDate, foreTriggle);
		quartzManage.addSimpleJob(jobLog);
	}



}
