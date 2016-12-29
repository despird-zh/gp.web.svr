package com.gp.web.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/gp_svc")
public class TrapAllController {
	
	@RequestMapping("trap")
	public ModelAndView doProcessError(HttpServletRequest request){	
		
		return null;
	}
}
