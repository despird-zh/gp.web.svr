package com.gp.web.view;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.common.AccessPoint;
import com.gp.common.GPrincipal;
import com.gp.util.CommonUtils;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.util.ExSecurityUtils;

@Controller
public class DefaultController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(DefaultController.class);
	
	@RequestMapping({ "/", "/index", "/home" })
	public ModelAndView index () throws Exception {
		
		ModelAndView mav = super.getJspModelView("main/dashboard");
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		mav.addObject("user", auth.getName());
		return mav;
	}
	
	@RequestMapping( value = "/login" )
	public ModelAndView login () throws Exception {
		
		ModelAndView mav = super.getJspModelView("main/login");
		return mav;
	}

	@RequestMapping( value = "/principal-context" )
	public ModelAndView principalContext () throws Exception {
		
		ModelAndView mav = super.getJsonModelView();
		GPrincipal princ = ExSecurityUtils.getPrincipal(request);
		AccessPoint ap = super.getAccessPoint(request);
		String token = ExSecurityUtils.generateToken(ap, princ);
		
		Map<String, Object> data = CommonUtils.toMap(princ, Object.class);
		
		data.remove("date_format");
		data.remove("time_zone");
		data.remove("password");
		data.put("user_id", princ.getUserId().getId());
		data.put("token", token);

		return mav.addAllObjects(data);
	}
}
