package com.gp.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.servlet.ServiceFilter;

@Controller
@RequestMapping(ServiceFilter.FILTER_PREFIX)
public class TrapAllController extends BaseController{
	
	static Logger LOGGER = LoggerFactory.getLogger(TrapAllController.class);
	
	/**
	 * trap all the illegal process 
	 **/
	@RequestMapping("trap")
	public ModelAndView doTrap(){	
		
		ModelAndView mav = super.getJsonModelView();
		ActionResult result = null;
		
		result = ActionResult.failure(this.getMessage("excp.invalid.token"));
		
		return mav.addAllObjects(result.asMap());
	}

	/**
	 * Process the bad token request. 
	 **/
	@RequestMapping("bad_token")
	public ModelAndView doBadToken(){	
		
		ModelAndView mav = super.getJsonModelView();
		ActionResult result = new ActionResult();
		
		result = ActionResult.failure(this.getMessage("excp.invalid.token"));

		return mav.addAllObjects(result.asMap());
	}
}
