package com.wjs.schedule.service.Job.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wjs.schedule.dao.exec.CuckooJobExecLogMapper;
import com.wjs.schedule.domain.exec.CuckooJobDetail;
import com.wjs.schedule.domain.exec.CuckooJobExecLog;
import com.wjs.schedule.enums.CuckooIsTypeDaily;
import com.wjs.schedule.enums.CuckooJobExecStatus;
import com.wjs.schedule.service.Job.CuckooJobLogService;
import com.wjs.util.DateUtil;
import com.wjs.util.bean.PropertyUtil;

@Service("cuckooJobLogService")
public class CuckooJobLogServiceImpl implements CuckooJobLogService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CuckooJobLogServiceImpl.class);
	@Autowired
	CuckooJobExecLogMapper cuckooJobExecLogMapper;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Long insertSelective(CuckooJobExecLog log) {
		
		cuckooJobExecLogMapper.insertSelective(log);
		return cuckooJobExecLogMapper.lastInsertId();
	}

	@Override
	public CuckooJobExecLog getJobLogByLogId(Long jobLogId) {

		return cuckooJobExecLogMapper.selectByPrimaryKey(jobLogId);
	}

	@Override
	public void updateJobLogStatusById(Long id, CuckooJobExecStatus jobStatus, String message) {

		CuckooJobExecLog log = new CuckooJobExecLog();
		log.setId(id);
		log.setExecJobStatus(jobStatus.getValue());
		log.setJobEndTime(System.currentTimeMillis());
		log.setRemark(message);
		cuckooJobExecLogMapper.updateByPrimaryKeySelective(log);
		
	}


	@Override
	public void updateJobLogByPk(CuckooJobExecLog cuckooJobExecLogs) {

		cuckooJobExecLogMapper.updateByPrimaryKeySelective(cuckooJobExecLogs);
	}
	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public CuckooJobExecLog initFirstJobLog(CuckooJobDetail cuckooJobDetail) {

		CuckooJobExecLog cuckooJobExecLog = new CuckooJobExecLog();
		Long curTime =  System.currentTimeMillis();
		
		// 写入初始化任务执行信息
		PropertyUtil.copyProperties(cuckooJobExecLog, cuckooJobDetail);
		cuckooJobExecLog.setId(null);
		cuckooJobExecLog.setJobId(cuckooJobDetail.getId());
		cuckooJobExecLog.setJobStartTime(curTime);
		cuckooJobExecLog.setExecJobStatus(CuckooJobExecStatus.PENDING.getValue());
		cuckooJobExecLog.setLatestCheckTime(curTime);
		cuckooJobExecLog.setNeedTriggleNext(true);
		cuckooJobExecLog.setForceTriggle(false);
		
		if(CuckooIsTypeDaily.YES.getValue().equals(cuckooJobDetail.getTypeDaily())){
			// 如果是日切任务，那么计算出TxDate
			
			cuckooJobExecLog.setTxDate(DateUtil.addIntDate(DateUtil.getIntDay(new Date()), cuckooJobDetail.getOffset()));
		}else{
			// 非日切任务,则认为是流式任务
			cuckooJobExecLog.setFlowLastTime(0L);
			cuckooJobExecLog.setFlowCurTime(curTime);
		}
		
		cuckooJobExecLogMapper.insertSelective(cuckooJobExecLog);
		cuckooJobExecLog.setId(cuckooJobExecLogMapper.lastInsertId());
		
		return cuckooJobExecLog;
	
	}

	@Override
	public CuckooJobExecLog initUnDailyJobLog(CuckooJobDetail cuckooJobDetail, Boolean needTriggleNext, Long flowLastTime, Long flowCurTime) {
		
		
		
		CuckooJobExecLog cuckooJobExecLog = new CuckooJobExecLog();
		Long curTime =  System.currentTimeMillis();
		
		// 写入初始化任务执行信息
		PropertyUtil.copyProperties(cuckooJobExecLog, cuckooJobDetail);
		cuckooJobExecLog.setId(null);
		cuckooJobExecLog.setJobId(cuckooJobDetail.getId());
		cuckooJobExecLog.setJobStartTime(curTime);
		cuckooJobExecLog.setExecJobStatus(CuckooJobExecStatus.PENDING.getValue());
		cuckooJobExecLog.setLatestCheckTime(curTime);
		cuckooJobExecLog.setNeedTriggleNext(needTriggleNext);
		cuckooJobExecLog.setForceTriggle(false);
		
		cuckooJobExecLog.setFlowLastTime(flowLastTime);
		cuckooJobExecLog.setFlowCurTime(flowCurTime);
		
		cuckooJobExecLogMapper.insertSelective(cuckooJobExecLog);
		cuckooJobExecLog.setId(cuckooJobExecLogMapper.lastInsertId());
		
		return cuckooJobExecLog;
	}

	@Override
	public CuckooJobExecLog initDailyJobLog(CuckooJobDetail cuckooJobDetail, Boolean needTriggleNext, Integer txDate) {
		CuckooJobExecLog cuckooJobExecLog = new CuckooJobExecLog();
		Long curTime = System.currentTimeMillis();

		// 写入初始化任务执行信息
		PropertyUtil.copyProperties(cuckooJobExecLog, cuckooJobDetail);
		cuckooJobExecLog.setId(null);
		cuckooJobExecLog.setJobId(cuckooJobDetail.getId());
		cuckooJobExecLog.setJobStartTime(curTime);
		cuckooJobExecLog.setExecJobStatus(CuckooJobExecStatus.PENDING.getValue());
		cuckooJobExecLog.setLatestCheckTime(curTime);
		cuckooJobExecLog.setNeedTriggleNext(needTriggleNext);
		cuckooJobExecLog.setForceTriggle(false);

		cuckooJobExecLog.setTxDate(txDate);
		cuckooJobExecLogMapper.insertSelective(cuckooJobExecLog);
		cuckooJobExecLog.setId(cuckooJobExecLogMapper.lastInsertId());

		return cuckooJobExecLog;
	}

}
