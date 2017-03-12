package com.wjs.schedule.controller.jobgroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wjs.schedule.controller.BaseController;
import com.wjs.schedule.dao.exec.CuckooJobGroupMapper;
import com.wjs.schedule.domain.exec.CuckooJobGroup;
import com.wjs.schedule.domain.exec.CuckooJobGroupCriteria;
import com.wjs.schedule.enums.CuckooIsTypeDaily;
import com.wjs.schedule.enums.CuckooJobExecStatus;
import com.wjs.schedule.enums.CuckooJobStatus;
import com.wjs.schedule.enums.CuckooJobTriggerType;
import com.wjs.schedule.exception.BaseException;
import com.wjs.schedule.vo.job.JobGroup;
import com.wjs.util.bean.PropertyUtil;

@Controller
@RequestMapping("/jobgroup")
public class JobGroupController  extends BaseController{

	
	@Autowired
	CuckooJobGroupMapper cuckooJobGroupMapper;
	
	@RequestMapping
	public String index0(HttpServletRequest request) {
		
		return index(request);
	}
	
	
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request) {
		
		List<CuckooJobGroup> jobGroups = cuckooJobGroupMapper.selectByExample(new CuckooJobGroupCriteria());
		
		request.setAttribute("jobGroups", jobGroups);
		
		return "jobgroup/jobgroup.index";
	}

	@RequestMapping(value = "/save")
	@ResponseBody
	public Object save(HttpServletRequest request, JobGroup jobGroup){
		
		CuckooJobGroup cuckooJobGroup = new CuckooJobGroup();
		PropertyUtil.copyProperties(cuckooJobGroup, jobGroup);
		
		if(StringUtils.isEmpty(jobGroup.getGroupName())){
			
			throw new BaseException("group name can not be null");
		}
		
		if(jobGroup.getId() != null){
			
			// 更新
			cuckooJobGroupMapper.updateByPrimaryKeySelective(cuckooJobGroup);
		}else{
			// 新增
			cuckooJobGroupMapper.insertSelective(cuckooJobGroup);
		}
		
		return success();
	}
	

	@RequestMapping(value = "/remove")
	@ResponseBody
	public Object remove(HttpServletRequest request, Long id){
		
		if(null == id){
			
			throw new BaseException("id can not be null");
		}
		
		cuckooJobGroupMapper.deleteByPrimaryKey(id);
		return success();
	}
}
