package com.gp.web.common;

import com.gp.common.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.web.BaseController;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/common")
public class PersonalController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(PersonalController.class);
	
	public ModelAndView doMessagesSearch(){
		
		return null;
	}
	
	public ModelAndView doNotificationsSearch(){
		return null;
		
	}
	
	public ModelAndView doTasksSearch(){
		return null;
		
	}

	@RequestMapping("user-profile-lite")
	public ModelAndView doGetProfileLite(HttpServletRequest request){

		ModelAndView mav = getJspModelView("dialog/user-profile-lite");
		String account = request.getParameter("account");

		return mav;
	}
}
