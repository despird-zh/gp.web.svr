package com.gp.ga.workgroup;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.audit.AccessPoint;
import com.gp.common.Principal;
import com.gp.core.WorkgroupFacade;
import com.gp.exception.CoreException;
import com.gp.info.CombineInfo;
import com.gp.dao.info.WorkgroupInfo;
import com.gp.svc.info.WorkgroupExtInfo;
import com.gp.util.DateTimeUtils;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.Workgroup;
import com.gp.web.util.CustomWebUtils;

@Controller("ga-wg-list-ctlr")
@RequestMapping("/ga")
public class WorkgroupListController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(WorkgroupListController.class);
	
	@RequestMapping("workgroup-list")
	public ModelAndView doInitial(){
		
		return getJspModelView("ga/config/workgroup-list");
	}
	
	@RequestMapping("workgroup-local-search")
	public ModelAndView doSearchLocalWorkgroups(HttpServletRequest request){
		
		if(LOGGER.isDebugEnabled())
			CustomWebUtils.dumpRequestAttributes(request);
		
		String wgroupname = super.readRequestParam("wgroup_name");
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);

		ModelAndView mav = getJsonModelView();
		List<Workgroup> list = new ArrayList<Workgroup>();
		try{
			List<WorkgroupExtInfo> ulist = WorkgroupFacade.findLocalWorkgroups(accesspoint, principal, 
					wgroupname);
			for(WorkgroupExtInfo info: ulist){
				
				Workgroup wgroup = new Workgroup();
				wgroup.setWorkgroupId(info.getInfoId().getId());
				wgroup.setWorkgroupName(info.getWorkgroupName());
				wgroup.setAdmin(info.getAdmin());
				wgroup.setDescription(info.getDescription());
				wgroup.setSourceName(info.getSourceName());
				wgroup.setState(info.getState());
				wgroup.setDescription(info.getDescription());
				wgroup.setCreateDate(DateTimeUtils.toYearMonthDay(info.getCreateDate()));
				
				list.add(wgroup);
			}

			mav.addObject(MODEL_KEY_STATE, ActionResult.SUCCESS);
			mav.addObject(MODEL_KEY_MESSAGE, getMessage("mesg.find.wgroup"));
			mav.addObject(MODEL_KEY_DATA, list);
			
		}catch(CoreException ce){

			mav.addObject(MODEL_KEY_STATE, ActionResult.ERROR);
			mav.addObject(MODEL_KEY_MESSAGE, ce.getMessage());
		}

		return mav;
	}
	
	@RequestMapping("workgroup-mirror-search")
	public ModelAndView doSearchMirrorWorkgroups(HttpServletRequest request){
		if(LOGGER.isDebugEnabled())
			CustomWebUtils.dumpRequestAttributes(request);
		
		String wgroupname = super.readRequestParam("wgroup_name");
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		
		ModelAndView mav = getJsonModelView();
		List<Workgroup> list = new ArrayList<Workgroup>();
		try{
			List<WorkgroupExtInfo> ulist = WorkgroupFacade.findMirrorWorkgroups(accesspoint, principal, 
					wgroupname);
			for(WorkgroupExtInfo info: ulist){
				
				Workgroup wgroup = new Workgroup();
				wgroup.setWorkgroupId(info.getInfoId().getId());
				wgroup.setWorkgroupName(info.getWorkgroupName());
				wgroup.setAdmin(info.getAdmin());
				wgroup.setDescription(info.getDescription());
				wgroup.setSourceName(info.getSourceName());
				wgroup.setState(info.getState());
				wgroup.setDescription(info.getDescription());
				wgroup.setCreateDate(DateTimeUtils.toYearMonthDay(info.getCreateDate()));
				wgroup.setEntityCode(info.getEntityCode());
				wgroup.setNodeCode(info.getNodeCode());
				list.add(wgroup);
			}

			mav.addObject(MODEL_KEY_STATE, ActionResult.SUCCESS);
			mav.addObject(MODEL_KEY_MESSAGE, getMessage("mesg.find.wgroup"));

			mav.addObject(MODEL_KEY_DATA, list);
			
		}catch(CoreException ce){

			mav.addObject(MODEL_KEY_STATE, ActionResult.ERROR);
			mav.addObject(MODEL_KEY_MESSAGE, ce.getMessage());
		}

		return mav;
	}
}
