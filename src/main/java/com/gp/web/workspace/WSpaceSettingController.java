package com.gp.web.workspace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gp.audit.AccessPoint;
import com.gp.common.Cabinets;
import com.gp.common.GeneralConfig;
import com.gp.common.GroupUsers;
import com.gp.common.IdKey;
import com.gp.common.Images;
import com.gp.common.Principal;
import com.gp.common.SystemOptions;
import com.gp.core.CabinetFacade;
import com.gp.core.ImageFacade;
import com.gp.core.PersonalFacade;
import com.gp.core.SecurityFacade;
import com.gp.core.StorageFacade;
import com.gp.exception.CoreException;
import com.gp.info.CombineInfo;
import com.gp.info.InfoId;
import com.gp.dao.info.CabinetInfo;
import com.gp.dao.info.ImageInfo;
import com.gp.dao.info.OrgHierInfo;
import com.gp.dao.info.StorageInfo;
import com.gp.dao.info.UserInfo;
import com.gp.dao.info.WorkgroupInfo;
import com.gp.util.HashUtils;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.Account;
import com.gp.web.model.UserBelonging;

@Controller
@RequestMapping("/workspace")
public class WSpaceSettingController  extends BaseController{
	
	static Logger LOGGER = LoggerFactory.getLogger(WSpaceSettingController.class);
	
	public static String CACHE_PATH = GeneralConfig.getString(SystemOptions.IMAGE_CACHE_PATH);
	
	static String ImagePath = GeneralConfig.getString(SystemOptions.IMAGE_CACHE_PATH);
	
	@RequestMapping("setting")
	public ModelAndView doViewInitial(){
		
		return getJspModelView("workspace/setting");
		
	}
	
	@RequestMapping("basic-setting")
	public ModelAndView doBasicSettingSearch(HttpServletRequest request){
		
		ModelAndView mav = super.getJsonModelView();
		ActionResult result = new ActionResult();
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		
		try {
			UserInfo uinfo = PersonalFacade.findAccountBasic(accesspoint, principal, principal.getUserId());
			Account ui = new Account();
			
			ui.setSourceId(uinfo.getSourceId());
			ui.setUserId(uinfo.getInfoId().getId());
			ui.setAccount(uinfo.getAccount());
			ui.setEmail(uinfo.getEmail());
			ui.setMobile(uinfo.getMobile());
			ui.setPhone(uinfo.getPhone());
			ui.setType(uinfo.getType());
			ui.setName(uinfo.getFullName());
			ui.setState(uinfo.getState());
			
			Integer storageId = uinfo.getStorageId();
			StorageInfo storage = StorageFacade.findStorage(accesspoint, principal, 
					IdKey.STORAGE.getInfoId(storageId));
			ui.setStorageId(storageId);
			ui.setStorageName(storage.getStorageName());
			
			List<CabinetInfo> cabs = CabinetFacade.findPersonalCabinets(accesspoint, principal, 
					principal.getAccount());
			
			for(CabinetInfo cinfo: cabs){
				if(Cabinets.CabinetType.PUBLISH.name().equals(cinfo.getCabinetType())){
					ui.setPubcapacity(cinfo.getCapacity());
				}else{
					ui.setPricapacity(cinfo.getCapacity());
				}
			}
			ImageInfo imginfo = ImageFacade.findImage(accesspoint, principal, IdKey.IMAGE.getInfoId(uinfo.getAvatarId()));
			String imagePath = "../" + ImagePath + "/" + imginfo.getLink();
			ui.setImagePath(imagePath);
			
			ui.setLanguage(uinfo.getLanguage());
			ui.setTimezone(uinfo.getTimeZone());
			ui.setSignature(uinfo.getSignature());
			result.setData(ui);
			
			result.setState(ActionResult.SUCCESS);
			result.setMessage(getMessage("mesg.find.account"));
		} catch (CoreException e) {
			result.setState(ActionResult.FAIL);
			result.setMessage(e.getMessage());
		}
		
		mav.addAllObjects(result.asMap());
		return mav;
	}
	
