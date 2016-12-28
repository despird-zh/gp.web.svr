package com.gp.ga.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.web.BaseController;

@Controller("ga-accs-ctlr")
@RequestMapping("/ga")
public class AccessCtrlController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(AccessCtrlController.class);

	@RequestMapping("access-ctrl")
	public ModelAndView doInitial(){
		
		return getJspModelView("ga/audit/access-ctrl");
	}
	
}
