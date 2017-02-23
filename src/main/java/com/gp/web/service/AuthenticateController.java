package com.gp.web.service;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gp.audit.AccessPoint;
import com.gp.common.JwtPayload;
import com.gp.common.Principal;
import com.gp.core.SecurityFacade;
import com.gp.exception.CoreException;
import com.gp.util.DateTimeUtils;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.servlet.ServiceFilter;

@Controller
@RequestMapping(ServiceFilter.FILTER_PREFIX)
public class AuthenticateController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(TrapAllController.class);
	
	@RequestMapping(
		    value = "authenticate.do", 
		    method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doAuthenticate(@RequestBody String payload) throws Exception {
		
		AccessPoint accesspoint = super.getAccessPoint(request);
		// the model and view
		ModelAndView mav = super.getJsonModelView();
		Map<String, String> map = JACKSON_MAPPER.readValue(payload, new TypeReference<Map<String, String>>(){});
		String account = map.get("principal");
		String password = map.get("credential");
		String audience = map.get("audience");
		
		ActionResult result = authenAccount(accesspoint, audience, account, password);
		return mav.addAllObjects(result.asMap());
	}
	
	@RequestMapping(
			value = "authenticate.do",
			method = RequestMethod.GET)
	public ModelAndView doAuthenticate(){	
		// the access point
		AccessPoint accesspoint = super.getAccessPoint(request);
		// the model and view
		ModelAndView mav = super.getJsonModelView();
		
		String account = request.getParameter("principal");
		String password = request.getParameter("credential");
		String audience = request.getParameter("audience");
		
		ActionResult result = authenAccount(accesspoint, audience, account, password);
		
		return mav.addAllObjects(result.asMap());
	}
	
	/**
	 * Verify the password and return the Result 
	 **/
	private ActionResult authenAccount(AccessPoint accesspoint, String audience, String account, String password){
		ActionResult result = null;
		try{
			if(StringUtils.isBlank(audience) ||
					StringUtils.isBlank(account) ||
					StringUtils.isBlank(password) ){
				
				String mesg = super.getMessage("excp.param.miss");
				result = ActionResult.failure(mesg);
			}
			Principal principal = SecurityFacade.findPrincipal(accesspoint, null, account, null);
			if(null == principal){
				String mesg = super.getMessage("excp.no.principal");
				result = ActionResult.failure(mesg);
				
			}else{
				// authenticate the subject & credential
				boolean pass = SecurityFacade.authenticate(accesspoint, principal, password);
				
				if(pass){
					String mesg = super.getMessage("mesg.pwd.pass");
					
					JwtPayload payload = new JwtPayload();
					payload.setIssuer("gp.svc.svr");
					payload.setSubject(account);
					payload.setAudience(audience);
					payload.setNotBefore(DateTimeUtils.now());
					payload.setIssueAt(DateTimeUtils.now());
					payload.setExpireTime(new Date(System.currentTimeMillis() + 60 * 60 * 1000 ));
					
					String token = SecurityFacade.newToken(accesspoint, payload);
					result = ActionResult.success(mesg);
					result.setData(token);
					
				}else{
					String mesg = super.getMessage("excp.pwd.wrong");
					result = ActionResult.failure(mesg);
					
				}
			}
			
		}catch(CoreException ce){
			result = ActionResult.error(ce.getLocalizedMessage());
		
		}

		return result;
	}
}
