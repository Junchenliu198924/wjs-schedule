package com.wjs.schedule.service.Job.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wjs.schedule.dao.exec.CuckooJobExecLogMapper;
import com.wjs.schedule.domain.exec.CuckooJobDetail;
import com.wjs.schedule.domain.exec.CuckooJobExecLog;
import com.wjs.schedule.domain.exec.CuckooJobExecLogCriteria;
import com.wjs.schedule.enums.CuckooIsTypeDaily;
import com.wjs.schedule.enums.CuckooJobExecStatus;
import com.wjs.schedule.service.Job.CuckooJobLogService;

@Service("cuckooJobLogService")
public class CuckooJobLogServiceImpl implements CuckooJobLogService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CuckooJobLogServiceImpl.class);
	@Autowired
	CuckooJobExecLogMapper cuckooJobExecLogsMapper;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Long insertSelective(CuckooJobExecLog log) {
		
		cuckooJobExecLogsMapper.insertSelective(log);
		return cuckooJobExecLogsMapper.lastInsertId();
	}

	@Override
	public CuckooJobExecLog getJobLogByLogId(Long jobLogId) {

		return cuckooJobExecLogsMapper.selectByPrimaryKey(jobLogId);
	}

	@Override
	public void updateJobLogStatusById(Long id, CuckooJobExecStatus jobStatus, String message) {

		CuckooJobExecLog log = new CuckooJobExecLog();
		log.setId(id);
		log.setExecJobStatus(jobStatus.getValue());
		log.setJobEndTime(System.currentTimeMillis());
		log.setRemark(message);
		cuckooJobExecLogsMapper.updateByPrimaryKeySelective(log);
		
	}

	@Override
	public Boolean getJobNeedTriglerByJobInfo(CuckooJobDetail jobInfo) {
		
		CuckooJobExecLogCriteria logCrt = new CuckooJobExecLogCriteria();
		CuckooJobExecLogCriteria.Criteria crt = logCrt.createCriteria();
		
		crt.andJobIdEqualTo(jobInfo.getId());
		if(CuckooIsTypeDaily.YES.getValue().equals(jobInfo.getTypeDaily())){
			crt.andTxDateEqualTo(jobInfo.getTxDate());
		}else {
			crt.andFlowCurTimeEqualTo(jobInfo.getFlowCurTime())
			.andFlowLastTimeEqualTo(jobInfo.getFlowLastTime());
		}
		logCrt.setOrderByClause(" id desc ");
		
		List<CuckooJobExecLog> logs =  cuckooJobExecLogsMapper.selectByExample(logCrt);
		if(CollectionUtils.isNotEmpty(logs)){
			return logs.get(0).getNeedTriggleNext();
		}
		return true;
	}

	@Override
	public void updateJobLogByPk(CuckooJobExecLog cuckooJobExecLogs) {

		cuckooJobExecLogsMapper.updateByPrimaryKeySelective(cuckooJobExecLogs);
	}

}
