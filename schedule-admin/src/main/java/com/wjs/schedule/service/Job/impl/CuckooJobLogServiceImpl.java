package com.wjs.schedule.service.Job.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wjs.schedule.dao.exec.CuckooJobExecLogMapper;
import com.wjs.schedule.dao.exec.CuckooJobExecLogSubMapper;
import com.wjs.schedule.domain.exec.CuckooJobDetail;
import com.wjs.schedule.domain.exec.CuckooJobExecLog;
import com.wjs.schedule.domain.exec.CuckooJobExecLogCriteria;
import com.wjs.schedule.enums.CuckooIsTypeDaily;
import com.wjs.schedule.enums.CuckooJobExecStatus;
import com.wjs.schedule.enums.CuckooJobTriggerType;
import com.wjs.schedule.service.Job.CuckooJobLogService;
import com.wjs.schedule.vo.qry.JobLogOverTimeQry;
import com.wjs.schedule.vo.qry.JobLogQry;
import com.wjs.util.DateUtil;
import com.wjs.util.bean.PropertyUtil;
import com.wjs.util.dao.PageDataList;

@Service("cuckooJobLogService")
public class CuckooJobLogServiceImpl implements CuckooJobLogService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CuckooJobLogServiceImpl.class);
	@Autowired
	CuckooJobExecLogMapper cuckooJobExecLogMapper;
	
	@Autowired
	CuckooJobExecLogSubMapper cuckooJobExecLogSubMapper;
	
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
	public CuckooJobExecLog initSysCronJobLog(Long cuckooJobId ,CuckooJobDetail cuckooJobDetail) {

		CuckooJobExecLog cuckooJobExecLog = null;
		if(null != cuckooJobId){
			cuckooJobExecLog = cuckooJobExecLogMapper.selectByPrimaryKey(cuckooJobId);
		}
		if(null != cuckooJobExecLog){
			return cuckooJobExecLog;
		}

		cuckooJobExecLog = new CuckooJobExecLog();
		 
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
//			cuckooJobExecLog.setFlowLastTime(0L);
			// 获得上一次任务执行时间最大的截止时间为当前任务的开始时间
			CuckooJobExecLogCriteria crt = new CuckooJobExecLogCriteria();
			crt.createCriteria().andJobIdEqualTo(cuckooJobExecLog.getJobId())
			.andGroupIdEqualTo(cuckooJobExecLog.getGroupId());
			crt.setOrderByClause(" flow_cur_time desc ");
			crt.setStart(0);
			crt.setLimit(1);
			List<CuckooJobExecLog> latestJobLog = cuckooJobExecLogMapper.selectByExample(crt);
			if(CollectionUtils.isEmpty(latestJobLog)){

				cuckooJobExecLog.setFlowLastTime(0L);
			}else{
				cuckooJobExecLog.setFlowLastTime(latestJobLog.get(0).getFlowCurTime());
			}
			
			cuckooJobExecLog.setFlowCurTime(curTime);
		}
		
		cuckooJobExecLogMapper.insertSelective(cuckooJobExecLog);
		cuckooJobExecLog.setId(cuckooJobExecLogMapper.lastInsertId());
		
		return cuckooJobExecLog;
	
	}

	@Override
	public CuckooJobExecLog initUnDailyJobLog(CuckooJobDetail cuckooJobDetail, Boolean needTriggleNext, Long flowLastTime, Long flowCurTime, boolean foreTriggle) { 
		
		
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
		cuckooJobExecLog.setForceTriggle(foreTriggle);
		
		cuckooJobExecLog.setFlowLastTime(flowLastTime);
		cuckooJobExecLog.setFlowCurTime(flowCurTime);
		
		cuckooJobExecLogMapper.insertSelective(cuckooJobExecLog);
		cuckooJobExecLog.setId(cuckooJobExecLogMapper.lastInsertId());
		
		return cuckooJobExecLog;
	}

	@Override
	public CuckooJobExecLog initDailyJobLog(CuckooJobDetail cuckooJobDetail, Boolean needTriggleNext, Integer txDate, boolean foreTriggle) {
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
		cuckooJobExecLog.setForceTriggle(foreTriggle);

		cuckooJobExecLog.setTxDate(txDate);
		cuckooJobExecLogMapper.insertSelective(cuckooJobExecLog);
		cuckooJobExecLog.setId(cuckooJobExecLogMapper.lastInsertId());

		return cuckooJobExecLog;
	}

	@Override
	public PageDataList<CuckooJobExecLog> pageByQry(JobLogQry qry) {
		
		
		CuckooJobExecLogCriteria crtPi = new CuckooJobExecLogCriteria();
		crtPi.setStart(qry.getStart());
		crtPi.setLimit(qry.getLimit());
		crtPi.setOrderByClause(" id desc ");
		
		CuckooJobExecLogCriteria.Criteria crt = crtPi.createCriteria();
	  
		if(null != qry.getLogId()){
			crt.andIdEqualTo(qry.getLogId());
		}
		
		if(null != qry.getGroupId()){
			crt.andGroupIdEqualTo(qry.getGroupId());
		}
		if(null != qry.getJobId()){
			crt.andJobIdEqualTo(qry.getJobId());
		}
		
		if(null != qry.getStartDateTime()){
			crt.andJobStartTimeGreaterThanOrEqualTo(qry.getStartDateTime());
		}
		if(null != qry.getEndDateTime()){
			crt.andJobStartTimeLessThanOrEqualTo(qry.getEndDateTime());
		}
		
		if(null != qry.getJobStatus()){
			crt.andExecJobStatusIn(qry.getJobStatus());
		}
		
		return cuckooJobExecLogMapper.pageByExample(crtPi);
	}

	@Override
	public void resetLogStatus(Long logId, CuckooJobExecStatus status) {

		CuckooJobExecLog cuckooJobExecLog = new CuckooJobExecLog();
		cuckooJobExecLog.setId(logId);
		cuckooJobExecLog.setExecJobStatus(status.getValue());
		cuckooJobExecLogMapper.updateByPrimaryKeySelective(cuckooJobExecLog);
	}

	@Override
	public PageDataList<CuckooJobExecLog> pageOverTimeJobs(JobLogOverTimeQry qry) {
		
		PageDataList<CuckooJobExecLog> page = new PageDataList<>();
		page.setPage(qry.getStart() / qry.getLimit()  + 1);
		page.setPageSize(qry.getLimit());
		page.setRows(cuckooJobExecLogSubMapper.pageOverTimeJobs(qry));
		page.setTotal(cuckooJobExecLogSubMapper.countOverTimeJobs(qry));
		
		return page;
	}

	@Override
	public boolean checkPreLogIsDone(CuckooJobExecLog jobLog) {

		
		
		
		if(CuckooIsTypeDaily.YES.getValue().equals(jobLog.getTypeDaily())){
			
			
			// 第一次执行的情况，返回true
			CuckooJobExecLogCriteria firstCrt = new CuckooJobExecLogCriteria();
			firstCrt.setOrderByClause(" tx_date desc ,id desc ");
			firstCrt.createCriteria().andGroupIdEqualTo(jobLog.getGroupId())
			.andJobIdEqualTo(jobLog.getJobId())
			// 考虑到修改任务的时候，如果修改触发方式会造成后续判断条件不一致，如果发生这种情况返回失败
			.andTriggerTypeEqualTo(jobLog.getTriggerType())
			// 首次任务重新执行的情况,没有时间更早的任务
			.andTxDateLessThan(jobLog.getTxDate());
			List<CuckooJobExecLog> firstResult = cuckooJobExecLogMapper.selectByExample(firstCrt);
			if(CollectionUtils.isEmpty(firstResult)){
				return true;
			}else{
				// 如果不为空,最近一条的执行是必须要连续的
				if(!DateUtil.addIntDate(jobLog.getTxDate(), -1).equals(firstResult.get(0).getTxDate())){
					LOGGER.error("job log exec is not continuous, curlogId:{},txdate:{},prelogId:{},curTime:{}"
							, jobLog.getId(), jobLog.getTxDate(),firstResult.get(0).getId(), firstResult.get(0).getTxDate());
					return false;
				}
			}
			
			
			// 有上级任务， 日切任务根据txdate-1获得上一个任务最终(ID倒序)执行状态
			CuckooJobExecLogCriteria cronCrt = new CuckooJobExecLogCriteria();
			cronCrt.setOrderByClause(" id desc ");
			cronCrt.createCriteria().andGroupIdEqualTo(jobLog.getGroupId())
			.andJobIdEqualTo(jobLog.getJobId())
			.andTxDateEqualTo(DateUtil.addIntDate(jobLog.getTxDate(), -1));
//			.andExecJobStatusEqualTo(CuckooJobExecStatus.SUCCED.getValue());
			List<CuckooJobExecLog> cronResult = cuckooJobExecLogMapper.selectByExample(cronCrt);
			if(CollectionUtils.isNotEmpty(cronResult)){
				if(CuckooJobExecStatus.SUCCED.getValue().equals(cronResult.get(0).getExecJobStatus())){
					return true;
				}
			}
			
		}else if(CuckooIsTypeDaily.NO.getValue().equals(jobLog.getTypeDaily())){

			

			// 第一次执行的情况，返回true
			CuckooJobExecLogCriteria firstCrt = new CuckooJobExecLogCriteria();
			firstCrt.setOrderByClause(" flow_cur_time desc ,id desc ");
			firstCrt.createCriteria().andGroupIdEqualTo(jobLog.getGroupId())
			.andJobIdEqualTo(jobLog.getJobId())
			// 考虑到修改任务的时候，如果修改触发方式会造成后续判断条件不一致，如果发生这种情况返回失败
			.andTriggerTypeEqualTo(jobLog.getTriggerType())
			// 首次任务重新执行的情况,没有时间更早的任务
			.andFlowCurTimeLessThan(jobLog.getFlowCurTime());
			List<CuckooJobExecLog> firstResult = cuckooJobExecLogMapper.selectByExample(firstCrt);
			if(CollectionUtils.isEmpty(firstResult)){
				return true;
			}else{
				// 如果不为空,最近一条的执行是必须要连续的
				if(!jobLog.getFlowLastTime().equals(firstResult.get(0).getFlowCurTime())){
					LOGGER.error("job log exec is not continuous, curlogId:{},lastTime:{},prelogId:{},curTime:{}"
							, jobLog.getId(), jobLog.getFlowLastTime(),firstResult.get(0).getId(), firstResult.get(0).getFlowCurTime());
					return false;
				}
				
			}
			
			// 非日切任务，根据lastTime获得上一个任务最终(ID倒序)执行状态
			CuckooJobExecLogCriteria jobCrt = new CuckooJobExecLogCriteria();
			jobCrt.setOrderByClause(" id desc ");
			jobCrt.createCriteria().andGroupIdEqualTo(jobLog.getGroupId())
			.andJobIdEqualTo(jobLog.getJobId())
			.andFlowCurTimeEqualTo(jobLog.getFlowLastTime());
//			.andExecJobStatusEqualTo(CuckooJobExecStatus.SUCCED.getValue());
			List<CuckooJobExecLog> jobResult = cuckooJobExecLogMapper.selectByExample(jobCrt);
			if(CollectionUtils.isNotEmpty(jobResult)){
				if(CuckooJobExecStatus.SUCCED.getValue().equals(jobResult.get(0).getExecJobStatus())){
					return true;
				}
			}
		}
		return false;
	}

}
