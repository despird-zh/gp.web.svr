package com.gp.web.view.security;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.Account;
import com.gp.web.util.ExWebUtils;
import com.gp.common.AccessPoint;
import com.gp.common.Cabinets;
import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.common.GPrincipal;
import com.gp.common.GroupUsers;
import com.gp.common.GroupUsers.UserState;
import com.gp.common.GroupUsers.UserType;
import com.gp.core.CabinetFacade;
import com.gp.core.SecurityFacade;
import com.gp.exception.CoreException;
import com.gp.exception.WebException;
import com.gp.dao.info.CabinetInfo;
import com.gp.info.CombineInfo;
import com.gp.info.InfoId;
import com.gp.dao.info.UserInfo;
import com.gp.svc.info.UserExtInfo;
import com.gp.util.CommonUtils;
import com.gp.util.DateTimeUtils;

@Controller
public class AccountListController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(AccountListController.class);
	
	static final String ALL_OPTION = "ALL";
	
	static final String VIEW_TAB_LIST = "list";
	static final String VIEW_TAB_EDIT = "edit";

	@RequestMapping("account-list")
	public ModelAndView doInitial(){
		
		ModelAndView mav = getJspModelView("config/account-list");
		String viewtab = super.readRequestParam("viewtab");
		
		int exist = ArrayUtils.indexOf(new String[]{VIEW_TAB_LIST,VIEW_TAB_EDIT}, viewtab);
		if(-1 == exist){
			viewtab = VIEW_TAB_LIST;
		}
		mav.addObject("viewtab", viewtab);
		Account account = new Account();
		account.setPubcapacity(123l);
		account.setPricapacity(125l);
		mav.addObject("item",account);
		return mav;

	}
