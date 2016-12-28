package com.gp.ga.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.audit.AccessPoint;
import com.gp.common.Principal;
import com.gp.common.Storages;
import com.gp.common.Storages.StoreSetting;
import com.gp.core.StorageFacade;
import com.gp.exception.CoreException;
import com.gp.info.InfoId;
import com.gp.dao.info.StorageInfo;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.Storage;
import com.gp.web.util.CustomWebUtils;

@Controller("ga-storage-new-ctlr")
@RequestMapping("/ga")
public class StorageAddController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(StorageAddController.class);

	@RequestMapping("storage-new")
	public ModelAndView doInitial(HttpServletRequest request){
		
		ModelAndView mav = getJspModelView("ga/config/storage-new");

		return mav;
	}
		
	@RequestMapping("storage-add")
	public ModelAndView doNewStorage(HttpServletRequest request){
		
		if(LOGGER.isDebugEnabled())
			CustomWebUtils.dumpRequestAttributes(request);
		// read parameter
		Storage storage = new Storage();
		super.readRequestData(request, storage);
		// read trace information
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		// prepare result
		ActionResult aresult = new ActionResult();
		ModelAndView mav = super.getJsonModelView();
		
		StorageInfo sinfo = new StorageInfo();
		Long cap = StringUtils.isNotBlank(storage.getCapacity()) ? Long.valueOf(storage.getCapacity()):0l;
		sinfo.setCapacity(cap);
		sinfo.setDescription(storage.getDescription());
		// convert setting into json string
		Map<String, Object> setting = new HashMap<String, Object>();
		setting.put(StoreSetting.StorePath.name(), storage.getStorePath());
		setting.put(StoreSetting.HdfsHost.name(), storage.getHdfsHost());
		setting.put(StoreSetting.HdfsPort.name(), storage.getHdfsPort());
		// try to save setting
		sinfo.setSettingJson(Storages.wrapSetting(setting));
		sinfo.setState(storage.getState());
		sinfo.setStorageType(storage.getType());
		sinfo.setStorageName(storage.getName());
		sinfo.setUsed(0l);
		
		try{
			InfoId<Integer> gresult = StorageFacade.newStorage(accesspoint, principal, sinfo);
			aresult.setState(ActionResult.SUCCESS);
			aresult.setMessage(getMessage("mesg.new.storage"));
		}catch(CoreException ce){
			aresult.setState(ActionResult.ERROR);
			aresult.setMessage(ce.getMessage());
			aresult.setDetailmsgs(ce.getValidateMessages());
		}
		
		mav.addAllObjects(aresult.asMap());
		return mav;
	}
	
}
