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
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import com.gp.audit.AccessPoint;
import com.gp.common.IdKey;
import com.gp.common.JwtPayload;
import com.gp.common.SpringContextUtil;
import com.gp.core.SecurityFacade;
import com.gp.dao.info.TokenInfo;
import com.gp.exception.CoreException;
import com.gp.info.InfoId;
import com.gp.svc.SystemService;
import com.gp.web.BaseController;
import com.gp.util.JwtTokenUtils;

public class ServiceFilter implements Filter{

	Logger LOGGER = LoggerFactory.getLogger(ServiceFilter.class);
	
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
		GHOST_TOKEN,
		INVALID_TOKEN,
		VALID_TOKEN,
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
			if(jwtPayload == null){
				// can not parse the payload
				state = RequestState.BAD_TOKEN;
				
			}else{
				
				AccessPoint accesspoint = BaseController.getAccessPoint(httpRequest);
				try{
					InfoId<Long> tokenId = IdKey.TOKEN.getInfoId(NumberUtils.toLong(jwtPayload.getJwtId()));
					TokenInfo tokenInfo = SecurityFacade.findToken(accesspoint, tokenId);
					
					if(tokenInfo == null){
						// not find any token in db
						state = RequestState.GHOST_TOKEN;
					}else{
						if(!StringUtils.equals(tokenInfo.getJwtToken(), token)
							|| !StringUtils.equals(jwtPayload.getSubject(), tokenInfo.getSubject())){
							state = RequestState.INVALID_TOKEN;
						}
						else{
							state = RequestState.VALID_TOKEN;
							// a valid token, continue the further process
							filterChain.doFilter(request, response);
						}
					}
				}catch(CoreException ce){
					state = RequestState.BAD_TOKEN;
					LOGGER.error("Fail to get the jwt token record",ce);
				}
			}
		}
		// trap all the invalid token request
		if(state != RequestState.VALID_TOKEN){
			forward(request, response, state);
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
			if(RequestState.NEED_AUTHC == state){
			
				dispatcher = filterConfig.getServletContext().getRequestDispatcher("/gp_svc/authenticate.do");
			
			}else if(RequestState.BAD_TOKEN == state ||
					RequestState.GHOST_TOKEN == state ||
					RequestState.INVALID_TOKEN == state){
			
				dispatcher = filterConfig.getServletContext().getRequestDispatcher("/gp_svc/bad_token.do");
			
			}else{
				
				dispatcher = filterConfig.getServletContext().getRequestDispatcher("/gp_svc/trap.do");
			}
			
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			
			LOGGER.error("fail to forward the request", e);
			// ignore
		}
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		this.filterConfig = filterConfig;
	}

}
