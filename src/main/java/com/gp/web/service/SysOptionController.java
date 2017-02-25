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
import com.gp.core.MasterFacade;
import com.gp.dao.info.SysOptionInfo;
import com.gp.exception.CoreException;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.SysOption;
import com.gp.web.servlet.ServiceFilter;

@Controller
@RequestMapping(ServiceFilter.FILTER_PREFIX)
public class SysOptionController extends BaseController{

	@RequestMapping(
			value = "sys-opts-query.do",
			method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doSysOptionsQuery(@RequestBody String payload){
		// the access point
		AccessPoint accesspoint = super.getAccessPoint(request);
		// the model and view
		ModelAndView mav = getJsonModelView();
		Map<String,String> paramap = this.readRequestJson(payload);
		
		String group = paramap.get("group");
		List<SysOption> rows = new ArrayList<SysOption>();
		ActionResult result = null;
		try{
			List<SysOptionInfo> opts = MasterFacade.findSystemOptions(accesspoint, this.getPrincipal(), group);
			
			for(SysOptionInfo opt : opts){
				
				SysOption item = new SysOption();
				item.setGroup(opt.getOptionGroup());
				item.setOption(opt.getOptionKey());
				item.setValue(opt.getOptionValue());
				item.setDescription(opt.getDescription());
				
				rows.add(item);
			}
			
			result = ActionResult.success(getMessage("mesg.find.sysopts"));
			result.setData(rows);
			
		}catch(CoreException ce){
			
			result = ActionResult.error(ce.getMessage());
		}
		
		return mav.addAllObjects(result.asMap());
	}
}
