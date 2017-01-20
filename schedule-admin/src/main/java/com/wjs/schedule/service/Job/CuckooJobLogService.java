package com.wjs.schedule.service.Job;

import com.wjs.schedule.domain.exec.CuckooJobExecLogs;

public interface CuckooJobLogService {
	
	public void insertSelective(CuckooJobExecLogs log);
}
