package com.gp.web.view.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.web.BaseController;

@Controller
public class AccountSyncController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(AccountSyncController.class);
	
	@RequestMapping("account-sync")
	public ModelAndView doInitial(){
		
		return getJspModelView("synctrl/account-sync");
	}
	
	
}
