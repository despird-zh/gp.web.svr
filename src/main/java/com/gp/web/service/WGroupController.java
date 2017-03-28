package com.gp.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gp.common.AccessPoint;
import com.gp.common.Principal;
import com.gp.core.WorkgroupFacade;
import com.gp.exception.CoreException;
import com.gp.svc.info.WorkgroupExtInfo;
import com.gp.util.DateTimeUtils;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.Workgroup;
import com.gp.web.servlet.ServiceFilter;

@Controller
@RequestMapping(ServiceFilter.FILTER_PREFIX)
public class WGroupController extends BaseController{

	@RequestMapping(
			value = "wgroup-query.do",
			method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doProfileQuery(@RequestBody String payload) {
		
		// the access point
		AccessPoint accesspoint = super.getAccessPoint(request);
		// the model and view
		ModelAndView mav = getJsonModelView();
				
		Map<String,String> paramap = this.readRequestJson(payload);
		String filterkey = paramap.get("filterkey");
		
		ActionResult result = null;
		
		try {
			List<Workgroup> list = new ArrayList<Workgroup>();
			Principal principal = this.getPrincipal();
			List<WorkgroupExtInfo> ulist = WorkgroupFacade.findWorkgroups(accesspoint, principal, 
					filterkey);
			
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
			
			result = ActionResult.success(getMessage("mesg.find.wgroup"));

			result.setData(list);
			
		} catch (CoreException ce) {
			
			result = ActionResult.error(ce.getMessage());
		}
		
		return mav.addAllObjects(result.asMap());
	}
}
