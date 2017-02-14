package com.wjs.schedule.controller.jobinfo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wjs.schedule.controller.BaseController;
import com.wjs.schedule.domain.exec.CuckooJobDetails;
import com.wjs.schedule.domain.exec.CuckooJobGroup;
import com.wjs.schedule.enums.CuckooJobExecStatus;
import com.wjs.schedule.enums.CuckooJobStatus;
import com.wjs.schedule.service.Job.CuckooGroupService;
import com.wjs.schedule.service.Job.CuckooJobService;
import com.wjs.schedule.vo.qry.JobInfoQry;
import com.wjs.util.dao.PageDataList;


/**
 * index controller
 */
@Controller
@RequestMapping("/jobinfo")
public class JobInfoController extends BaseController{


	@Autowired
	CuckooJobService cuckooJobService;
	
	@Autowired
	CuckooGroupService cuckooGroupService;
	
	@RequestMapping
	public String index0(HttpServletRequest request) {
		
		return index(request);
	}
	
	
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request) {
		// 任务类型
		List<CuckooJobGroup> jobGroups = cuckooGroupService.selectAllGroup();
		request.setAttribute("jobGroupList", jobGroups);
		
		// APP应用 TODO
		List<String> jobAppList = cuckooJobService.findAllApps();
		request.setAttribute("jobAppList", jobAppList);
		
		// 任务状态
		CuckooJobStatus[] jobStatusList = CuckooJobStatus.values();
		request.setAttribute("jobStatusList", jobStatusList);
		
		// 任务执行状态
		CuckooJobExecStatus[] jobExecStatus = CuckooJobExecStatus.values();
		request.setAttribute("jobExecStatus", jobExecStatus);
		
		return "jobinfo/jobinfo.index";
	}
	
	@RequestMapping(value="/pageList")
	@ResponseBody
	public Object pageList(JobInfoQry jobInfo, Integer start, Integer length ){
		PageDataList<CuckooJobDetails> page = cuckooJobService.pageList(jobInfo, start, length );
		return dataTable(page);
	}


	
	
	
}
