package com.gp.ga.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.web.BaseController;

@Controller("ga-audit-ctlr")
@RequestMapping("/ga")
public class AuditCtrlController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(AuditCtrlController.class);
	
	@RequestMapping("audit-ctrl")
	public ModelAndView doInitial(){
		
		return getJspModelView("ga/audit/audit-ctrl");
	}
	
}
