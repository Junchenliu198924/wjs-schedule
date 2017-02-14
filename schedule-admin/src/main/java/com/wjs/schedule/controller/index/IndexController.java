package com.wjs.schedule.controller.index;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wjs.schedule.controller.BaseController;
import com.wjs.schedule.enums.CuckooAdminPages;


/**
 * index controller
 */
@Controller
public class IndexController extends BaseController{

	@RequestMapping("/")
	public String index(Model model, HttpServletRequest request) {
		
		return redict(CuckooAdminPages.INDEX.getValue());
	}
	
	@RequestMapping("/toLogin")
	public String toLogin(Model model, HttpServletRequest request) {
		
		return CuckooAdminPages.LOGIN.getValue();
	}
	
	
	
}
