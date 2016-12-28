package com.gp.ga.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.audit.AccessPoint;
import com.gp.common.IdKey;
import com.gp.common.Principal;
import com.gp.common.Storages;
import com.gp.common.Storages.StorageState;
import com.gp.common.Storages.StorageType;
import com.gp.common.Storages.StoreSetting;
import com.gp.core.StorageFacade;
import com.gp.exception.CoreException;
import com.gp.info.InfoId;
import com.gp.dao.info.StorageInfo;
import com.gp.util.CommonUtils;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.Storage;
import com.gp.web.util.CustomWebUtils;

@Controller("ga-storage-list-ctlr")
@RequestMapping("/ga")
public class StorageListController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(StorageListController.class);
	static final String ALL_OPTION = "ALL";
	
	static final String VIEW_TAB_LIST = "list";
	static final String VIEW_TAB_MODIFY = "modify";
	
	@RequestMapping("storage-list")
	public ModelAndView doInitial(HttpServletRequest request){
		
		ModelAndView mav = getJspModelView("ga/config/storage-list");
		String viewtab = super.readRequestParam("viewtab");
		
		int exist = ArrayUtils.indexOf(new String[]{VIEW_TAB_LIST,VIEW_TAB_MODIFY}, viewtab);
		if(-1 == exist){
			viewtab = VIEW_TAB_LIST;
		}
		mav.addObject("viewtab", viewtab);
		
		return mav;
	}
	
	@RequestMapping("storage-search")
	public ModelAndView doSearchSearch(HttpServletRequest request){
		
		List<Storage> rows = new ArrayList<Storage>();

		String namecond = this.readRequestParam("storage_name");
		String type = this.readRequestParam("storage_type");
		String state = this.readRequestParam("storage_state");
		String[] types = null;
		if(ALL_OPTION.equals(type)){
			types = new String[]{
					StorageType.DISK.name(),
					StorageType.HDFS.name()
				};
		}else{
			types = new String[]{type};
		}
		String[] states = null;
		if(ALL_OPTION.equals(state)){
			states = new String[]{
					StorageState.CLOSE.name(),
					StorageState.FULL.name(),
					StorageState.OPEN.name()};
		}else{
			states = new String[]{state};
		}
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		ActionResult aresult = new ActionResult();
		ModelAndView mav = super.getJsonModelView();
		
		try{
			List<StorageInfo> gresult = StorageFacade.findStorages(accesspoint, 
				principal, 
				namecond, types, states);
		
			for(StorageInfo sinfo : gresult){
				
				Storage storage = new Storage();
				storage.setStorageId(sinfo.getInfoId().getId());
				storage.setName(sinfo.getStorageName());
				storage.setState(sinfo.getState());
				storage.setType(sinfo.getStorageType());
				storage.setDescription(sinfo.getDescription());
				String cap = CommonUtils.humanReadableByteCount(sinfo.getCapacity() * 1024 * 1024);
				storage.setCapacity(cap);
				String used = CommonUtils.humanReadableByteCount(sinfo.getUsed() * 1024 * 1024);
				storage.setUsed(used);
				int percent = (int)((double)sinfo.getUsed()/(double)sinfo.getCapacity()*100);
				storage.setPercent(percent);
				// parse setting json into map 
				Map<String, Object> setting = Storages.parseSetting(sinfo.getSettingJson());
				storage.setStorePath((String)setting.get(StoreSetting.StorePath.name()));
				storage.setHdfsHost((String)setting.get(StoreSetting.HdfsHost.name()));
				storage.setHdfsPort((String)setting.get(StoreSetting.HdfsPort.name()));
				
				rows.add(storage);
			}
			aresult.setData(rows);
			aresult.setState(ActionResult.SUCCESS);
			aresult.setMessage(getMessage("mesg.find.storage"));
		}catch(CoreException ce){
			
			aresult.setState(ActionResult.ERROR);
			aresult.setMessage(ce.getMessage());
			aresult.setDetailmsgs(ce.getValidateMessages());
		}
		
		mav.addAllObjects(aresult.asMap());
		return mav;
	}
	
	@RequestMapping("storage-save")
	public ModelAndView doSaveStorage(HttpServletRequest request){
		
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
		
		InfoId<Integer> infoid = IdKey.STORAGE.getInfoId(storage.getStorageId());
		sinfo.setInfoId(infoid);
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
		try{
			
			StorageFacade.saveStorage(accesspoint, principal, sinfo);
			aresult.setState(ActionResult.SUCCESS);
			aresult.setMessage(getMessage("mesg.save.storage"));
			
		}catch(CoreException ce){
			aresult.setState(ActionResult.ERROR);
			aresult.setMessage(ce.getMessage());
			aresult.setDetailmsgs(ce.getValidateMessages());
		}
		
		mav.addAllObjects(aresult.asMap());
		return mav;
	}
	
	@RequestMapping("storage-remove")
	public ModelAndView doRemoveStorage(HttpServletRequest request){
		
		String storageId = super.readRequestParam("storage_id");
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		ModelAndView mav = super.getJsonModelView();
		ActionResult aresult = new ActionResult();
		
		InfoId<Integer> sid = IdKey.STORAGE.getInfoId(Integer.valueOf(storageId));
		if(Storages.DEFAULT_STORAGE_ID == sid.getId()){
			
			aresult.setState(ActionResult.ERROR);
			aresult.setMessage(getMessage("mesg.rsrv.storage"));
			mav.addAllObjects(aresult.asMap());
			return mav;
		}
		try{
			StorageFacade.removeStorage(accesspoint, principal, sid);
			aresult.setState(ActionResult.SUCCESS);
			aresult.setMessage(getMessage("mesg.remove.storage"));
		}catch(CoreException ce){
			aresult.setState(ActionResult.ERROR);
			aresult.setMessage(ce.getMessage());
			aresult.setDetailmsgs(ce.getValidateMessages());
		}
		mav.addAllObjects(aresult.asMap());
		return mav;
	}
	
	@RequestMapping("storage-info")
	public ModelAndView doGetStorage(HttpServletRequest request){
		
		String storageId = super.readRequestParam("storage_id");
		Storage storage = new Storage();
		// read trace information
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		ModelAndView mav = super.getJsonModelView();
		ActionResult aresult = new ActionResult();
		
		InfoId<Integer> sid = IdKey.STORAGE.getInfoId(Integer.valueOf(storageId));
		try{
			StorageInfo sinfo = StorageFacade.findStorage(accesspoint, principal, sid);
			storage.setStorageId(sinfo.getInfoId().getId());
			storage.setName(sinfo.getStorageName());
			storage.setState(sinfo.getState());
			storage.setType(sinfo.getStorageType());
			storage.setDescription(sinfo.getDescription());
			storage.setCapacity(sinfo.getCapacity().toString());
			storage.setUsed(sinfo.getUsed().toString());
			
			Map<String, Object> setting = Storages.parseSetting(sinfo.getSettingJson());
			storage.setStorePath((String)setting.get(StoreSetting.StorePath.name()));
			storage.setHdfsHost((String)setting.get(StoreSetting.HdfsHost.name()));
			storage.setHdfsPort((String)setting.get(StoreSetting.HdfsPort.name()));
			
			aresult.setData(storage);
			aresult.setState(ActionResult.SUCCESS);
			aresult.setMessage(getMessage("mesg.find.storage"));
		}catch(CoreException ce){
			aresult.setState(ActionResult.ERROR);
			aresult.setMessage(ce.getMessage());
		}
		
		mav.addAllObjects(aresult.asMap());
		return mav;	
	}
}
