package com.wjs.schedule.controller.logon;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wjs.schedule.constant.CuckooWebConstant;
import com.wjs.schedule.controller.BaseController;
import com.wjs.schedule.enums.CuckooAdminPages;
import com.wjs.schedule.exception.BaseException;

@Controller
@RequestMapping("/logon")
public class LogonController extends BaseController{
	
	@RequestMapping("/index")
	public Object logon(HttpServletRequest request,String redirectUrl){

		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		System.out.println(requestUri +" _ " + contextPath);

		return CuckooAdminPages.LOGIN.getValue();
	}
	
	
	@RequestMapping("/in")
	@ResponseBody
	public Object login(HttpServletRequest request,String user, String pwd){
		
		// 查询数据库
		if(!"guest".equals(user) || !"guest".equals(pwd)){
			
			throw new BaseException("unknow userName:{},password:{},please try using [guest,guest]", user, pwd);
		}
		
		request.getSession().setAttribute(CuckooWebConstant.ADMIN_WEB_SESSION_USER_KEY, "guest");
		


		 
		return success(CuckooAdminPages.INDEX.getValue());
	}
	
	
	@RequestMapping("/out")
	@ResponseBody
	public Object logout(HttpServletRequest request,String redirectUrl){
		
		
		request.getSession().invalidate();
		

		request.setAttribute("redirectUrl", redirectUrl);
		if(null != redirectUrl){
		
			return success(redirectUrl);
		}
		return success(CuckooAdminPages.INDEX.getValue());
	}
}
