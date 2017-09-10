package com.gp.web.view;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.common.AccessPoint;
import com.gp.common.GPrincipal;
import com.gp.core.MasterFacade;
import com.gp.exception.CoreException;
import com.gp.info.KVPair;
import com.gp.dao.info.SysOptionInfo;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.SysOption;

@Controller
public class SysOptionController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(SysOptionController.class);
	
	@RequestMapping("sys-option")
	public ModelAndView doInitial(){
		
		return getJspModelView("config/sys-option");
	}
//	
//	@RequestMapping("sys-option-groups")
//	public ModelAndView doGetSystemOptionGroups(HttpServletRequest request){
//		
//		ActionResult ars = new ActionResult();
//		List<KVPair<?,?>> groups = new ArrayList<KVPair<?,?>>();
//		GPrincipal princ = super.getPrincipal();
//		AccessPoint ap = super.getAccessPoint(request);
//
//		try{
//			List<String> grst = MasterFacade.findSystemOptionGroups(ap, princ);
//			for(String group : grst){
//				KVPair<String,String> item = new KVPair<String,String>(group,group);
//				groups.add(item);
//			}
//			ars.setData(groups);
//			ars = ActionResult.success(getMessage("mesg.find.sysopt.group"));
//			
//			
//		}catch(CoreException ce){
//			ars = super.wrapResult(ce);
//		}
//
//		ModelAndView mav = super.getJsonModelView();
//		mav.addAllObjects(ars.asMap());
//		
//		return mav;
//	}
//	
//	@RequestMapping("sys-option-search")
//	public ModelAndView doGetSystemOptions(HttpServletRequest request){
//		
//		List<SysOption> rows = new ArrayList<SysOption>();
//		GPrincipal princ = super.getPrincipal();
//		AccessPoint ap = super.getAccessPoint(request);
//		ActionResult ars = new ActionResult();
//		String optgroup = this.readRequestParam("opt_group");
//		ModelAndView mav = super.getJsonModelView();
//		
//		try{
//			List<SysOptionInfo> grst = MasterFacade.findSystemOptions(ap, princ, optgroup);
//
//			for(SysOptionInfo opt : grst){
//				
//				SysOption item = new SysOption();
//				item.setGroup(opt.getOptionGroup());
//				item.setOption(opt.getOptionKey());
//				item.setValue(opt.getOptionValue());
//				item.setDescription(opt.getDescription());
//				
//				rows.add(item);
//			}
//			
//			ars = ActionResult.success(getMessage("mesg.find.sysopts"));
//			ars.setData(rows);
//
//		}catch(CoreException ce){
//			
//			ars = super.wrapResult(ce);
//		}
//		mav.addAllObjects(ars.asMap());
//		return mav;
//	}
//	
//	@RequestMapping("sys-option-save")
//	public ModelAndView doSaveSystemOption(HttpServletRequest request){
//		
//		GPrincipal princ = super.getPrincipal();
//		AccessPoint ap = super.getAccessPoint(request);
//		ActionResult ars = new ActionResult();
//		String optkey = this.readRequestParam("option_key");
//		String optvalue = this.readRequestParam("option_value");
//		ModelAndView mav = super.getJsonModelView();
//		
//		try{
//			MasterFacade.saveSystemOption(ap, princ, optkey, optvalue);
//
//			ars = ActionResult.success(getMessage("mesg.save.sysopt"));
//
//		}catch(CoreException ce){
//			
//			ars = super.wrapResult(ce);
//		}
//		mav.addAllObjects(ars.asMap());
//		return mav;
//	}
}
