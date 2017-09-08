package com.gp.web.view.sysinfo;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.common.AccessPoint;
import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.common.Sources.State;
import com.gp.common.GPrincipal;
import com.gp.core.SourceFacade;
import com.gp.exception.CoreException;
import com.gp.info.InfoId;
import com.gp.dao.info.SourceInfo;
import com.gp.util.CommonUtils;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.Source;
import com.gp.web.util.ExWebUtils;

@Controller
public class BasicInfoController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(BasicInfoController.class);
	
	@RequestMapping("basic")
	public ModelAndView doInitial(){
		
		return getJspModelView("sysinfo/basicinfo");
	}
	
	@RequestMapping("source-info")
	public ModelAndView doGetInstance(HttpServletRequest request){
		
		if(LOGGER.isDebugEnabled())
			ExWebUtils.dumpRequestAttributes(request);
		
		String instanceId = request.getParameter("source_id");
		
		ModelAndView mav = super.getJsonModelView();
		ActionResult rst = new ActionResult();

		if(StringUtils.isBlank(instanceId)){
			rst = ActionResult.error("parameter [instanceid] is required.");
		}
		else if(!CommonUtils.isNumeric(instanceId)){
			rst = ActionResult.error("parameter [instanceid] must be number.");
		}else{
			GPrincipal principal = super.getPrincipal();
			AccessPoint ap = super.getAccessPoint(request);
			
			InfoId<Integer> id = IdKeys.getInfoId(IdKey.GP_SOURCES, Integer.valueOf(instanceId));
						
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
				data.setSourceName(instinfo.getSourceName());
				data.setSourceId(instinfo.getInfoId().getId());
				data.setGlobalId(instinfo.getHashKey());
				
				
				rst = ActionResult.success(getMessage("mesg.find.instance"));
				rst.setData(data);
			}catch(CoreException ce){
				rst = super.wrapResult(ce);
			}	
		}
	
		mav.addAllObjects(rst.asMap());
		return mav;
	}
	
	@RequestMapping("source-change-state")
	public ModelAndView doChangeSourceState(HttpServletRequest request){

		ModelAndView mav = super.getJsonModelView();
		ActionResult rst = new ActionResult();
		String instanceIdStr = request.getParameter("source_id");
		String stateStr = request.getParameter("source_state");
		Integer instanceId = StringUtils.isBlank(instanceIdStr) ? -1 : Integer.valueOf(instanceIdStr);
		InfoId<Integer> id = IdKeys.getInfoId(IdKey.GP_SOURCES,instanceId);
		
		GPrincipal princ = super.getPrincipal();
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
	
	@RequestMapping("source-save")
	public ModelAndView doSaveInstance(HttpServletRequest request){

		Source data = new Source();
		ModelAndView mav = super.getJsonModelView();
		ActionResult rst = new ActionResult();
		// read request parameters
		super.readRequestParams( data);

		InfoId<Integer> id = IdKeys.getInfoId(IdKey.GP_SOURCES, data.getSourceId());
		
		GPrincipal princ = super.getPrincipal();
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
		instinfo.setSourceName(data.getSourceName());
		instinfo.setHashKey(data.getGlobalId());

		try{
			SourceFacade.saveSource(ap, princ, instinfo);
			rst = ActionResult.success(getMessage("mesg.save.instance"));
			
		}catch(CoreException ce){
			
			rst = super.wrapResult(ce);
		}
		
		mav.addAllObjects(rst.asMap());
		return mav;
	}
	
	@RequestMapping("source-search")
	ModelAndView doSearchInstance(HttpServletRequest request){
		
		if(LOGGER.isDebugEnabled())
			ExWebUtils.dumpRequestAttributes(request);
		String name = request.getParameter("source_name");

		GPrincipal princ = super.getPrincipal();
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
				data.setSourceName(instinfo.getSourceName());
				data.setSourceId(instinfo.getInfoId().getId());
				data.setGlobalId(instinfo.getHashKey());
				data.setState(instinfo.getState());
				list.add(data);
			}
			
			rst = ActionResult.success(getMessage("mesg.find.instance"));
			rst.setData(list);
		}catch(CoreException ce){
			
			rst = super.wrapResult(ce);
		}
		ModelAndView mav = getJsonModelView();		
		mav.addAllObjects(rst.asMap());

		return mav;		
	}
}
