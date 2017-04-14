package com.wjs.schedule.controller.manage;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wjs.schedule.controller.BaseController;

@Controller
@RequestMapping("/manage")
public class ManageController extends BaseController{

	@RequestMapping
	public String index0(HttpServletRequest request,Long groupId, Long jobId) {

		return index(request, groupId, jobId);
	}

	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request,Long groupId, Long jobId) {
		

		return "manage/manage.index";
	}
}
