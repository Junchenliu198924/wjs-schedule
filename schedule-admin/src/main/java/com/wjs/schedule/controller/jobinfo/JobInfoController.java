package com.wjs.schedule.controller.jobinfo;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.wjs.schedule.enums.CuckooJobTriggerType;
import com.wjs.schedule.exception.BaseException;
import com.wjs.schedule.service.Job.CuckooGroupService;
import com.wjs.schedule.service.Job.CuckooJobDependencyService;
import com.wjs.schedule.service.Job.CuckooJobNextService;
import com.wjs.schedule.service.Job.CuckooJobService;
import com.wjs.schedule.vo.job.CuckooJobDetailVo;
import com.wjs.schedule.vo.qry.JobInfoQry;
import com.wjs.util.DateUtil;
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
	
	@Autowired
	CuckooJobNextService cuckooJobNextService;
	
	@Autowired
	CuckooJobDependencyService cuckooJobDependencyService;
	
	@RequestMapping
	public String index0(HttpServletRequest request) {
		
		return index(request);
	}
	
	
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request) {
		// 任务类型
		List<CuckooJobGroup> jobGroupList = cuckooGroupService.selectAllGroup();
		request.setAttribute("jobGroupList", jobGroupList);
		
		List<CuckooJobGroup> jobGroupsWithNull = new ArrayList<CuckooJobGroup>();
		CuckooJobGroup groupNull = new CuckooJobGroup();
		groupNull.setGroupName("全部/无");
		jobGroupsWithNull.add(0, groupNull);
		jobGroupsWithNull.addAll(jobGroupList);
		request.setAttribute("jobGroupsWithNull", jobGroupsWithNull);
		
		// APP应用
		Map<String,String> jobAppList = cuckooJobService.findAllApps();
		request.setAttribute("jobAppList", jobAppList);
		Map<String,String> jobAppWithNull = new HashMap<>();
		jobAppWithNull.put("", "全部/无");
		jobAppWithNull.putAll(jobAppList);
		request.setAttribute("jobAppWithNull", jobAppWithNull);
		
		
		
		// 任务状态
		CuckooJobStatus[] jobStatusList = CuckooJobStatus.values();
		request.setAttribute("jobStatusList", jobStatusList);
		
//		CuckooJobStatus[] jobStatusNoNull = CuckooJobStatus.valuesNoNull();
//		request.setAttribute("jobStatusNoNull", jobStatusNoNull);
		
		// 任务执行状态

		CuckooJobExecStatus[] jobExecStatus = CuckooJobExecStatus.values();
		request.setAttribute("jobExecStatusList", jobExecStatus);
//		CuckooJobExecStatus[] jobExecStatus = CuckooJobExecStatus.valuesNoNull();
//		request.setAttribute("jobExecStatusNoNull", jobExecStatus);
		
		// 任务触发方式
		CuckooJobTriggerType[] jobTriggerType = CuckooJobTriggerType.valuesNoNull();
		request.setAttribute("jobTriggerTypeNoNull", jobTriggerType);
		
		// 是否为日切任务
		CuckooIsTypeDaily[] jobIsTypeDaily = CuckooIsTypeDaily.valuesNoNull();
		request.setAttribute("jobIsTypeDailyNoNull", jobIsTypeDaily);
		
		
		
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
	 * 根据jobId获取触发任务的id
	 * @param jobId
	 * @return
	 */
	@RequestMapping(value="/getPreJobIdByJobId")
	@ResponseBody
	public Object getPreJobIdByJobId(Long jobId){
		
		return success(cuckooJobNextService.findJobIdByNextJobId(jobId));
	}
	
	
	
	/**
	 * 根据jobId获取任务依赖的任务IDs
	 * @param jobId
	 * @return
	 */
	@RequestMapping(value="/getDependencyIdsByJobId")
	@ResponseBody
	public Object getDependencyIdsByJobId(Long jobId){
		
		return success(cuckooJobDependencyService.listDependencyIdsByJobId(jobId));
	}
	
	/**
	 * 暂停全部
	 * @return
	 */
	@RequestMapping(value="/paushAll")
	@ResponseBody
	public Object paushAll(){
		
		cuckooJobService.pauseAllJob();
		return success();
	}
	
	/**
	 * 回复全部
	 * @return
	 */
	@RequestMapping(value="/resumeAll")
	@ResponseBody
	public Object resumeAll(){

		cuckooJobService.resumeAllJob();
		return success();
	}
	
	/**
	 * 执行
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/trigger")
	@ResponseBody
	public Object trigger(Long id, String typeDaily, Boolean needTriggleNext, Integer txDate, String flowLastTime, String flowCurTime){
		
		
		
		if(CuckooIsTypeDaily.NO.getValue().equals(typeDaily)){
			
			cuckooJobService.triggerUnDailyJob(id, needTriggleNext, DateUtil.getLongTime(flowLastTime, "yyyyMMddHHmmss"), DateUtil.getLongTime(flowCurTime, "yyyyMMddHHmmss"), true);
		}else if(CuckooIsTypeDaily.YES.getValue().equals(typeDaily)){

			cuckooJobService.triggerDailyJob(id, needTriggleNext, txDate, true);
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
		
		cuckooJobService.pauseOnejob(id);
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
		
		cuckooJobService.resumeOnejob(id);
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
		
		cuckooJobService.removeJob(id);
		return success();
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	public Object add(HttpServletRequest request, CuckooJobDetailVo jobDetail){
		

		
		
		if(null == jobDetail.getId()){


			cuckooJobService.addJob(jobDetail);
		}else{
			cuckooJobService.modifyJob(jobDetail);
		}
		
		
		return success();
	}
	
	
}