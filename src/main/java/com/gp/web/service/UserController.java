package com.gp.web.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gp.common.AccessPoint;
import com.gp.common.Principal;
import com.gp.common.Sources;
import com.gp.core.SecurityFacade;
import com.gp.exception.CoreException;
import com.gp.svc.info.UserExtInfo;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.servlet.ServiceFilter;

@Controller
@RequestMapping(ServiceFilter.FILTER_PREFIX)
public class UserController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
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
		LOGGER.debug("params {}" , paramap);
		
		String[] states = new String[]{paramap.get("state")};
		if("ALL".equals(paramap.get("state")))
			states = new String[0];
		
		Integer sourceId = null;
		if(!"ALL".equals(paramap.get("type")))
			sourceId = NumberUtils.toInt(paramap.get("type"));
		
		ActionResult result = null;
		try{
			List<UserExtInfo> ulist = SecurityFacade.findAccounts(accesspoint, principal, 
					paramap.get("filterkey"), // name
					sourceId,  // entity
					new String[0],  // type
					states); // state
			
			result = ActionResult.success(this.getMessage("mesg.find.account"));
			result.setData(ulist);
		}catch(CoreException ce){
			
			result = ActionResult.error(ce.getMessage());
		}
		return mav.addAllObjects(result.asMap());
	}
}
