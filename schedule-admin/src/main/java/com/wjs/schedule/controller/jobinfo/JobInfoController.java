package com.wjs.schedule.controller.jobinfo;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wjs.schedule.controller.BaseController;
import com.wjs.schedule.domain.exec.CuckooJobDetail;
import com.wjs.schedule.domain.exec.CuckooJobGroup;
import com.wjs.schedule.enums.CuckooIsTypeDaily;
import com.wjs.schedule.enums.CuckooJobExecStatus;
import com.wjs.schedule.enums.CuckooJobStatus;
import com.wjs.schedule.exception.BaseException;
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
		CuckooJobGroup all = new CuckooJobGroup();
		all.setGroupName("全部/无");
		jobGroups.add(0, all);
		request.setAttribute("jobGroupList", jobGroups);
		
		// APP应用
		Map<String,String> jobAppList = cuckooJobService.findAllApps();
		request.setAttribute("jobAppList", jobAppList);
		
		// 任务状态
		CuckooJobStatus[] jobStatusList = CuckooJobStatus.values();
		request.setAttribute("jobStatusList", jobStatusList);
		
		// 任务执行状态
		CuckooJobExecStatus[] jobExecStatus = CuckooJobExecStatus.values();
		request.setAttribute("jobExecStatus", jobExecStatus);
		
		return "jobinfo/jobinfo.index";
	}
	
	/**
	 * 分页查询任务
	 * @param jobInfo
	 * @param start
	 * @param limit
	 * @return
	 */
	@RequestMapping(value="/pageList")
	@ResponseBody
	public Object pageList(JobInfoQry jobInfo, Integer start, Integer limit ){
		PageDataList<CuckooJobDetail> page = cuckooJobService.pageList(jobInfo, start, limit);
		return dataTable(page);
	}
	
	/**
	 * 执行
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/trigger")
	@ResponseBody
	public Object trigger(Long id, String typeDaily, Boolean needTriggleNext, Integer txDate, Long flowLastTime, Long flowCurTime ){
		
		if(CuckooIsTypeDaily.NO.getValue().equals(typeDaily)){
			
			cuckooJobService.pendingUnDailyJob(id, needTriggleNext, flowLastTime, flowCurTime);
		}else if(CuckooIsTypeDaily.YES.getValue().equals(typeDaily)){

			cuckooJobService.pendingDailyJob(id, needTriggleNext, txDate);
		}else{
			throw new BaseException("Unknow triggerType:{}", typeDaily);
		}
		return success();
	}


	/**
	 * 暂停
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/pause")
	@ResponseBody
	public Object pause(Long id){
		
		System.err.println(id);
		return success();
	}
	
	/**
	 * 恢复
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/resume")
	@ResponseBody
	public Object resume(Long id){
		
		System.err.println(id);
		return success();
	}

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/remove")
	@ResponseBody
	public Object remove(Long id){
		
		System.err.println(id);
		return success();
	}
	
	
}
