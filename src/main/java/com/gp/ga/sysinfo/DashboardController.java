package com.gp.ga.sysinfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.web.BaseController;

@Controller("ga-dashboard-ctlr")
@RequestMapping("/ga")
public class DashboardController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);
	
	@RequestMapping("dashboard")
	public ModelAndView doInitial(){
		
		return getJspModelView("ga/sysinfo/dashboard");
	}
	
}
