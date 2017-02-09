package com.wjs.schedule.service.Job.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wjs.schedule.dao.exec.CuckooJobExecLogsMapper;
import com.wjs.schedule.domain.exec.CuckooJobDetails;
import com.wjs.schedule.domain.exec.CuckooJobExecLogs;
import com.wjs.schedule.domain.exec.CuckooJobExecLogsCriteria;
import com.wjs.schedule.enums.CuckooJobExecStatus;
import com.wjs.schedule.enums.CuckooJobTriggerType;
import com.wjs.schedule.exception.BaseException;
import com.wjs.schedule.service.Job.CuckooJobLogService;

@Service("cuckooJobLogService")
public class CuckooJobLogServiceImpl implements CuckooJobLogService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CuckooJobLogServiceImpl.class);
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
	public void updateJobLogStatusById(Long id, CuckooJobExecStatus jobStatus) {

		CuckooJobExecLogs log = new CuckooJobExecLogs();
		log.setId(id);
		log.setExecJobStatus(jobStatus.getValue());
		cuckooJobExecLogsMapper.updateByPrimaryKey(log);
		
	}

	@Override
	public Boolean getJobNeedTriglerByJobInfo(CuckooJobDetails jobInfo) {
		
		CuckooJobExecLogsCriteria logCrt = new CuckooJobExecLogsCriteria();
		CuckooJobExecLogsCriteria.Criteria crt = logCrt.createCriteria();
		
		crt.andJobIdEqualTo(jobInfo.getId());
		if(CuckooJobTriggerType.FLOW.getValue().equals(jobInfo.getTriggerType())){
			crt.andFlowCurTimeEqualTo(jobInfo.getFlowCurTime())
			.andFlowLastTimeEqualTo(jobInfo.getFlowLastTime());
		}else if(CuckooJobTriggerType.CRON.getValue().equals(jobInfo.getTriggerType()) 
				|| CuckooJobTriggerType.DAILY.getValue().equals(jobInfo.getTriggerType())){
			crt.andTxDateEqualTo(jobInfo.getTxDate());
		}else{
			LOGGER.error("unknow job triggler type:{}, jobInfo:", jobInfo.getTriggerType(), jobInfo);
			throw new BaseException("unknow job triggler type:{}, jobInfo:", jobInfo.getTriggerType(), jobInfo);
		}
		logCrt.setOrderByClause(" id desc ");
		
		List<CuckooJobExecLogs> logs =  cuckooJobExecLogsMapper.selectByExample(logCrt);
		if(CollectionUtils.isNotEmpty(logs)){
			return logs.get(0).getNeedTriggleNext();
		}
		return true;
	}

}
