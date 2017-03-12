package com.wjs.schedule.service.Job.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wjs.schedule.dao.exec.CuckooJobDependencyMapper;
import com.wjs.schedule.dao.exec.CuckooJobDetailMapper;
import com.wjs.schedule.dao.exec.CuckooJobExecLogMapper;
import com.wjs.schedule.domain.exec.CuckooJobDependency;
import com.wjs.schedule.domain.exec.CuckooJobDependencyCriteria;
import com.wjs.schedule.domain.exec.CuckooJobExecLog;
import com.wjs.schedule.domain.exec.CuckooJobExecLogCriteria;
import com.wjs.schedule.enums.CuckooIsTypeDaily;
import com.wjs.schedule.enums.CuckooJobExecStatus;
import com.wjs.schedule.exception.BaseException;
import com.wjs.schedule.exception.JobCanNotRunningException;
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
	public void checkDepedencyJobFinished(CuckooJobExecLog jobLog) throws JobCanNotRunningException {

		CuckooJobDependencyCriteria depJobMapCrt = new CuckooJobDependencyCriteria();
		depJobMapCrt.createCriteria().andJobIdEqualTo(jobLog.getJobId());
		List<CuckooJobDependency> depJobMaps = cuckooJobDependencyMapper.selectByExample(depJobMapCrt);
		List<Long> depJobIds = PropertyUtil.fetchFieldList(depJobMaps, "dependencyJobId");
		if (CollectionUtils.isEmpty(depJobIds)) {
			return;
		}

		// 依赖执行任务完成条件： 1.依赖的任务状态都为成功；2.日切任务的txdate需要一致、非日切任务的latestTime一致
		List<CuckooJobExecLog> readyDepJobs = null;
		if (CuckooIsTypeDaily.NO.getValue().equals(jobLog.getTypeDaily())) {

			CuckooJobExecLogCriteria depJobCrt = new CuckooJobExecLogCriteria();
			depJobCrt.createCriteria().andIdIn(depJobIds)
					// 1.依赖的任务状态都为成功
					.andExecJobStatusNotEqualTo(CuckooJobExecStatus.SUCCED.getValue())
					// 非日切任务的latestTime一致
					.andFlowLastTimeEqualTo(jobLog.getFlowLastTime());
			readyDepJobs = cuckooJobExecLogMapper.selectByExample(depJobCrt);

		} else {

			// 2.日切任务的txdate需要一致
			CuckooJobExecLogCriteria depLogTxdateCrt = new CuckooJobExecLogCriteria();
			depLogTxdateCrt.createCriteria().andIdIn(depJobIds)
					// 1.依赖的任务状态都为成功
					.andExecJobStatusNotEqualTo(CuckooJobExecStatus.SUCCED.getValue())
					// 日切任务的txdate需要一致
					.andTxDateEqualTo(jobLog.getTxDate());
			readyDepJobs = cuckooJobExecLogMapper.selectByExample(depLogTxdateCrt);

		}

		if (CollectionUtils.isNotEmpty(readyDepJobs) && readyDepJobs.size() != depJobIds.size()) {
			
			List<Long> readydepJobs = PropertyUtil.fetchFieldList(readyDepJobs, "dependencyJobId");
			

			LOGGER.info("dependency was not ready,jobLog:{},dependyJobs:{},readydepJobs:{}", jobLog, depJobIds, readydepJobs);
			throw new JobCanNotRunningException("dependency was not ready,jobLog:{},dependyJobs:{},readydepJobs:{}", jobLog, depJobIds, readydepJobs);
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
