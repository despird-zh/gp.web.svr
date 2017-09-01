package com.gp.web.api;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.gp.common.IdKeys;
import com.gp.common.GPrincipal;
import com.gp.common.Storages;
import com.gp.common.Storages.StorageState;
import com.gp.common.Storages.StorageType;
import com.gp.common.Storages.StoreSetting;
import com.gp.core.StorageFacade;
import com.gp.dao.info.StorageInfo;
import com.gp.exception.CoreException;
import com.gp.info.InfoId;
import com.gp.util.CommonUtils;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.Storage;
import com.gp.web.servlet.ServiceTokenFilter;
import com.gp.web.util.ExWebUtils;

@Controller
@RequestMapping(ServiceTokenFilter.FILTER_PREFIX)
public class StorageController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(StorageController.class);
	static final String ALL_OPTION = "ALL";
	
	@RequestMapping(
		    value = "storages-query.do", 
		    method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doStoragesQuery(@RequestBody String payload) throws Exception {
		// the access point
		AccessPoint accesspoint = super.getAccessPoint(request);
		GPrincipal principal = super.getPrincipal();
		// the model and view
		ModelAndView mav = getJsonModelView();
		Map<String,String> paramap = this.readRequestJson(payload);
		LOGGER.debug("params {}" , paramap);
		ActionResult result = null;
		String namecond = paramap.get("filter");
		String type = paramap.get("type");
		String state = paramap.get("state");
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
		
		try{
			List<Storage> rows = new ArrayList<Storage>();
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
			result = ActionResult.success(getMessage("mesg.find.storage"));
			result.setData(rows);
		}catch(CoreException ce){
			
			result = super.wrapResult(ce);
			
		}
		
		return mav.addAllObjects(result.asMap());
	}
	
	@RequestMapping(
		    value = "storage-add.do", 
		    method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doNewStorage(@RequestBody String payload){
		
		if(LOGGER.isDebugEnabled())
			ExWebUtils.dumpRequestAttributes(request);

		// read trace information
		GPrincipal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		// prepare result
		ActionResult result = null;
		ModelAndView mav = super.getJsonModelView();

		// read parameter
		Storage storage = super.readRequestBody(payload, Storage.class);
		
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
			StorageFacade.newStorage(accesspoint, principal, sinfo);
			result = ActionResult.success(getMessage("mesg.new.storage"));
		}catch(CoreException ce){

			result = super.wrapResult(ce);
		}
		
		mav.addAllObjects(result.asMap());
		return mav;
	}
	
	@RequestMapping(
		    value = "storage-save.do", 
		    method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doSaveStorage(@RequestBody String payload){
		
		if(LOGGER.isDebugEnabled())
			ExWebUtils.dumpRequestAttributes(request);

		// read trace information
		GPrincipal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		// prepare result
		ActionResult result = null;
		ModelAndView mav = super.getJsonModelView();
		
		// read parameter
		Storage storage = super.readRequestBody(payload, Storage.class);
		StorageInfo sinfo = new StorageInfo();
		
		InfoId<Integer> infoid = IdKeys.getInfoId(IdKey.STORAGE,storage.getStorageId());
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
			result = ActionResult.success(getMessage("mesg.save.storage"));
			
		}catch(CoreException ce){
			result = super.wrapResult(ce);
		}
		
		mav.addAllObjects(result.asMap());
		return mav;
	}
	
	@RequestMapping(
		    value = "storage-remove.do", 
		    method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doRemoveStorage(@RequestBody String payload){
		
		String storageId = super.readRequestParam("storage_id");
		GPrincipal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		ModelAndView mav = super.getJsonModelView();
		ActionResult result = new ActionResult();
		
		InfoId<Integer> sid = IdKeys.getInfoId(IdKey.STORAGE,Integer.valueOf(storageId));
		if(Storages.DEFAULT_STORAGE_ID == sid.getId()){
			
			result = ActionResult.failure(getMessage("mesg.rsrv.storage"));
			mav.addAllObjects(result.asMap());
			return mav;
		}
		try{
			StorageFacade.removeStorage(accesspoint, principal, sid);
			result = ActionResult.success(getMessage("mesg.remove.storage"));
		}catch(CoreException ce){
		
			result = super.wrapResult(ce);
		
		}
		mav.addAllObjects(result.asMap());
		return mav;
	}
}
