package com.gp.web.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import com.gp.common.SpringContextUtil;
import com.gp.svc.SystemService;

public class ServiceFilter implements Filter{

	public static final String AUTH_TOKEN = "auth-token";
	
	private FilterConfig filterConfig = null;
	
	public static final String FILTER_PREFIX = "/gp_svc";
	
	/**
	 * Define the state of request 
	 **/
	public static enum RequestState{
		
		NEED_AUTHC,
		FAIL_AUTHC,
		WRONG_TOKEN,
		;
	}
	
	@Autowired
	SystemService systemsvc;
	
	public ServiceFilter(){
		// here let autowired annotation work
		AutowireCapableBeanFactory awfactory = SpringContextUtil.getApplicationContext().getAutowireCapableBeanFactory();
		awfactory.autowireBean(this);
	}
	
	@Override
	public void destroy() {
		// ignore
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		String token = httpRequest.getHeader("auth-token");
		
		if(StringUtils.isBlank(token)){
			
		}
		
	}

	private void trap(ServletRequest request, ServletResponse response){
		
		try {
			filterConfig.getServletContext().getRequestDispatcher("/gp_svc/trap.do").
			forward(request, response);
		} catch (ServletException | IOException e) {
			
			// ignore
		}
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		this.filterConfig = filterConfig;
	}

}
