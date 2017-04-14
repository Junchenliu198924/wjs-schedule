package com.wjs.schedule.web.interceptor;

import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.wjs.schedule.constant.CuckooWebConstant;
import com.wjs.schedule.enums.CuckooAdminPages;
import com.wjs.schedule.exception.BaseException;

public class LogonInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 不需要拦截的资源
	 */
	private List<String> excludeUrls;

	public List<String> getExcludeUrls() {
		return excludeUrls;
	}

	public void setExcludeUrls(List<String> excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		super.preHandle(request, response, handler);

		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String uri = requestUri.substring(contextPath.length());
		// 不需要登录即可访问的
		for (String url : excludeUrls) {
			if (isExcludeUrl(request.getContextPath(), uri, url)) {
				return true;
			}
		}


		Object sessionInfo = request.getSession().getAttribute(CuckooWebConstant.ADMIN_WEB_SESSION_USER_KEY);

		// 如果没有登录或登录超时
		if (null == sessionInfo) {
			String requestType = request.getHeader("X-Requested-With");
			// 判断用户请求方式是否为异步请求
			if (StringUtils.isNotBlank(requestType) && requestType.equals("XMLHttpRequest")) {
				throw new BaseException("登录超时，请先登录");
			} else {
				// 未登录时记录上一次操作地址
				String servletPath = request.getServletPath();
				String queryString = request.getQueryString();
				String redirectURL = servletPath;
				if (StringUtils.isNotBlank(queryString)) {
					redirectURL = request.getContextPath() + servletPath + "?" + StringUtils.trimToEmpty(queryString);
				}
				redirectURL = URLEncoder.encode(redirectURL, CuckooWebConstant.ADMIN_WEB_ENCODING);
				String allUrl = CuckooAdminPages.LOGIN.getValue() + "?redirectURL=" + request.getContextPath() + redirectURL;
				// 转到登录页
				response.sendRedirect(allUrl);
			}
			return false;
		} 
//		else {
//			// 转到首页
//			response.sendRedirect(CuckooAdminPages.INDEX.getValue());
//		}

		return true;
	}
	public static void main(String[] args) {
		String uri = "/logon*";
		String url = "/logon";
		System.out.println(uri.substring(0, uri.lastIndexOf("*")));
		System.out.println(url.startsWith(uri.substring(0, uri.lastIndexOf("*"))));
	}

	private boolean isExcludeUrl(String ContextPath, String requestUri, String url) {
		if (StringUtils.isNotEmpty(ContextPath) && requestUri.length() > ContextPath.length()) {
			requestUri = requestUri.substring(ContextPath.length());
		}
		if (requestUri.equals(url)) {
			return true;
		}
		if (url.endsWith("*") && requestUri.startsWith(url.substring(0, url.length() - 2))) {
			return true;
		}
		return false;
	}
}
