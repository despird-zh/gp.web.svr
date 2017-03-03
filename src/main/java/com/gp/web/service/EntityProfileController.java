package com.gp.web.service;

import java.text.DateFormat;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gp.common.AccessPoint;
import com.gp.common.IdKey;
import com.gp.common.Principal;
import com.gp.common.Sources;
import com.gp.core.SourceFacade;
import com.gp.dao.info.SourceInfo;
import com.gp.exception.CoreException;
import com.gp.info.InfoId;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.Source;
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
		InfoId<Integer> sourceId = (entityId <= 0) ? Sources.LOCAL_INST_ID : IdKey.SOURCE.getInfoId(entityId);
		Source source = new Source();
		ActionResult result = null;
		
		try {
			Principal principal = this.getPrincipal();
			SourceInfo instinfo = SourceFacade.findSource(accesspoint, principal, sourceId);

			source.setAbbr(instinfo.getAbbr());
			source.setAdmin(instinfo.getAdmin());
			source.setBinaryUrl(instinfo.getBinaryUrl());
			source.setServiceUrl(instinfo.getServiceUrl());
			source.setDescription(instinfo.getDescription());
			source.setEmail(instinfo.getEmail());
			source.setEntityCode(instinfo.getEntityCode());
			source.setNodeCode(instinfo.getNodeCode());
			source.setShortName(instinfo.getShortName());
			source.setName(instinfo.getSourceName());
			source.setSourceId(instinfo.getInfoId().getId());
			source.setGlobalId(instinfo.getHashKey());
			
			DateFormat datefmt = principal.getDateFormat();
			source.setModifier(instinfo.getModifier());
			source.setLastModified(datefmt.format(instinfo.getModifyDate()));
			
			result = ActionResult.success(getMessage("mesg.find.instance"));

			result.setData(source);
			
		} catch (CoreException ce) {
			
			result = ActionResult.error(ce.getMessage());
		}
		
		return mav.addAllObjects(result.asMap());
				
	}
}
