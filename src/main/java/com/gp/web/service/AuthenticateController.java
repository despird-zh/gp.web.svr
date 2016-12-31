package com.gp.web.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gp.audit.AccessPoint;
import com.gp.common.Principal;
import com.gp.core.SecurityFacade;
import com.gp.exception.CoreException;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;

@Controller
@RequestMapping("/gp_svc")
public class AuthenticateController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(TrapAllController.class);
	
	@RequestMapping(
		    value = "authenticate.do", 
		    method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doAuthenticate(HttpServletRequest request, @RequestBody String payload) throws Exception {
		
		AccessPoint accesspoint = super.getAccessPoint(request);
		// the model and view
		ModelAndView mav = super.getJsonModelView();
		Map<String, String> map = JACKSON_MAPPER.readValue(payload, new TypeReference<Map<String, String>>(){});
		String account = map.get("principal");
		String password = map.get("credential");
		
		ActionResult result = verifyAccount(accesspoint, account, password);
		return mav.addAllObjects(result.asMap());
	}
	
	@RequestMapping(
			value = "authenticate.do",
			method = RequestMethod.GET)
	public ModelAndView doAuthenticate(HttpServletRequest request){	
		// the access point
		AccessPoint accesspoint = super.getAccessPoint(request);
		// the model and view
		ModelAndView mav = super.getJsonModelView();
		
		String account = request.getParameter("principal");
		String password = request.getParameter("credential");
		ActionResult result = verifyAccount(accesspoint, account, password);
		
		return mav.addAllObjects(result.asMap());
	}
	
	/**
	 * Verify the password and return the Result 
	 **/
	private ActionResult verifyAccount(AccessPoint accesspoint, String account, String password){
		ActionResult result = new ActionResult();
		Map<String, String> data = new HashMap<String, String>();
		try{
			
			Principal principal = SecurityFacade.findPrincipal(accesspoint, null, account, null);
			if(null == principal){
				String mesg = super.getMessage("excp.no.principal");
				data.put("AUTH_CODE", "ACCOUNT_NOT_EXIST");
				data.put("AUTH_MESG", mesg);
			}else{
				boolean pass = SecurityFacade.authenticate(accesspoint, principal, password);
				
				if(pass){
					String mesg = super.getMessage("excp.pwd.pass");
					data.put("AUTH_CODE", "AUTH_PASS");
					data.put("AUTH_MESG", mesg);
				}else{
					String mesg = super.getMessage("excp.pwd.wrong");
					data.put("AUTH_CODE", "AUTH_FAIL");
					data.put("AUTH_MESG", mesg);
				}
			}
			
		}catch(CoreException ce){
			data.put("AUTH_CODE", "AUTH_FAIL");
			data.put("AUTH_MESG", ce.getLocalizedMessage());
		}
		
		result.setData(data);
		return result;
	}
}
