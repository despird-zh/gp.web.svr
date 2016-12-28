package com.gp.web.workspace;

import com.gp.common.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.web.BaseController;

@Controller
@RequestMapping("/workspace")
public class WSpaceTasksController extends BaseController{

	private static final long serialVersionUID = 1L;

	@RequestMapping("tasks")
	public ModelAndView doInitial(){
		Principal principal = super.getPrincipal();

		ModelAndView mav = getJspModelView("workspace/tasks");
		mav.addObject("user_id", principal.getUserId().getId());
		return mav;
	}
}