	@RequestMapping("belongings")
	public ModelAndView doBelongingSearch(HttpServletRequest request){
		
		ModelAndView mav = super.getJsonModelView();
		ActionResult result = new ActionResult();
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		
		try{
			Map<String, Object> data = new HashMap<String, Object>();
			
			List<CombineInfo<OrgHierInfo, Boolean>> orgs = PersonalFacade.findUserOrgHierNodes(accesspoint, principal, principal.getAccount());
			List<UserBelonging> orgbelongs = new ArrayList<UserBelonging>();
			for(CombineInfo<OrgHierInfo, Boolean> oinfo: orgs){
				UserBelonging ubelong = new UserBelonging();
				ubelong.setBelongId(oinfo.getPrimary().getInfoId().getId());
				ubelong.setName(oinfo.getPrimary().getOrgName());
				ubelong.setType("org");
				ubelong.setPostVisible(oinfo.getExtended());
				
				orgbelongs.add(ubelong);
			}
			data.put("orghiers", orgbelongs);
			
			List<CombineInfo<WorkgroupInfo, Boolean>> grps = PersonalFacade.findAccountWorkgroups(accesspoint, principal, principal.getAccount());
			List<UserBelonging> wgroups = new ArrayList<UserBelonging>();
			for(CombineInfo<WorkgroupInfo, Boolean> winfo: grps){
				UserBelonging ubelong = new UserBelonging();
				ubelong.setBelongId(winfo.getPrimary().getInfoId().getId());
				ubelong.setName(winfo.getPrimary().getWorkgroupName());
				ubelong.setType("wgroup");
				ubelong.setPostVisible(winfo.getExtended());
				
				wgroups.add(ubelong);
			}
			data.put("workgroups", wgroups);
			result.setData(data);
			result.setState(ActionResult.SUCCESS);
			result.setMessage(getMessage("mesg.find.belongs"));
		}catch (CoreException e) {
			result.setState(ActionResult.FAIL);
			result.setMessage(e.getMessage());
		}
		mav.addAllObjects(result.asMap());
		return mav;
	}
	
	@RequestMapping("change-pwd")
	public ModelAndView doChangePassword(HttpServletRequest request){
		
		ModelAndView mav = super.getJsonModelView();
		ActionResult result = new ActionResult();
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		String newpwd = request.getParameter("new-password");
		String cfmpwd = request.getParameter("confirm-password");
		String oripwd = request.getParameter("origin-password");
		
		String hashpwd = HashUtils.hashEncodeBase64(oripwd, SecurityFacade.HASH_SALT);
		try{
			UserInfo uinfo = SecurityFacade.findAccountLite(accesspoint, principal, null, principal.getAccount(), null);
			if(!StringUtils.equals(newpwd, cfmpwd)){
				
				result.setState(ActionResult.FAIL);
				result.setMessage(getMessage("excp.pwd.confirm.diff"));
				
			}else if(!StringUtils.equals(hashpwd, uinfo.getPassword())){
				
				result.setState(ActionResult.FAIL);
				result.setMessage(getMessage("excp.pwd.wrong"));
				
			}else{
				
				SecurityFacade.changePassword(accesspoint, principal, principal.getAccount(), newpwd);
				result.setState(ActionResult.SUCCESS);
				result.setMessage(getMessage("mesg.change.pwd"));
			}
		}catch (CoreException e) {
			result.setState(ActionResult.FAIL);
			result.setMessage(e.getMessage());
		}
		
		mav.addAllObjects(result.asMap());
		return mav;
	}
	
	@RequestMapping("save-basic-setting")
	public ModelAndView doBasicSettingSave(HttpServletRequest request){
		ModelAndView mav = super.getJsonModelView();
		ActionResult result = new ActionResult();
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		Account setting = new Account();
		super.readRequestData(setting);
		if(setting.getUserId() <= 0 && setting.getUserId() != GroupUsers.ADMIN_USER.getUserId().getId()){
			result.setState(ActionResult.FAIL);
			result.setMessage("missing the user id");
			mav.addAllObjects(result.asMap());
			return mav;
		}
		UserInfo uinfo = new UserInfo();
		try{
			uinfo.setInfoId(IdKey.USER.getInfoId(setting.getUserId()));
			uinfo.setType(setting.getType());
			uinfo.setEmail(setting.getEmail());
			uinfo.setMobile(setting.getMobile());
			uinfo.setSignature(setting.getSignature());
			uinfo.setFullName(setting.getName());
			uinfo.setPhone(setting.getPhone());
			uinfo.setState(setting.getState());
			String imgfile = FilenameUtils.getName(setting.getImagePath());
			String imagePath = request.getServletContext().getRealPath("/" + CACHE_PATH) +'/' + imgfile;
			LOGGER.debug("image file path : {}", imagePath);

			PersonalFacade.saveBasicSetting(accesspoint, principal, uinfo, imagePath);
			result.setState(ActionResult.SUCCESS);
			result.setMessage(getMessage("mesg.update.basic"));

		}catch(CoreException ce){
			result.setState(ActionResult.FAIL);
			result.setMessage(ce.getMessage());
			result.setDetailmsgs(ce.getValidateMessages());
		}
		
		mav.addAllObjects(result.asMap());
		return mav;

	}
	
