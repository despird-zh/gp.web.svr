package com.gp.web.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.audit.AccessPoint;
import com.gp.exception.WebException;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.shiro.AuthenToken;
import com.gp.web.util.CustomWebUtils;

@Controller("auth-ctlr")
@RequestMapping("/main")
public class AuthController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
	
	public static final int RELOG_INDICATOR = 444;
	
	@SuppressWarnings("deprecation")
	@RequestMapping("login")
	public ModelAndView doLogon(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Subject currentUser = SecurityUtils.getSubject();
		// If not authenticated and request is XMLHttpRequest, handle it as ajax SessionTimeout, indicate client to relogon
		if(!currentUser.isAuthenticated() && 
			"XMLHttpRequest".equalsIgnoreCase((request).getHeader("X-Requested-With"))){

	        LOGGER.debug("Trying to set timeout on reponse header.");
	        response.setCharacterEncoding("UTF-8");
	        ((HttpServletResponse)response).setHeader("sessionstatus", "timeout"); 
	        // here we set status to 511 force the client to process it, 
	        // the global exception advice also adapt it to re-logon page.
			response.setStatus(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value(), "Need relogon!!!" );
			throw new WebException("Session timeout relogon");
		}

		return getJspModelView("main/login");
	}

	@RequestMapping("authenticate")
	public ModelAndView doAuth(HttpServletRequest request, HttpServletResponse response) throws WebException{
		
		AccessPoint ap = CustomWebUtils.getAccessPoint(request);
		if(LOGGER.isDebugEnabled())
			CustomWebUtils.dumpRequestAttributes(request);
		
		String username = request.getParameter("account");
		String password = request.getParameter("password");
		AuthenToken atoken = new AuthenToken(username, password, ap);
		
	    Subject currentUser = SecurityUtils.getSubject();
	    ActionResult result = new ActionResult();
		try {
			currentUser.login(atoken);
			result.setState(ActionResult.SUCCESS);
			result.setMessage("Logon OK");
			LOGGER.info("AUTH SUCCESS");
		} catch (AuthenticationException ae) {
			LOGGER.error("AUTH MSSG: " + ae.getMessage(),ae);
			result.setState(ActionResult.ERROR);
			result.setMessage(ae.getMessage());
		}	    		
		
		ModelAndView mav = this.getJsonModelView();
		mav.addAllObjects(result.asMap());
		return mav;
	}

}
