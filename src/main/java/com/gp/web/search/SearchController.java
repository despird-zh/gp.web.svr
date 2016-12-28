package com.gp.web.search;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.web.BaseController;

@Controller("search-ctrl")
@RequestMapping("/workgroup")
public class SearchController extends BaseController{

	@RequestMapping("search")
	public ModelAndView doInitial(){
		
		return getJspModelView("search/search");
	}
}
