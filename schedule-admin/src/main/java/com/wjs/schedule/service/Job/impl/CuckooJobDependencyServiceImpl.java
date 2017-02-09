package com.wjs.schedule.service.Job.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wjs.schedule.constant.CuckooJobConstant;
import com.wjs.schedule.dao.exec.CuckooJobDependencyMapper;
import com.wjs.schedule.dao.exec.CuckooJobDetailsMapper;
import com.wjs.schedule.dao.exec.CuckooJobExecLogsMapper;
import com.wjs.schedule.domain.exec.CuckooJobDependency;
import com.wjs.schedule.domain.exec.CuckooJobDependencyCriteria;
import com.wjs.schedule.domain.exec.CuckooJobDetails;
import com.wjs.schedule.domain.exec.CuckooJobDetailsCriteria;
import com.wjs.schedule.domain.exec.CuckooJobExecLogs;
import com.wjs.schedule.domain.exec.CuckooJobExecLogsCriteria;
import com.wjs.schedule.enums.CuckooJobExecStatus;
import com.wjs.schedule.enums.CuckooJobTriggerType;
import com.wjs.schedule.exception.BaseException;
import com.wjs.schedule.exception.JobDependencyException;
import com.wjs.schedule.service.Job.CuckooJobDependencyService;
import com.wjs.schedule.util.CuckBeanUtil;
import com.wjs.schedule.vo.job.JobDependency;
import com.wjs.util.bean.PropertyUtil;

@Service("cuckooJobDependencyService")
public class CuckooJobDependencyServiceImpl implements CuckooJobDependencyService {


	private static final Logger LOGGER = LoggerFactory.getLogger(CuckooJobDependencyServiceImpl.class);


	@Autowired
	CuckooJobDependencyMapper cuckooJobDependencyMapper;
	
	@Autowired
	CuckooJobDetailsMapper cuckooJobDetailsMapper;
	
	@Autowired
	CuckooJobExecLogsMapper cuckooJobExecLogsMapper;
	
	@Override
	@Transactional
	public void setDependencyJobConfig(List<JobDependency> dependencyJobs) {

		if(CollectionUtils.isEmpty(dependencyJobs)){
			
			throw new BaseException("dependency jobs should not be empty : ");
		}
		// 先删除触发
		CuckooJobDependencyCriteria curJobCrt = new CuckooJobDependencyCriteria();
		curJobCrt.createCriteria().andIdEqualTo(dependencyJobs.get(0).getJobId());
		cuckooJobDependencyMapper.deleteByExample(curJobCrt);
		
		// 再增加触发
		for (JobDependency jobDependency : dependencyJobs) {
			CuckooJobDependency cuckooJobDependency = CuckBeanUtil.parseJobDependency(jobDependency);
			cuckooJobDependencyMapper.insertSelective(cuckooJobDependency);
		}
	}

	@Override
	public void checkDepedencyJobFinished(CuckooJobDetails jobInfo, JobDataMap data) throws JobDependencyException {
		

		CuckooJobDependencyCriteria depJobMapCrt = new CuckooJobDependencyCriteria();
		depJobMapCrt.createCriteria().andJobIdEqualTo(jobInfo.getId());
		List<CuckooJobDependency> depJobMaps = cuckooJobDependencyMapper.selectByExample(depJobMapCrt);
		List<Long> depJobIds = PropertyUtil.fetchFieldList(depJobMaps, "dependencyJobId");
		if(CollectionUtils.isEmpty(depJobIds)){
			return;
		}
		if(CuckooJobTriggerType.FLOW.getValue().equals(jobInfo.getTriggerType()) || CuckooJobTriggerType.CRON.getValue().equals(jobInfo.getTriggerType())){

			if(CollectionUtils.isNotEmpty(depJobIds)){

				// 如果非日切任务，那么需要检查每个依赖任务的执行状态
				CuckooJobDetailsCriteria depJobCrt = new CuckooJobDetailsCriteria();
				depJobCrt.createCriteria().andIdIn(depJobIds)
				.andExecJobStatusNotEqualTo(CuckooJobExecStatus.SUCCED.getValue());
				List<CuckooJobDetails> depJobs = cuckooJobDetailsMapper.selectByExample(depJobCrt);
				if(CollectionUtils.isNotEmpty(depJobs)){
					LOGGER.info("dependency has not succed yet ,job{}-{}:jobs:{}", jobInfo.getId(), jobInfo.getJobName(), depJobs);
					throw new BaseException("dependency has not succed yet ,job{}-{}:jobs:{}", jobInfo.getId(), jobInfo.getJobName(), depJobs);
				}
			}
			
			
		}else if(CuckooJobTriggerType.DAILY.getValue().equals(jobInfo.getTriggerType())){

			if(CollectionUtils.isNotEmpty(depJobIds)){
				
				Object txdate = data.get(CuckooJobConstant.DAILY_JOB_TXDATE);
				if(null == txdate){
					LOGGER.info("daily job exec has no txdate, jobInfo:{}", jobInfo);
					throw new BaseException("daily job exec has no txdate, jobInfo:{}", jobInfo);
				}
				// 如果是日切任务，那么需要检查每个依赖任务对应的bizDate的执行状态

				// 检查依赖任务的执行日期是否一致
				CuckooJobExecLogsCriteria depLogTxdateCrt = new CuckooJobExecLogsCriteria();
				depLogTxdateCrt.createCriteria().andIdIn(depJobIds)
				.andTxDateEqualTo(Integer.valueOf(String.valueOf(txdate)));
				List<CuckooJobExecLogs> depLogTxJobs = cuckooJobExecLogsMapper.selectByExample(depLogTxdateCrt);
				if(CollectionUtils.isNotEmpty(depLogTxJobs)){
					LOGGER.error("dependency has txdate not the same ,job:{}-{},txdate:{},depjobs:{}", jobInfo.getId(), jobInfo.getJobName(), txdate, depLogTxJobs);
					throw new JobDependencyException("dependency has not succed yet ,job:{}-{},txdate:{},txdate:{},depjobs:{}", jobInfo.getId(), jobInfo.getJobName(), txdate, depLogTxJobs);
				}
				
				// 检查当天任务执行状况
				CuckooJobExecLogsCriteria depLogExecCrt = new CuckooJobExecLogsCriteria();
				depLogExecCrt.createCriteria().andIdIn(depJobIds)
				.andExecJobStatusNotEqualTo(CuckooJobExecStatus.SUCCED.getValue())
				.andTxDateEqualTo(Integer.valueOf(String.valueOf(txdate)));
				List<CuckooJobExecLogs> depLogExecJobs = cuckooJobExecLogsMapper.selectByExample(depLogExecCrt);
				if(CollectionUtils.isNotEmpty(depLogExecJobs)){
					LOGGER.info("dependency has not succed yet ,job:{}-{},txdate:{},depjobs:{}", jobInfo.getId(), jobInfo.getJobName(), txdate, depLogExecJobs);
					throw new BaseException("dependency has not succed yet ,job:{}-{},txdate:{},txdate:{},depjobs:{}", jobInfo.getId(), jobInfo.getJobName(), txdate, depLogExecJobs);
				}
			}
			
		}else{
			
			throw new BaseException("unknow triggle type in check depedency,jobInfo:{}", jobInfo);
		}
		
		
	}

}
