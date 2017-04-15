package com.wjs.schedule.controller.manage;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wjs.schedule.controller.BaseController;
import com.wjs.schedule.enums.CuckooUserAuthType;
import com.wjs.schedule.service.auth.CuckooAuthService;
import com.wjs.util.config.ConfigUtil;

@Controller
@RequestMapping("/manage")
public class ManageController extends BaseController{

	
	@Autowired
	CuckooAuthService cuckooAuthService;
	
	@RequestMapping
	public String index0(HttpServletRequest request) {

		return index(request);
	}

	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request) {

		
		if(cuckooAuthService.getLogonInfo().getCuckooUserAuthType().getValue().equals(CuckooUserAuthType.ADMIN.getValue())){
			// 管理员跳转管理页面

			return "manage/manage.index";
		}else{
			// 跳转个人页面

			return "redirect:manage/userdetail?id=" + cuckooAuthService.getLogonInfo().getId()+"&from=mine";
		}
		

	}
	
	@RequestMapping(value = "/userdetail")
	public String userdetail(HttpServletRequest request,Long id,String from) {
		
		if(null != id){
			request.setAttribute("userInfo", cuckooAuthService.getUserInfoById(id));
		}
		request.setAttribute("from", from);

		return "manage/manage.userdetail";

	}
}
