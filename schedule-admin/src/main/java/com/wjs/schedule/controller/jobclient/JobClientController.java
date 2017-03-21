package com.wjs.schedule.controller.jobclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wjs.schedule.controller.BaseController;
import com.wjs.schedule.domain.exec.CuckooClientJobDetail;
import com.wjs.schedule.service.Job.CuckooClientJobDetailService;
import com.wjs.schedule.service.Job.CuckooJobService;
import com.wjs.schedule.vo.job.CuckooClientJobDetailVo;
import com.wjs.schedule.vo.qry.JobClientQry;
import com.wjs.schedule.web.util.JqueryDataTable;
import com.wjs.util.bean.PropertyUtil;
import com.wjs.util.dao.PageDataList;

@Controller
@RequestMapping("/jobclient")
public class JobClientController  extends BaseController{


	@Autowired
	CuckooJobService cuckooJobService;
	
	@Autowired
	CuckooClientJobDetailService cuckooClientJobDetailService;
	@RequestMapping
	public String index0(HttpServletRequest request) {
		
		return index(request);
	}
	
	
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request) {
		
		// APP应用
		Map<String,String> jobAppList = cuckooJobService.findAllApps();
		Map<String,String> jobAppWithNull = new HashMap<>();
		jobAppWithNull.put("", "全部/无");
		jobAppWithNull.putAll(jobAppList);
		request.setAttribute("jobAppWithNull", jobAppWithNull);
		
		return "jobclient/jobclient.index";
	}
	
	@ResponseBody
	@RequestMapping(value = "/pageList")
	public Object pageList(JobClientQry qry){
	
		PageDataList<CuckooClientJobDetail> page = cuckooClientJobDetailService.pageData(qry);
		
		List<CuckooClientJobDetailVo> rows = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(page.getRows())){
			for (CuckooClientJobDetail r : page.getRows()) {
				CuckooClientJobDetailVo vo = new CuckooClientJobDetailVo();
				PropertyUtil.copyProperties(vo, r);
				rows.add(vo);
			}
		}
		
		PageDataList<CuckooClientJobDetailVo> pages = new PageDataList<>();
		pages.setPage(page.getPage());
		pages.setPageSize(page.getPageSize());
		pages.setTotal(page.getTotal());
		pages.setRows(rows);
		
		return dataTable(pages);
	}
	
	/**
	 * parse PageDataList to JqueryDataTable
	 * @param page
	 * @return
	 */
	public <T> JqueryDataTable<T>  dataTable(PageDataList<T> page) {
		JqueryDataTable<T> t = new JqueryDataTable<>();
		t.setRecordsFiltered(page.getTotal());
		t.setRecordsTotal(page.getTotal());

		t.setData(page.getRows());
		
		return t;
	}
}
