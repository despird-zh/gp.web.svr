package com.gp.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gp.common.AccessPoint;
import com.gp.common.Principal;
import com.gp.core.SecurityFacade;
import com.gp.exception.CoreException;
import com.gp.svc.info.UserExtInfo;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.servlet.ServiceFilter;

@Controller
@RequestMapping(ServiceFilter.FILTER_PREFIX)
public class UserController extends BaseController{

	@RequestMapping(
			value = "users-query.do",
			method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doUsersQuery(@RequestBody String payload){
		
		// the access point
		AccessPoint accesspoint = super.getAccessPoint(request);
		Principal principal = super.getPrincipal();
		// the model and view
		ModelAndView mav = getJsonModelView();
		Map<String,String> paramap = this.readRequestJson(payload);
		String[] a = new String[0];
		ActionResult result = null;
		try{
			List<UserExtInfo> ulist = SecurityFacade.findAccounts(getAccessPoint(request), principal, 
					"", // name
					12,  // entity
					a,  // type
					a); // state
		}catch(CoreException ce){
			
			result = ActionResult.error(ce.getMessage());
		}
		return mav.addAllObjects(result.asMap());
	}
}