	@RequestMapping("save-belong-setting")
	public ModelAndView doOrgBelongSettingSave(HttpServletRequest request){
		
		ModelAndView mav = super.getJsonModelView();
		ActionResult result = new ActionResult();
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		
		String setting_json = request.getParameter("setting_json");
		String belong_type = request.getParameter("belong_type");
		List<UserBelonging> settings = null;
		try {
			settings = JACKSON_MAPPER.readValue(setting_json, new TypeReference<List<UserBelonging>>(){});
		} catch (IOException e) {
			LOGGER.error("Fail save org belong setting", e);
			result.setMessage(getMessage("mesg.invalid.param"));
			result.setState(ActionResult.FAIL);
			return mav.addAllObjects(result.asMap());
		}
		
		if(CollectionUtils.isEmpty(settings) || StringUtils.isBlank(belong_type)){
			result.setMessage(getMessage("mesg.invalid.param"));
			result.setState(ActionResult.FAIL);
			return mav.addAllObjects(result.asMap());
		}
		
		Map<InfoId<Long>,Boolean> settingmap = new HashMap<InfoId<Long>,Boolean>();
		for(UserBelonging setting: settings){
			if("org".equals(belong_type))
				settingmap.put(IdKey.ORG_HIER.getInfoId(setting.getBelongId()), setting.getPostVisible());
			else
				settingmap.put(IdKey.WORKGROUP.getInfoId(setting.getBelongId()), setting.getPostVisible());
		}
		try{
			
			PersonalFacade.saveBelongSetting(accesspoint, principal,
				settingmap);
			result.setMessage(getMessage("mesg.save.belong.setting"));
			result.setState(ActionResult.SUCCESS);
		}catch(CoreException ce){
			result.setMessage(getMessage("excp.save.belong.setting"));
			result.setState(ActionResult.FAIL);
		}
		
		return mav.addAllObjects(result.asMap());
	}
	
	@RequestMapping("save-storage-setting")
	public ModelAndView doStorageSettingSave(HttpServletRequest request){
		ModelAndView mav = super.getJsonModelView();
		ActionResult result = new ActionResult();
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		
		String publishcap = request.getParameter("publish_cap");
		String netdiskcap = request.getParameter("netdisk_cap");
		String storageid = request.getParameter("storage_id");
		
		try{
			PersonalFacade.saveStorageSetting(accesspoint, principal, 
					NumberUtils.toLong(storageid), 
					NumberUtils.toLong(publishcap), 
					NumberUtils.toLong(netdiskcap));
			
			result.setMessage(getMessage("mesg.save.storage.setting"));
			result.setState(ActionResult.SUCCESS);
			
		}catch(CoreException ce){
			
			result.setMessage(ce.getMessage());
			result.setState(ActionResult.FAIL);
		}
		return mav.addAllObjects(result.asMap());
	}
	
	@RequestMapping("save-region-setting")
	public ModelAndView doRegionSettingSave(HttpServletRequest request){
		ModelAndView mav = super.getJsonModelView();
		ActionResult result = new ActionResult();
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		
		String timezone = request.getParameter("timezone");
		String language = request.getParameter("language");
		
		try{
			PersonalFacade.saveRegionSetting(accesspoint, principal, 
					timezone, language);
			
			result.setMessage(getMessage("mesg.save.region.setting"));
			result.setState(ActionResult.SUCCESS);
			
		}catch(CoreException ce){
			
			result.setMessage(ce.getMessage());
			result.setState(ActionResult.FAIL);
		}
		return mav.addAllObjects(result.asMap());
	}
}
