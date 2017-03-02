package com.gp.web.service;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gp.common.AccessPoint;
import com.gp.common.IdKey;
import com.gp.common.Sources;
import com.gp.core.SourceFacade;
import com.gp.dao.info.SourceInfo;
import com.gp.exception.CoreException;
import com.gp.info.InfoId;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.servlet.ServiceFilter;
/**
 *  
 * @author 
 **/
@Controller
@RequestMapping(ServiceFilter.FILTER_PREFIX)
public class EntityProfileController extends BaseController{

	@RequestMapping(
			value = "ent-profile-query.do",
			method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doProfileQuery(@RequestBody String payload){
		
		// the access point
		AccessPoint accesspoint = super.getAccessPoint(request);
		// the model and view
		ModelAndView mav = getJsonModelView();
				
		Map<String,String> paramap = this.readRequestJson(payload);
		Integer entityId = NumberUtils.toInt(paramap.get("instance_id"));
		InfoId<Integer> sourceId = (entityId < 0) ? Sources.LOCAL_INST_ID : IdKey.SOURCE.getInfoId(entityId);
		SourceInfo source = null;
		ActionResult result = null;
		
		try {
			
			source = SourceFacade.findSource(accesspoint, this.getPrincipal(), sourceId);
			result = ActionResult.success(getMessage("mesg.find.instance"));
			result.setData(source);
			
		} catch (CoreException ce) {
			
			result = ActionResult.error(ce.getMessage());
		}
		
		return mav;
				
	}
}
