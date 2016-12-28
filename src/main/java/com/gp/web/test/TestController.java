package com.gp.web.test;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.common.Principal;
import com.gp.exception.WebException;
import com.gp.web.BaseController;

@Controller("test-ctlr")
@RequestMapping("/test")
public class TestController extends BaseController{
	
	static Logger LOGGER = LoggerFactory.getLogger(TestController.class);
	
	@RequestMapping("test")
	public ModelAndView test(){
		Principal principal = super.getPrincipal();
		ModelAndView mav = super.getJspModelView("test/test");
		String msg = getMessage("excp.demo");
		mav.addObject("test", msg);
		TestBean tb = new TestBean();
		tb.setAttr1("attr-value-x1");
		mav.addObject("testbean", tb);
		
		return mav;
	}
	
	@RequestMapping("datatables")
	public ModelAndView datatables(){
		
		ModelAndView mav = super.getJspModelView("test/datatables");
		mav.addObject("test", "test-value-xxx");
		TestBean tb = new TestBean();
		tb.setAttr1("attr-value-x1");
		mav.addObject("testbean", tb);
		
		return mav;
	}
	
	public static class TestBean{
		
		private String attr1;

		public String getAttr1() {
			return attr1;
		}

		public void setAttr1(String attr1) {
			this.attr1 = attr1;
		}		
		
	}
	
	@RequestMapping("test-511")
	public ModelAndView test511(HttpServletResponse response) throws WebException {
		LOGGER.debug("Trying to set timeout on reponse header.");
		ModelAndView mav = super.getJspModelView("error/511");
		return mav;
	}
	
	@RequestMapping("test-relogon")
	public ModelAndView testRelogon(HttpServletResponse response) throws WebException {
		LOGGER.debug("Trying to set timeout on reponse header.");
        response.setCharacterEncoding("UTF-8");
        ((HttpServletResponse)response).setHeader("sessionstatus", "timeout"); 

        	response.setStatus(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value());
        	throw new WebException("Session timeout relogon");
		
	}
}
