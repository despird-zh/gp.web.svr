package com.gp.web.view.sysinfo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gp.common.AccessPoint;
import com.gp.common.GPrincipal;
import com.gp.common.Sources;
import com.gp.core.CoreConstants;
import com.gp.core.SourceFacade;
import com.gp.exception.CoreException;
import com.gp.dao.info.SourceInfo;
import com.gp.pagination.PageQuery;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.Source;
import com.gp.web.servlet.ServiceTokenFilter;
import com.gp.web.util.ExWebUtils;

@Controller
@RequestMapping(ServiceTokenFilter.FILTER_PREFIX)
public class ExternInfoController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(ExternInfoController.class);

	@RequestMapping(
			value = "ext-entities-query",
			method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doExternInstanceSearch(@RequestBody String payload){

		if(LOGGER.isDebugEnabled())
			ExWebUtils.dumpRequestAttributes(request);
		
		String name = request.getParameter("source-name");
		PageQuery pq = new PageQuery(5,1);
		// read paging parameter
		readRequestParams( pq);
		ActionResult result = new ActionResult();	
		List<Source> list = new ArrayList<Source>();
		GPrincipal principal = super.getPrincipal();

		try{
			List<SourceInfo> instances = SourceFacade.findSources(getAccessPoint(request), principal, name);
			//
			int cnt = 0;
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
				data.setGlobalId("qwert"+cnt);
				cnt++;
				list.add(data);
			}
			
			result = ActionResult.success(getMessage("mesg.find.instance"));
			result.setData(list);
		}catch(CoreException ce){
			result = super.wrapResult(ce);
		}
		
		ModelAndView mav = getJsonModelView();		
		mav.addAllObjects(result.asMap());

		return mav;		
	}
	
	
	@RequestMapping("ext-entity-save")
	public ModelAndView doSaveExtInstance(HttpServletRequest request){

		Source data = new Source();
		ModelAndView mav = super.getJsonModelView();
		ActionResult rst = new ActionResult();
		// read request parameters
		super.readRequestParams(data);

		GPrincipal princ = super.getPrincipal();
		AccessPoint ap = super.getAccessPoint(request);
		
		SourceInfo instinfo = new SourceInfo();
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
			SourceFacade.saveExtSource(ap, princ, instinfo);
			rst =ActionResult.success(getMessage("mesg.save.instance.ext"));
		}catch(CoreException ce){
			rst = super.wrapResult(ce);
		}
		
		mav.addAllObjects(rst.asMap());
		return mav;
	}
	
	@RequestMapping("ext-entity-info")
	public ModelAndView doGetExternSource(HttpServletRequest request){
		
		if(LOGGER.isDebugEnabled())
			ExWebUtils.dumpRequestAttributes(request);
		
		String globalId = request.getParameter("global_id");
		ActionResult rst = new ActionResult();
		
		Source data = new Source();
		data.setAbbr("ABB1");
		data.setAdmin("admin1");
		data.setBinaryUrl("http://sdlfkj:8090/s");
		data.setServiceUrl("http://sdlfkj:8090/s");
		data.setDescription("this a fake description");
		data.setEmail("sddf@13.com");
		data.setEntityCode("E0010");
		data.setNodeCode("N0011");
		data.setShortName("简称001");
		data.setSourceName("测试外部实例");
		data.setState(Sources.State.ACTIVE.name());
		data.setGlobalId(globalId);
		
		rst = ActionResult.success("取得外部实例成功");
		rst.setData(data);
		
		ModelAndView mav = getJsonModelView();		
		mav.addAllObjects(rst.asMap());

		return mav;	
	}
}
