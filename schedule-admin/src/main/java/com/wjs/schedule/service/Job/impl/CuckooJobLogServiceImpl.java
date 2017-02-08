package com.wjs.schedule.service.Job.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wjs.schedule.dao.exec.CuckooJobExecLogsMapper;
import com.wjs.schedule.domain.exec.CuckooJobExecLogs;
import com.wjs.schedule.enums.JobExecStatus;
import com.wjs.schedule.service.Job.CuckooJobLogService;

@Service("cuckooJobLogService")
public class CuckooJobLogServiceImpl implements CuckooJobLogService {

	@Autowired
	CuckooJobExecLogsMapper cuckooJobExecLogsMapper;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void insertSelective(CuckooJobExecLogs log) {
		
		cuckooJobExecLogsMapper.insertSelective(log);
	}

	@Override
	public CuckooJobExecLogs getJobLogByLogId(Long jobLogId) {

		return cuckooJobExecLogsMapper.selectByPrimaryKey(jobLogId);
	}

	@Override
	public void updateJobLogStatusById(Long id, JobExecStatus jobStatus) {

		CuckooJobExecLogs log = new CuckooJobExecLogs();
		log.setId(id);
		log.setExecJobStatus(jobStatus.getValue());
		cuckooJobExecLogsMapper.updateByPrimaryKey(log);
		
	}

}
