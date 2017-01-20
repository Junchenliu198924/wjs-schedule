package com.wjs.schedule.service.Job.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wjs.schedule.dao.exec.CuckooJobExecLogsMapper;
import com.wjs.schedule.domain.exec.CuckooJobExecLogs;
import com.wjs.schedule.service.Job.CuckooJobLogService;

public class CuckooJobLogServiceImpl implements CuckooJobLogService {

	@Autowired
	CuckooJobExecLogsMapper cuckooJobExecLogsMapper;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void insertSelective(CuckooJobExecLogs log) {
		
		cuckooJobExecLogsMapper.insertSelective(log);
	}

}
