package com.gp.web.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.web.ActionResult;
import com.gp.web.BaseController;

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
	
	@RequestMapping(
		    value = "/login")
	public ModelAndView login () throws Exception {
		
		ModelAndView mav = super.getJspModelView("main/login");
		return mav;
	}

}
