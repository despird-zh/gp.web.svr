package com.gp.web.service;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.audit.AccessPoint;
import com.gp.common.Principal;
import com.gp.exception.CoreException;
import com.gp.web.BaseController;

@Controller
@RequestMapping("/gp_svc")
public class TrapAllController {
	
	static Logger LOGGER = LoggerFactory.getLogger(TrapAllController.class);
	
	@RequestMapping("trap")
	public ModelAndView doTrap(HttpServletRequest request){	
		
		return null;
	}
	
	@RequestMapping("authenticate.do")
	public ModelAndView doAuthenticate(HttpServletRequest request){	
		AccessPoint accesspoint = BaseController.getAccessPoint(request);
		LOGGER.debug("------", accesspoint.getApp());
//		try{
//			Principal SecurityFacade.findPrincipal(accesspoint, null, jwtPayload.getSubject(), null);
//			
//		}catch(CoreException ce){
//			
//		}
		return null;
	}
	
	@RequestMapping("bad_token")
	public ModelAndView doBadToken(HttpServletRequest request){	
		
		return null;
	}
}
