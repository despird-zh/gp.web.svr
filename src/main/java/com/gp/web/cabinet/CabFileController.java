package com.gp.web.cabinet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.web.BaseController;

/**
 * This controller wrap the operation on folder 
 **/
@Controller("cab-file-ctrl")
@RequestMapping("/cabinet")
public class CabFileController extends BaseController{
	
	@RequestMapping("file-copy")
	public ModelAndView doCopyFile(HttpServletRequest request){
		return null;
		
	}
	
	@RequestMapping("file-move")
	public ModelAndView doMoveFile(HttpServletRequest request){
		return null;
		
	}
	
	@RequestMapping("file-delete")
	public ModelAndView doDeleteFile(HttpServletRequest request){
		return null;
		
	}
	
	@RequestMapping("file-purge")
	public ModelAndView doPurgeFile(HttpServletRequest request){
		return null;
		
	}
}
