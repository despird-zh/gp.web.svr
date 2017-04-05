package com.gp.web.service;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gp.common.AccessPoint;
import com.gp.common.Cabinets;
import com.gp.common.GroupUsers;
import com.gp.common.IdKey;
import com.gp.common.Principal;
import com.gp.core.CabinetFacade;
import com.gp.core.SecurityFacade;
import com.gp.dao.info.CabinetInfo;
import com.gp.dao.info.UserInfo;
import com.gp.exception.CoreException;
import com.gp.exception.WebException;
import com.gp.info.InfoId;
import com.gp.svc.info.UserExtInfo;
import com.gp.util.CommonUtils;
import com.gp.util.DateTimeUtils;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.Account;
import com.gp.web.servlet.ServiceFilter;
import com.gp.web.util.ExWebUtils;

@Controller
@RequestMapping(ServiceFilter.FILTER_PREFIX)
public class UserController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@RequestMapping(
			value = "users-query.do",
			method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doUsersQuery(@RequestBody String payload){
		
		// the access point
		AccessPoint accesspoint = super.getAccessPoint(request);
		Principal principal = super.getPrincipal();
		// the model and view
		ModelAndView mav = getJsonModelView();
		Map<String,String> paramap = this.readRequestJson(payload);
		LOGGER.debug("params {}" , paramap);
		
		String[] states = new String[]{paramap.get("state")};
		if("ALL".equals(paramap.get("state")))
			states = new String[0];
		
		Integer sourceId = null;
		if(!"ALL".equals(paramap.get("type")))
			sourceId = NumberUtils.toInt(paramap.get("type"));
		
		ActionResult result = null;
		try{
			List<UserExtInfo> ulist = SecurityFacade.findAccounts(accesspoint, principal, 
					paramap.get("filterkey"), // name
					sourceId,  // entity
					new String[0],  // type
					states); // state
			
			List<Account> list = new ArrayList<Account>();
			for(UserExtInfo info: ulist){
				
				Account ui = new Account();
				ui.setSourceId(info.getSourceId());
				ui.setUserId(info.getInfoId().getId());
				ui.setAccount(info.getAccount());
				ui.setEmail(info.getEmail());
				ui.setMobile(info.getMobile());
				ui.setPhone(info.getPhone());
				ui.setType(info.getType());
				if(info.getCreateDate() != null)
					ui.setCreateDate(DateTimeUtils.toYearMonthDay(info.getCreateDate()));
				
				ui.setStorageName(info.getStorageName());
				ui.setName(info.getFullName());
				ui.setSourceName(info.getSourceName());
				ui.setState(info.getState());
	
				list.add(ui);
			}		

			result = ActionResult.success(this.getMessage("mesg.find.account"));
			result.setData(list);
		}catch(CoreException ce){
			
			result = super.wrapResult(ce);
		}
		return mav.addAllObjects(result.asMap());
	}
	
	@RequestMapping(
			value = "user-save.do",
			method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doUserSave(@RequestBody String payload) {
		
		if(LOGGER.isDebugEnabled())
			ExWebUtils.dumpRequestAttributes(request);
		
		Account account = super.readRequestBody(payload, Account.class);
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		ActionResult result = new ActionResult();
		
		UserInfo uinfo = new UserInfo();
		InfoId<Long> uid = IdKey.USER.getInfoId(Long.valueOf(account.getUserId()));
		uinfo.setInfoId(uid);
		uinfo.setAccount(account.getAccount());
		uinfo.setFullName(account.getName());
		uinfo.setLanguage(account.getLanguage());
		uinfo.setEmail(account.getEmail());
		uinfo.setPassword(account.getPassword());
		uinfo.setPhone(account.getPhone());
		uinfo.setMobile(account.getMobile());
		uinfo.setTimezone(account.getTimezone());
		uinfo.setType(account.getType());
		uinfo.setStorageId(account.getStorageId());
		uinfo.setState(account.getState());
		
		Long pubcapacity = account.getPubcapacity();
		Long pricapacity = account.getPricapacity();
		
		try{
			SecurityFacade.saveAccount(accesspoint, principal, uinfo, pubcapacity, pricapacity);
			result = ActionResult.success(getMessage("mesg.save.account"));
			
		}catch(CoreException ce){
			
			result = super.wrapResult(ce);

		}
		
		ModelAndView mav = getJsonModelView();		
		mav.addAllObjects(result.asMap());

		return mav;
	}
	
	@RequestMapping(
			value = "user-add.do",
			method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doNewAccount(@RequestBody String payload) throws WebException {
		
		if(LOGGER.isDebugEnabled())
			ExWebUtils.dumpRequestAttributes(request);
		
		Account account = super.readRequestBody(payload, Account.class);
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		ActionResult result = new ActionResult();		
		ModelAndView mav = getJsonModelView();		
		
		String confirmPwd = super.readRequestJson(payload).get("confirm");
		UserInfo uinfo = new UserInfo();
		uinfo.setAccount(account.getAccount());
		uinfo.setFullName(account.getName());
		uinfo.setLanguage(account.getLanguage());
		uinfo.setEmail(account.getEmail());
		uinfo.setPassword(account.getPassword());
		uinfo.setPhone(account.getPhone());
		uinfo.setMobile(account.getMobile());
		uinfo.setTimezone(account.getTimezone());
		uinfo.setType(account.getType());
		uinfo.setStorageId(account.getStorageId());
		uinfo.setState(GroupUsers.UserState.ACTIVE.name());
	
		Long pubcapacity = account.getPubcapacity();
		Long pricapacity = account.getPricapacity();
		
		// password not consistent
		if(!StringUtils.equals(confirmPwd, account.getPassword())){
		
			result = ActionResult.failure(getMessage("mesg.pwd.diff.cfm"));
			mav.addAllObjects(result.asMap());

		}else{
			try{
				SecurityFacade.newAccount(accesspoint, principal, uinfo, pubcapacity, pricapacity);
				result = ActionResult.success(getMessage("mesg.save.account"));
			}catch(CoreException ce){
				if(CollectionUtils.isNotEmpty(ce.getValidateMessages())){
					result = ActionResult.invalid(ce.getMessage(), ce.getValidateMessages());
				}else{
					result = ActionResult.error(ce.getMessage());
				}
			}
			mav.addAllObjects(result.asMap());
		}

		return mav;
	}
	
	@RequestMapping(
			value = "user-remove.do",
			method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doAccountDelete(@RequestBody String payload){
		
		if(LOGGER.isDebugEnabled())
			ExWebUtils.dumpRequestAttributes(request);
		
		Map<String,String> paramap = this.readRequestJson(payload);
		
		String account = paramap.get("account");
		String uid = paramap.get("user_id");
		
		ModelAndView mav = getJsonModelView();	
		ActionResult result = new ActionResult();
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		Long userId = null;
		InfoId<Long> userkey = null;
		if(StringUtils.isNotBlank(uid) && CommonUtils.isNumeric(uid)){
			userId = Long.valueOf(uid);
			userkey = IdKey.USER.getInfoId(userId);
		}
		
		if(userId == GroupUsers.ADMIN_USER.getUserId().getId()){
			
			result = ActionResult.failure(getMessage("mesg.rsrv.admin"));
			mav.addAllObjects(result.asMap());
			return mav;
		}
		
		try{
			SecurityFacade.removeAccount(accesspoint, principal, userkey, account);
			result = ActionResult.success(getMessage("mesg.remove.account"));
		}catch(CoreException ce){
			result = super.wrapResult(ce);
		}
			
		mav.addAllObjects(result.asMap());

		return mav;
	}
	
	@RequestMapping(value = "user-info.do",
			method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doAccountInfo(@RequestBody String payload){

		Map<String,String> paramap = this.readRequestJson(payload);
		
		String account = paramap.get("account");
		String type = paramap.get("type");
		String uidStr = paramap.get("user_id");

		AccessPoint accesspoint = super.getAccessPoint(request);
		Principal principal = super.getPrincipal();
		Long userId = null;

		InfoId<Long> userkey = null;
		if(StringUtils.isNotBlank(uidStr) && CommonUtils.isNumeric(uidStr) ){
			userId = Long.valueOf(uidStr);
			userkey = IdKey.USER.getInfoId(userId);
		}

		ActionResult result = null;
		
		try{
			UserExtInfo info = SecurityFacade.findAccount(accesspoint,principal, userkey,account, type);
			Account ui = new Account();
			ui.setUserId(info.getInfoId().getId());
			ui.setSourceId(info.getSourceId());
			ui.setAccount(info.getAccount());
			ui.setEmail(info.getEmail());
			ui.setMobile(info.getMobile());
			ui.setType(info.getType());
			ui.setPhone(info.getPhone());
			ui.setName(info.getFullName());
			ui.setState(info.getState());
			ui.setStorageId(info.getStorageId());
			ui.setLanguage(info.getLanguage());
			ui.setTimezone(info.getTimezone());
			ui.setStorageName(info.getStorageName());
			
			List<CabinetInfo> cabs = CabinetFacade.findPersonalCabinets(accesspoint,GroupUsers.PSEUDO_USER,info.getAccount());
			for(CabinetInfo cinfo: cabs){
				if(Cabinets.CabinetType.NETDISK.name().equals(cinfo.getCabinetType()))
					ui.setPricapacity(cinfo.getCapacity());
				
				else if(Cabinets.CabinetType.PUBLISH.name().equals(cinfo.getCabinetType()))
					ui.setPubcapacity(cinfo.getCapacity());
			}
			
			DateFormat datefmt = principal.getDateFormat();
			ui.setModifier(info.getModifier());
			ui.setLastModified(datefmt.format(info.getModifyDate()));
			
			result = ActionResult.success(getMessage("mesg.find.account"));
			result.setData(ui);
		}catch(CoreException ce){
			result = super.wrapResult(ce);
		}
		ModelAndView mav = getJsonModelView();		
		mav.addAllObjects(result.asMap());

		return mav;
	}
}
