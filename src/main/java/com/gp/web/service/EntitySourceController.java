package com.gp.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gp.common.AccessPoint;
import com.gp.common.IdKey;
import com.gp.common.Sources.State;
import com.gp.common.Principal;
import com.gp.core.SourceFacade;
import com.gp.exception.CoreException;
import com.gp.info.InfoId;
import com.gp.dao.info.SourceInfo;
import com.gp.util.CommonUtils;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.Source;
import com.gp.web.servlet.ServiceFilter;
import com.gp.web.util.ExWebUtils;

@Controller
@RequestMapping(ServiceFilter.FILTER_PREFIX)
public class EntitySourceController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(EntitySourceController.class);
	
	@RequestMapping(
			value = "entity-info",
			method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doGetInstance(@RequestBody String payload){
		
		if(LOGGER.isDebugEnabled())
			ExWebUtils.dumpRequestAttributes(request);
		
		Map<String,String> paramap = this.readRequestJson(payload);
		String instanceId = paramap.get("source_id");
		
		ModelAndView mav = super.getJsonModelView();
		ActionResult rst = null;

		if(StringUtils.isBlank(instanceId)){
			rst = ActionResult.error("parameter [instanceid] is required.");
		}
		else if(!CommonUtils.isNumeric(instanceId)){
			rst = ActionResult.error("parameter [instanceid] must be number.");
		}else{
			Principal principal = super.getPrincipal();
			AccessPoint ap = super.getAccessPoint(request);
			
			InfoId<Integer> id = IdKey.SOURCE.getInfoId(Integer.valueOf(instanceId));
			
			
			try{
				SourceInfo instinfo  = SourceFacade.findSource(ap, principal, id);
				Source data = new Source();
				
				data.setAbbr(instinfo.getAbbr());
				data.setAdmin(instinfo.getAdmin());
				data.setBinaryUrl(instinfo.getBinaryUrl());
				data.setServiceUrl(instinfo.getServiceUrl());
				data.setDescription(instinfo.getDescription());
				data.setEmail(instinfo.getEmail());
				data.setEntityCode(instinfo.getEntityCode());
				data.setNodeCode(instinfo.getNodeCode());
				data.setShortName(instinfo.getShortName());
				data.setName(instinfo.getSourceName());
				data.setSourceId(instinfo.getInfoId().getId());
				data.setGlobalId(instinfo.getHashKey());
				
				rst = ActionResult.success(getMessage("mesg.find.instance"));
				rst.setData(data);
				
			}catch(CoreException ce){
				rst = ActionResult.error(ce.getMessage());
			}	
		}
	
		mav.addAllObjects(rst.asMap());
		return mav;
	}
	
	@RequestMapping(
			value = "entity-change-state",
			method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doChangeSourceState(@RequestBody String payload){

		ModelAndView mav = super.getJsonModelView();
		ActionResult rst = new ActionResult();
		Map<String,String> paramap = this.readRequestJson(payload);

		String instanceIdStr = paramap.get("source_id");
		String stateStr = paramap.get("source_state");
		Integer instanceId = StringUtils.isBlank(instanceIdStr) ? -1 : Integer.valueOf(instanceIdStr);
		InfoId<Integer> id = IdKey.SOURCE.getInfoId(instanceId);
		
		Principal princ = super.getPrincipal();
		AccessPoint ap = super.getAccessPoint(request);

		try{
			SourceFacade.changeSourceState(ap, princ, id, State.valueOf(stateStr));
			rst = ActionResult.success(getMessage("mesg.change.instance.state"));
			
		}catch(CoreException ce){
			
			rst = super.wrapResult(ce);
		}
		
		mav.addAllObjects(rst.asMap());
		return mav;
	}
	
	@RequestMapping(
			value = "entity-save",
			method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doSaveInstance(@RequestBody String payload){

		ModelAndView mav = super.getJsonModelView();
		ActionResult rst = new ActionResult();
		// read request parameters
		Source data = this.readRequestBody(payload, Source.class);
		
		InfoId<Integer> id = IdKey.SOURCE.getInfoId(data.getSourceId());
		
		Principal princ = super.getPrincipal();
		AccessPoint ap = super.getAccessPoint(request);
		
		SourceInfo instinfo = new SourceInfo();
		instinfo.setInfoId(id);
		instinfo.setAbbr(data.getAbbr());
		instinfo.setAdmin(data.getAdmin());
		instinfo.setBinaryUrl(data.getBinaryUrl());
		instinfo.setServiceUrl(data.getServiceUrl());
		instinfo.setDescription(data.getDescription());
		instinfo.setEmail(data.getEmail());
		instinfo.setEntityCode(data.getEntityCode());
		instinfo.setNodeCode(data.getNodeCode());
		instinfo.setShortName(data.getShortName());
		instinfo.setSourceName(data.getName());
		instinfo.setHashKey(data.getGlobalId());

		try{
			SourceFacade.saveSource(ap, princ, instinfo);
			rst = ActionResult.error(getMessage("mesg.save.instance"));
			
		}catch(CoreException ce){
			
			rst = super.wrapResult(ce);
		}
		
		mav.addAllObjects(rst.asMap());
		return mav;
	}
	
	@RequestMapping(
			value = "entities-query",
			method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	ModelAndView doSearchInstance(@RequestBody String payload){
		
		if(LOGGER.isDebugEnabled())
			ExWebUtils.dumpRequestAttributes(request);
		
		Map<String,String> paramap = this.readRequestJson(payload);
		String name = paramap.get("source_name");

		Principal princ = super.getPrincipal();
		AccessPoint ap = super.getAccessPoint(request);
		List<Source> list = new ArrayList<Source>();
		ActionResult rst = new ActionResult();
		
		try{
			
			List<SourceInfo> instances = SourceFacade.findSources(ap, princ, name);
			for(SourceInfo instinfo: instances){
				Source data = new Source();
				data.setAbbr(instinfo.getAbbr());
				data.setAdmin(instinfo.getAdmin());
				data.setBinaryUrl(instinfo.getBinaryUrl());
				data.setServiceUrl(instinfo.getServiceUrl());
				data.setDescription(instinfo.getDescription());
				data.setEmail(instinfo.getEmail());
				data.setEntityCode(instinfo.getEntityCode());
				data.setNodeCode(instinfo.getNodeCode());
				data.setShortName(instinfo.getShortName());
				data.setName(instinfo.getSourceName());
				data.setSourceId(instinfo.getInfoId().getId());
				data.setGlobalId(instinfo.getHashKey());
				data.setState(instinfo.getState());
				list.add(data);
			}
			rst.setData(list);
			rst = ActionResult.success(getMessage("mesg.find.instance"));
		}catch(CoreException ce){
			
			rst = super.wrapResult(ce);
		}
		ModelAndView mav = getJsonModelView();		
		mav.addAllObjects(rst.asMap());

		return mav;		
	}
}
