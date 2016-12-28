package com.gp.web.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.web.BaseController;

@Controller("main-ctlr")
@RequestMapping("/main")
public class MainController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(MainController.class);
	
	@RequestMapping("mainpage")
	public ModelAndView doInit(){
		
		return this.getJspModelView("main/main");
	}
}
