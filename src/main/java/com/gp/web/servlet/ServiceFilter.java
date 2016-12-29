package com.gp.web.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class ServiceFilter implements Filter{

	public static final String AUTH_TOKEN = "auth-token";
	
	@Override
	public void destroy() {
		// ignore
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		String token = httpRequest.getHeader("auth-token");
		
		//request.get
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
