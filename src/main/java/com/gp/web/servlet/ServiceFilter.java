package com.gp.web.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import com.gp.audit.AccessPoint;
import com.gp.common.JwtPayload;
import com.gp.common.SpringContextUtil;
import com.gp.core.SecurityFacade;
import com.gp.exception.CoreException;
import com.gp.svc.SystemService;
import com.gp.web.BaseController;
import com.gp.util.JwtTokenUtils;

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
		BAD_TOKEN,
		UNKNOWN;
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
		String token = httpRequest.getHeader(AUTH_TOKEN);
		RequestState state = RequestState.UNKNOWN;
		
		if(StringUtils.isBlank(token)){
			// don't have token, forward request to authenticate it
			state = RequestState.NEED_AUTHC;
		}else{
			JwtPayload jwtPayload = JwtTokenUtils.parsePayload(token);
			AccessPoint accesspoint = BaseController.getAccessPoint(httpRequest);
			
		}
		
	}

	/**
	 * forward the request as per the state of request and token
	 * @param request
	 * @param response
	 * @param state 
	 * 
	 **/
	private void forward(ServletRequest request, ServletResponse response, RequestState state){
		RequestDispatcher dispatcher = null;
		try {
			if(RequestState.NEED_AUTHC == state)
				dispatcher = filterConfig.getServletContext().getRequestDispatcher("/gp_svc/authenticate.do");
			else if(RequestState.BAD_TOKEN == state)
				dispatcher = filterConfig.getServletContext().getRequestDispatcher("/gp_svc/bad_token.do");
			else{
				dispatcher = filterConfig.getServletContext().getRequestDispatcher("/gp_svc/trap.do");
			}
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			
			// ignore
		}
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		this.filterConfig = filterConfig;
	}

}
