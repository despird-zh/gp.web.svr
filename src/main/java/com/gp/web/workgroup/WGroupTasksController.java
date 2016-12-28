package com.gp.web.workgroup;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.web.BaseController;


@Controller
@RequestMapping("/workgroup")
public class WGroupTasksController extends BaseController{

	@RequestMapping("tasks")
	public ModelAndView doInitial(){
		
		ModelAndView mav = getJspModelView("workgroup/tasks");
		String wgid = super.readRequestParam("wgroup_id");
		mav.addObject("wgroup_id",  wgid);
		return mav;
	}
}
