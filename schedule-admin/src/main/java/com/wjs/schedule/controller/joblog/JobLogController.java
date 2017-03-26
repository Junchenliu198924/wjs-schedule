package com.wjs.schedule.controller.joblog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wjs.schedule.controller.BaseController;
import com.wjs.schedule.domain.exec.CuckooJobDetail;
import com.wjs.schedule.domain.exec.CuckooJobExecLog;
import com.wjs.schedule.domain.exec.CuckooJobGroup;
import com.wjs.schedule.enums.CuckooJobExecStatus;
import com.wjs.schedule.exception.BaseException;
import com.wjs.schedule.service.job.CuckooGroupService;
import com.wjs.schedule.service.job.CuckooJobLogService;
import com.wjs.schedule.service.job.CuckooJobService;
import com.wjs.schedule.vo.QryBase;
import com.wjs.schedule.vo.job.CuckooJobExecLogVo;
import com.wjs.schedule.vo.qry.JobLogQry;
import com.wjs.util.DateUtil;
import com.wjs.util.bean.PropertyUtil;
import com.wjs.util.dao.PageDataList;

@Controller
@RequestMapping("/joblog")
public class JobLogController extends BaseController {

	@Autowired
	CuckooGroupService cuckooGroupService;
	
	@Autowired
	CuckooJobService cuckooJobService;
	
	@Autowired
	CuckooJobLogService cuckooJobLogService;
	

