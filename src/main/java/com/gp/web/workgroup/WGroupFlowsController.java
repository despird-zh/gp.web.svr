package com.gp.web.workgroup;

import com.gp.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/workgroup")
public class WGroupFlowsController extends BaseController{

	@RequestMapping("flows")
	public ModelAndView doInitial(){
		
		ModelAndView mav = getJspModelView("workgroup/flows");
		String wgid = super.readRequestParam("wgroup_id");
		mav.addObject("wgroup_id",  wgid);
		return mav;
	}
}
