package com.wjs.schedule.service.Job.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wjs.schedule.dao.exec.CuckooJobNextJobMapper;
import com.wjs.schedule.domain.exec.CuckooJobNextJob;
import com.wjs.schedule.domain.exec.CuckooJobNextJobCriteria;
import com.wjs.schedule.exception.BaseException;
import com.wjs.schedule.service.Job.CuckooJobNextService;
import com.wjs.schedule.util.CuckBeanUtil;
import com.wjs.schedule.vo.job.JobNext;

@Service("cuckooJobNextService")
public class CuckooJobNextServiceImpl implements CuckooJobNextService {

	@Autowired
	CuckooJobNextJobMapper cuckooJobNextJobMapper;

	@Override
	@Transactional
	public void setNextJobConfig(List<JobNext> nextJobs) {
		
		if(CollectionUtils.isEmpty(nextJobs)){
			
			throw new BaseException("next jobs should not be empty : ");
		}
		// 先删除触发
		CuckooJobNextJobCriteria curJobCrt = new CuckooJobNextJobCriteria();
		curJobCrt.createCriteria().andIdEqualTo(nextJobs.get(0).getJobId());
		cuckooJobNextJobMapper.deleteByExample(curJobCrt);
		
		// 再增加触发
		for (JobNext jobNext : nextJobs) {
			CuckooJobNextJob cuckooJobNextJob = CuckBeanUtil.parseJobNext(jobNext);
			cuckooJobNextJobMapper.insertSelective(cuckooJobNextJob);
		}
	}

}
