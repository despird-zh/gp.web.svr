package com.gp.web.util;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.gp.common.AccessPoint;
import com.gp.common.GPrincipal;
import com.gp.common.JwtPayload;
import com.gp.core.SecurityFacade;
import com.gp.exception.CoreException;
import com.gp.util.DateTimeUtils;

public class ExSecurityUtils {

	public static GPrincipal getPrincipal(HttpServletRequest request){
		
		GPrincipal princ = ExWebUtils.getPrincipal(request);
		if(null == princ) {
			
			Authentication authen = SecurityContextHolder.getContext().getAuthentication();
			princ = (GPrincipal) authen.getDetails();
		}
		
		return princ;
	}
	
	public static String generateToken(AccessPoint accesspoint, GPrincipal princ) {
		
		JwtPayload payload = new JwtPayload();
		payload.setIssuer("gp.svc.svr");
		payload.setSubject(princ.getAccount());
		payload.setAudience("gp.svc.console");
		
		payload.setNotBefore(DateTimeUtils.now());
		payload.setIssueAt(DateTimeUtils.now());
		payload.setExpireTime(new Date(System.currentTimeMillis() + 60 * 60 * 1000 ));
		
		try {
			String token = SecurityFacade.newToken(accesspoint, payload);
			return token;
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			return "";
		}
	}
}
