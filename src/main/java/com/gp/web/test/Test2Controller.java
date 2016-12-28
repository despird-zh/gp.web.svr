package com.gp.web.test;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.exception.ServiceException;
import com.gp.exception.WebException;
import com.gp.web.BaseController;

@Controller("test2-ctlr")
@RequestMapping("/test")
public class Test2Controller extends BaseController{
	
	static Logger LOGGER = LoggerFactory.getLogger(Test2Controller.class);
	
	@RequestMapping("excep")
	public ModelAndView test(){
		
		ModelAndView mav = super.getJspModelView("test/excep");

		return mav;
	}

	@RequestMapping("web-excep")
	public ModelAndView testWebExcep() throws WebException{
		
		throw new WebException("demo web exception");
	}
	
	@RequestMapping("svc-excep")
	public ModelAndView testSvcExcep() throws ServiceException{
		
		throw new ServiceException("demo web exception");
	}
}