	@RequestMapping
	public String index0(HttpServletRequest request,Long groupId, Long jobId) {

		return index(request, groupId, jobId);
	}

	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request,Long groupId, Long jobId) {
		

		request.setAttribute("groupId", groupId);
		request.setAttribute("jobId", jobId);
		// 任务类型
		List<CuckooJobGroup> jobGroupList = cuckooGroupService.selectAllGroup();
		request.setAttribute("jobGroupList", jobGroupList);
		List<CuckooJobGroup> jobGroupsWithNull = new ArrayList<CuckooJobGroup>();
		CuckooJobGroup groupNull = new CuckooJobGroup();
		groupNull.setGroupName("全部/无");
		jobGroupsWithNull.add(0, groupNull);
		jobGroupsWithNull.addAll(jobGroupList);
		request.setAttribute("jobGroupsWithNull", jobGroupsWithNull);
		
		// 执行状态

		CuckooJobExecStatus[] jobExecStatus = CuckooJobExecStatus.values();
		request.setAttribute("jobExecStatusList", jobExecStatus);
		
		return "joblog/joblog.index";
	}

	@ResponseBody
	@RequestMapping(value = "/getJobsByGroup")
	public Object getJobsByGroup(Long groupId){
		
		
		return success(cuckooJobService.getJobsByGroupId(groupId));
	}
	
	
	@ResponseBody
	@RequestMapping(value="/pageList")
	public Object pageList(JobLogQry qry,String jobStatusStr){
		
		if(StringUtils.isNotEmpty(qry.getFilterTime())){
			// 2017-03-20 00:00:00 - 2017-03-21 00:00:00
			String[] timeRange = qry.getFilterTime().split(" - ");
			qry.setStartDateTime(DateUtil.parseDate(timeRange[0], "yyyy-MM-dd HH:mm:ss").getTime());
			qry.setEndDateTime(DateUtil.parseDate(timeRange[1], "yyyy-MM-dd HH:mm:ss").getTime());
		}
		
		if(StringUtils.isNotEmpty(jobStatusStr)){
			String[] jobStatusArr = jobStatusStr.split(",");
			qry.setJobStatus(Arrays.asList(jobStatusArr));
		}
		
		
		PageDataList<CuckooJobExecLog> pageLog = cuckooJobLogService.pageByQry(qry);
		
		PageDataList<CuckooJobExecLogVo> pageLogVo = new PageDataList<>();
		pageLogVo.setPage(pageLog.getPage());
		pageLogVo.setPageSize(pageLog.getPageSize());
		pageLogVo.setTotal(pageLog.getTotal());
		
		
		pageLogVo.setRows(converPageRows(pageLog.getRows()));
		
		return dataTable(pageLogVo);
	}
	
	
	@ResponseBody
	@RequestMapping(value="/pagePendingList")
	public Object pagePendingList(QryBase qry){
		
		PageDataList<CuckooJobExecLog> pageLog = cuckooJobLogService.pagePendingList(qry);
		
		PageDataList<CuckooJobExecLogVo> pageLogVo = new PageDataList<>();
		pageLogVo.setPage(pageLog.getPage());
		pageLogVo.setPageSize(pageLog.getPageSize());
		pageLogVo.setTotal(pageLog.getTotal());
		
		
		pageLogVo.setRows(converPageRows(pageLog.getRows()));
		
		return dataTable(pageLogVo);
	}
	
	
	@ResponseBody
	@RequestMapping(value="/pageOverTimeList")
	public Object pageOverTimeList(QryBase qry){
		
		PageDataList<CuckooJobExecLog> pageLog = cuckooJobLogService.pageOverTimeJobs(qry);
		
		PageDataList<CuckooJobExecLogVo> pageLogVo = new PageDataList<>();
		pageLogVo.setPage(pageLog.getPage());
		pageLogVo.setPageSize(pageLog.getPageSize());
		pageLogVo.setTotal(pageLog.getTotal());
		
		
		pageLogVo.setRows(converPageRows(pageLog.getRows()));
		
		return dataTable(pageLogVo);
	}

	private List<CuckooJobExecLogVo> converPageRows(List<CuckooJobExecLog> rows) {
		
		List<CuckooJobExecLogVo> list = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(rows)){
			for (CuckooJobExecLog cuckooJobExecLog : rows) {
				CuckooJobExecLogVo vo = new CuckooJobExecLogVo();
				PropertyUtil.copyProperties(vo, cuckooJobExecLog);
				list.add(vo);
			}
		}
		
		return list;
	}	
	
	@ResponseBody
	@RequestMapping(value="/reset")
	public Object reset(Long logId){
		
		if(null == logId){
			throw new BaseException("logid can not be null");
		}
		
		cuckooJobLogService.resetLogStatus(logId, CuckooJobExecStatus.SUCCED);
		
		return success();
	}
	
	@ResponseBody
	@RequestMapping(value="/redo")
	public Object redo(Long logId, Boolean needTriggleNext){
		
		if(null == logId){
			throw new BaseException("logid can not be null");
		}
		
//		cuckooJobLogService.resetLogStatus(logId, CuckooJobExecStatus.SUCCED);
		
		CuckooJobExecLog cuckooJobExecLog = cuckooJobLogService.getJobLogByLogId(logId);
		if(null == cuckooJobExecLog){
			 throw new BaseException("can not get jobLog by logid:{}", logId);
		}
		cuckooJobExecLog.setForceTriggle(true);
		cuckooJobExecLog.setNeedTriggleNext(needTriggleNext == null ? false : needTriggleNext);
		CuckooJobDetail jobDetail = cuckooJobService.getJobById(cuckooJobExecLog.getJobId());
		if(null == jobDetail){
			 throw new BaseException("can not get jobDetail by jobId:{}", cuckooJobExecLog.getJobId());
		}
		Long id = cuckooJobService.pendingJob(jobDetail, cuckooJobExecLog);
		
		return success(id);
	}
	

	@RequestMapping(value = "/logdetail")
	public String logdetail(HttpServletRequest request, Long logId){
		
		CuckooJobExecLog cuckooJobExecLog = cuckooJobLogService.getJobLogByLogId(logId);
		if(null == cuckooJobExecLog){
			 throw new BaseException("can not get jobLog by logid:{}", logId);
		}
		
		request.setAttribute("log", converJobLogVo(cuckooJobExecLog));
		
		return "joblog/joblog.detail";
	}

	private CuckooJobExecLogVo converJobLogVo(CuckooJobExecLog cuckooJobExecLog) {

		CuckooJobExecLogVo vo = new CuckooJobExecLogVo();
		
		CuckooJobGroup jobGroup = cuckooGroupService.getGroupById(cuckooJobExecLog.getGroupId());
		CuckooJobDetail jobDetail = cuckooJobService.getJobById(cuckooJobExecLog.getJobId());
		vo.setGroupName(jobGroup.getGroupName());
		vo.setJobDesc(jobDetail.getJobDesc());
		PropertyUtil.copyProperties(vo, cuckooJobExecLog);
		return vo;
	}
	
	
}
