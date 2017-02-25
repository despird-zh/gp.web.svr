package com.gp.web.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsProcessor;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.DefaultCorsProcessor;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gp.common.AccessPoint;
import com.gp.common.GroupUsers;
import com.gp.common.IdKey;
import com.gp.common.JwtPayload;
import com.gp.common.Principal;
import com.gp.common.SystemOptions;
import com.gp.core.MasterFacade;
import com.gp.core.SecurityFacade;
import com.gp.dao.info.SysOptionInfo;
import com.gp.dao.info.TokenInfo;
import com.gp.exception.CoreException;
import com.gp.info.InfoId;
import com.gp.util.JwtTokenUtils;
import com.gp.web.util.ExWebUtils;

/**
 * This filter customize as per the CorsFilter, add the authentication process 
 * 
 * @author diaogc
 * @version 0.1 2016-10-20 
 **/
public class ServiceFilter extends OncePerRequestFilter {

	Logger LOGGER = LoggerFactory.getLogger(ServiceFilter.class);
	
	public static final String AUTH_TOKEN = "Auth-Token";
	
	public static final String BLIND_TOKEN = "__blind_token__";
	
	public static final String FILTER_STATE = "_svc_filter_state";
	
	
	/**
	 * the patter will be /p1/p2, remember it will be applied to controller annotation.
	 * so don't change it pattern 
	 **/
	public static final String FILTER_PREFIX = "/gpapi";
	
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
		NEED_FORWARD,
		UNKNOWN;
	}
	
	private final CorsConfigurationSource configSource;

	private CorsProcessor processor = new DefaultCorsProcessor();


	/**
	 * Constructor accepting a {@link CorsConfigurationSource} used by the filter
	 * to find the {@link CorsConfiguration} to use for each incoming request.
	 * @see UrlBasedCorsConfigurationSource
	 */
	public ServiceFilter(CorsConfigurationSource configSource) {
		Assert.notNull(configSource, "CorsConfigurationSource must not be null");
		this.configSource = configSource;
	}


	/**
	 * Configure a custom {@link CorsProcessor} to use to apply the matched
	 * {@link CorsConfiguration} for a request.
	 * <p>By default {@link DefaultCorsProcessor} is used.
	 */
	public void setCorsProcessor(CorsProcessor processor) {
		Assert.notNull(processor, "CorsProcessor must not be null");
		this.processor = processor;
	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {

		if (CorsUtils.isCorsRequest(request)) {
			CorsConfiguration corsConfiguration = this.configSource.getCorsConfiguration(request);
			if (corsConfiguration != null) {
				boolean isValid = this.processor.processRequest(corsConfiguration, request, response);
				if (!isValid || CorsUtils.isPreFlightRequest(request)) {
					return;
				}
			}
		}
		
		/**
		 * Set the buffer size of response 
		 **/
		HttpServletResponse origin = ExWebUtils.getNativeResponse(response, HttpServletResponse.class);
		origin.setBufferSize(402800);
		/**
		 * filterChain.doFilter(request, response);
		 * append further authentication here.
		 **/
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		if(LOGGER.isDebugEnabled()){
			ExWebUtils.dumpRequestAttributes(httpRequest);
			LOGGER.debug("Filter URL:{}", request.getRequestURI());
		}
		String token = httpRequest.getHeader(AUTH_TOKEN);
		RequestState state = RequestState.UNKNOWN;

		AccessPoint accesspoint = ExWebUtils.getAccessPoint(httpRequest);
		if(StringUtils.isBlank(token) || StringUtils.equalsIgnoreCase(BLIND_TOKEN, token)){
			// don't have token, forward request to authenticate it
			state = RequestState.NEED_AUTHC;
		}else{
			JwtPayload jwtPayload = JwtTokenUtils.parsePayload(token);
			if(null == jwtPayload){
				// can not parse the payload
				state = RequestState.BAD_TOKEN;
				
			}else{

				try{
					InfoId<Long> tokenId = IdKey.TOKEN.getInfoId(NumberUtils.toLong(jwtPayload.getJwtId()));
					TokenInfo tokenInfo = SecurityFacade.findToken(accesspoint, tokenId);
					// check if the token record exists
					if(tokenInfo == null){
						// not find any token in db
						state = RequestState.GHOST_TOKEN;
					}else{
						SysOptionInfo secret = MasterFacade.findSystemOption(accesspoint, GroupUsers.PSEUDO_USER, SystemOptions.SECURITY_JWT_SECRET);
						if(!StringUtils.equals(tokenInfo.getJwtToken(), token)
							|| !JwtTokenUtils.verifyHS256(secret.getOptionValue(), token, jwtPayload)){
							state = RequestState.INVALID_TOKEN;
						}
						else{
							state = RequestState.VALID_TOKEN;
							// attach the state to request
							request.setAttribute(FILTER_STATE, state);
							// attach principal to request
							Principal principal = SecurityFacade.findPrincipal(accesspoint, null, jwtPayload.getSubject(), null);
							ExWebUtils.setPrincipal(httpRequest, principal);
							// a valid token, continue the further process
							filterChain.doFilter(request, response);
							return;
						}
					}
				}catch(CoreException ce){
					state = RequestState.BAD_TOKEN;
					LOGGER.error("Fail to get the jwt token record",ce);
				}
			}
		}
		
		// trap all the invalid token request
		request.setAttribute(FILTER_STATE, state);
		forward(request, response, state);
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
		
		FilterConfig filterConfig = this.getFilterConfig();
		
		try {
			if(RequestState.NEED_AUTHC == state){
			
				dispatcher = filterConfig.getServletContext().getRequestDispatcher(FILTER_PREFIX + "/authenticate.do");
			
			}else if(RequestState.BAD_TOKEN == state ||
					RequestState.GHOST_TOKEN == state ||
					RequestState.INVALID_TOKEN == state){
			
				dispatcher = filterConfig.getServletContext().getRequestDispatcher(FILTER_PREFIX + "/bad_token.do");
			
			}else{
				
				dispatcher = filterConfig.getServletContext().getRequestDispatcher(FILTER_PREFIX + "/trap.do");
			}
			
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			
			LOGGER.error("fail to forward the request", e);
			// ignore
		}
	}
}
