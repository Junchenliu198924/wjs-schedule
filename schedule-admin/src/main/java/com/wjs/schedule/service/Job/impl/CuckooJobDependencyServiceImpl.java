package com.wjs.schedule.service.Job.impl;

import java.util.ArrayList;
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
import com.wjs.schedule.dao.exec.CuckooJobDetailMapper;
import com.wjs.schedule.dao.exec.CuckooJobExecLogMapper;
import com.wjs.schedule.domain.exec.CuckooJobDependency;
import com.wjs.schedule.domain.exec.CuckooJobDependencyCriteria;
import com.wjs.schedule.domain.exec.CuckooJobDetail;
import com.wjs.schedule.domain.exec.CuckooJobDetailCriteria;
import com.wjs.schedule.domain.exec.CuckooJobExecLog;
import com.wjs.schedule.domain.exec.CuckooJobExecLogCriteria;
import com.wjs.schedule.enums.CuckooIsTypeDaily;
import com.wjs.schedule.enums.CuckooJobExecStatus;
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
	CuckooJobDetailMapper cuckooJobDetailMapper;
	
	@Autowired
	CuckooJobExecLogMapper cuckooJobExecLogMapper;
	
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
	public void checkDepedencyJobFinished(CuckooJobDetail jobInfo) throws JobDependencyException {
		

		CuckooJobDependencyCriteria depJobMapCrt = new CuckooJobDependencyCriteria();
		depJobMapCrt.createCriteria().andJobIdEqualTo(jobInfo.getId());
		List<CuckooJobDependency> depJobMaps = cuckooJobDependencyMapper.selectByExample(depJobMapCrt);
		List<Long> depJobIds = PropertyUtil.fetchFieldList(depJobMaps, "dependencyJobId");
		if(CollectionUtils.isEmpty(depJobIds)){
			return;
		}
		if(CuckooIsTypeDaily.NO.getValue().equals(jobInfo.getTypeDaily()) ){

			if(CollectionUtils.isNotEmpty(depJobIds)){

				// 如果非日切任务，那么需要检查每个依赖任务的执行状态
				CuckooJobDetailCriteria depJobCrt = new CuckooJobDetailCriteria();
				depJobCrt.createCriteria().andIdIn(depJobIds)
				.andExecJobStatusNotEqualTo(CuckooJobExecStatus.SUCCED.getValue());
				List<CuckooJobDetail> depJobs = cuckooJobDetailMapper.selectByExample(depJobCrt);
				if(CollectionUtils.isNotEmpty(depJobs)){
					LOGGER.info("dependency has not succed yet ,job{}-{}:jobs:{}", jobInfo.getId(), jobInfo.getJobName(), depJobs);
					throw new BaseException("dependency has not succed yet ,job{}-{}:jobs:{}", jobInfo.getId(), jobInfo.getJobName(), depJobs);
				}
			}
			
			
		}else{
			
			if(CollectionUtils.isNotEmpty(depJobIds)){

				// 检查依赖任务的执行日期是否一致
				CuckooJobDetailCriteria depLogTxdateCrt = new CuckooJobDetailCriteria();
				depLogTxdateCrt.createCriteria().andIdIn(depJobIds)
				.andTxDateNotEqualTo(jobInfo.getTxDate());
				List<CuckooJobDetail> depLogTxJobs = cuckooJobDetailMapper.selectByExample(depLogTxdateCrt);
				if(CollectionUtils.isNotEmpty(depLogTxJobs)){
					LOGGER.error("dependency has txdate not the same ,job:{}-{},txdate:{},depjobs:{}", jobInfo.getId(), jobInfo.getJobName(), jobInfo.getTxDate(), depLogTxJobs);
					throw new JobDependencyException("dependency has not succed yet ,job:{}-{},txdate:{},txdate:{},depjobs:{}", jobInfo.getId(), jobInfo.getJobName(), jobInfo.getTxDate(), depLogTxJobs);
				}
				
				// 检查当天任务执行状况
				CuckooJobDetailCriteria depLogExecCrt = new CuckooJobDetailCriteria();
				depLogExecCrt.createCriteria().andIdIn(depJobIds)
				.andExecJobStatusNotEqualTo(CuckooJobExecStatus.SUCCED.getValue())
				.andTxDateEqualTo(jobInfo.getTxDate());
				List<CuckooJobDetail> depLogExecJobs = cuckooJobDetailMapper.selectByExample(depLogExecCrt);
				if(CollectionUtils.isNotEmpty(depLogExecJobs)){
					LOGGER.info("dependency has not succed yet ,job:{}-{},txdate:{},depjobs:{}", jobInfo.getId(), jobInfo.getJobName(), jobInfo.getTxDate(), depLogExecJobs);
					throw new BaseException("dependency has not succed yet ,job:{}-{},txdate:{},txdate:{},depjobs:{}", jobInfo.getId(), jobInfo.getJobName(), jobInfo.getTxDate(), depLogExecJobs);
				}
			}
			
		}
		
		
	}

	@Override
	public List<Long> listDependencyIdsByJobId(Long jobId) {
		
		List<Long> rtn = new ArrayList<>(0);
		CuckooJobDependencyCriteria crt = new CuckooJobDependencyCriteria();
		crt.createCriteria().andJobIdEqualTo(jobId);
		List<CuckooJobDependency> result = cuckooJobDependencyMapper.selectByExample(crt);
		if(CollectionUtils.isNotEmpty(result)){
			rtn = PropertyUtil.fetchFieldList(result, "dependencyJobId");
		}
		return rtn;
	}

}