//
//	/**
//	 * Account search action 
//	 *  
//	 **/
//	@RequestMapping("account-search")
//	public ModelAndView doAccountSearch(HttpServletRequest request, HttpServletResponse response) throws WebException {
//				
//		List<Account> list = new ArrayList<Account>();
//
//		GPrincipal principal = super.getPrincipal();
//
//		String uname = request.getParameter("uname");
//		String instanceStr = request.getParameter("instance_id");
//		String type = request.getParameter("type");
//		String state = request.getParameter("state");
//		ActionResult result = new ActionResult();		
//		Integer instanceId = null;
//		if(StringUtils.isNotBlank(instanceStr) && CommonUtils.isNumeric(instanceStr))
//			instanceId = Integer.valueOf(instanceStr);
//		
//		String[] types = null;
//		if(ALL_OPTION.equals(type)){
//			types = new String[]{
//					UserType.INLINE.name(),
//					UserType.LDAP.name(),
//					UserType.EXTERNAL.name()};
//		}else{
//			types = new String[]{type};
//		}
//		String[] states = null;
//		if(ALL_OPTION.equals(state)){
//			states = new String[]{
//					UserState.ACTIVE.name(),
//					UserState.DEACTIVE.name(),
//					UserState.FROZEN.name()};
//		}else{
//			states = new String[]{state};
//		}
//		
//		try{
//			List<UserExtInfo> ulist = SecurityFacade.findAccounts(getAccessPoint(request), principal, 
//				uname, // name
//				instanceId,  // entity
//				types,  // type
//				states); // state
//
//			for(UserExtInfo info: ulist){
//				
//				Account ui = new Account();
//				ui.setSourceId(info.getSourceId());
//				ui.setUserId(info.getInfoId().getId());
//				ui.setAccount(info.getAccount());
//				ui.setEmail(info.getEmail());
//				ui.setMobile(info.getMobile());
//				ui.setPhone(info.getPhone());
//				ui.setType(info.getType());
//				if(info.getCreateDate() != null)
//					ui.setCreateDate(DateTimeUtils.toYearMonthDay(info.getCreateDate()));
//				
//				ui.setStorageName(info.getStorageName());
//				ui.setName(info.getFullName());
//				ui.setSourceName(info.getSourceName());
//				ui.setState(info.getState());
//	
//				list.add(ui);
//			}			
//			
//			result = ActionResult.success(getMessage("mesg.find.account"));
//			result.setData(list);
//
//		}catch(CoreException ce){
//
//			result = super.wrapResult(ce);
//		}
//		
//		ModelAndView mav = getJsonModelView();		
//		mav.addAllObjects(result.asMap());
//
//		return mav;
//	}
//		
//	@RequestMapping("account-update")
//	public ModelAndView doAccountUpdate(HttpServletRequest request, HttpServletResponse response) throws WebException {
//		
//		if(LOGGER.isDebugEnabled())
//			ExWebUtils.dumpRequestAttributes(request);
//		
//		Account account = new Account();
//		super.readRequestParams( account);
//		
//		GPrincipal principal = super.getPrincipal();
//		AccessPoint accesspoint = super.getAccessPoint(request);
//		ActionResult result = new ActionResult();
//		
//		UserInfo uinfo = new UserInfo();
//		InfoId<Long> uid = IdKeys.getInfoId(IdKey.GP_USERS, Long.valueOf(account.getUserId()));
//		uinfo.setInfoId(uid);
//		uinfo.setAccount(account.getAccount());
//		uinfo.setFullName(account.getName());
//		uinfo.setLanguage(account.getLanguage());
//		uinfo.setEmail(account.getEmail());
//		uinfo.setPassword(account.getPassword());
//		uinfo.setPhone(account.getPhone());
//		uinfo.setMobile(account.getMobile());
//		uinfo.setTimezone(account.getTimezone());
//		uinfo.setType(account.getType());
//		uinfo.setStorageId(account.getStorageId());
//		uinfo.setState(account.getState());
//		
//		Long pubcapacity = account.getPubcapacity();
//		Long pricapacity = account.getPricapacity();
//		
//		try{
//			SecurityFacade.saveAccount(accesspoint, principal, uinfo, pubcapacity, pricapacity);
//			result = ActionResult.success(getMessage("mesg.save.account"));
//			
//		}catch(CoreException ce){
//			
//			result = super.wrapResult(ce);
//		}
//		
//		ModelAndView mav = getJsonModelView();		
//		mav.addAllObjects(result.asMap());
//
//		return mav;
//	}
//	
//	/**
//	 * the parameter :
//	 * user_id 
//	 * src_id 
//	 * type
//	 * account 
//	 **/
//	@RequestMapping("account-info")
//	public ModelAndView doAccountInfo(HttpServletRequest request){
//
//		String account = request.getParameter("account");
//		String type = request.getParameter("type");
//		String uidStr = request.getParameter("user_id");
//
//		AccessPoint accesspoint = super.getAccessPoint(request);
//		GPrincipal principal = super.getPrincipal();
//		Long userId = null;
//
//		InfoId<Long> userkey = null;
//		if(StringUtils.isNotBlank(uidStr) && CommonUtils.isNumeric(uidStr) ){
//			userId = Long.valueOf(uidStr);
//			userkey = IdKeys.getInfoId(IdKey.GP_USERS, userId);
//		}
//
//		ActionResult result = new ActionResult();
//		
//		try{
//			UserExtInfo info = SecurityFacade.findAccount(accesspoint,principal, userkey,account, type);
//			Account ui = new Account();
//			ui.setUserId(info.getInfoId().getId());
//			ui.setSourceId(info.getSourceId());
//			ui.setAccount(info.getAccount());
//			ui.setEmail(info.getEmail());
//			ui.setMobile(info.getMobile());
//			ui.setType(info.getType());
//			ui.setPhone(info.getPhone());
//			ui.setName(info.getFullName());
//			ui.setState(info.getState());
//			ui.setStorageId(info.getStorageId());
//			ui.setLanguage(info.getLanguage());
//			ui.setTimezone(info.getTimezone());
//			ui.setStorageName(info.getStorageName());
//			
//			List<CabinetInfo> cabs = CabinetFacade.findPersonalCabinets(accesspoint,GroupUsers.PSEUDO_USER,info.getAccount());
//			for(CabinetInfo cinfo: cabs){
//				if(Cabinets.CabinetType.NETDISK.name().equals(cinfo.getCabinetType()))
//					ui.setPricapacity(cinfo.getCapacity());
//				
//				else if(Cabinets.CabinetType.PUBLISH.name().equals(cinfo.getCabinetType()))
//					ui.setPubcapacity(cinfo.getCapacity());
//			}
//			result = ActionResult.success(getMessage("mesg.find.account"));
//			result.setData(ui);
//		}catch(CoreException ce){
//			result = super.wrapResult(ce);
//		}
//		ModelAndView mav = getJsonModelView();		
//		mav.addAllObjects(result.asMap());
//
//		return mav;
//	}
//	
//	@RequestMapping("account-delete")
//	public ModelAndView doAccountDelete(HttpServletRequest request, HttpServletResponse response){
//		
//		if(LOGGER.isDebugEnabled())
//			ExWebUtils.dumpRequestAttributes(request);
//		
//		String account = request.getParameter("account");
//		String uid = request.getParameter("user_id");
//		ModelAndView mav = getJsonModelView();	
//		ActionResult result = new ActionResult();
//		GPrincipal principal = super.getPrincipal();
//		AccessPoint accesspoint = super.getAccessPoint(request);
//		Long userId = null;
//		InfoId<Long> userkey = null;
//		if(StringUtils.isNotBlank(uid) && CommonUtils.isNumeric(uid)){
//			userId = Long.valueOf(uid);
//			userkey = IdKeys.getInfoId(IdKey.GP_USERS, userId);
//		}
//		
//		if(userId == GroupUsers.ADMIN_USER.getUserId().getId()){
//			
//			result = ActionResult.error(getMessage("mesg.rsrv.admin"));
//			mav.addAllObjects(result.asMap());
//			return mav;
//		}
//		
//		try{
//			SecurityFacade.removeAccount(accesspoint, principal, userkey, account);
//			result = ActionResult.success(getMessage("mesg.remove.account"));
//		}catch(CoreException ce){
//			result = super.wrapResult(ce);
//		}
//			
//		mav.addAllObjects(result.asMap());
//
//		return mav;
//	}
}
