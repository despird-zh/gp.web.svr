package com.gp.web.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DebugController {

	@RequestMapping(
		    value = "/debug")
	public String login () throws Exception {
		
		return "debug/debug";
	}
}
