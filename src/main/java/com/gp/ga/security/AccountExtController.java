package com.gp.ga.security;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.audit.AccessPoint;
import com.gp.common.Principal;
import com.gp.common.GroupUsers;
import com.gp.core.SecurityFacade;
import com.gp.exception.CoreException;
import com.gp.info.InfoId;
import com.gp.dao.info.UserInfo;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.AccountExt;
import com.gp.web.util.CustomWebUtils;

@Controller("ga-account-ext-ctlr")
@RequestMapping("/ga")
public class AccountExtController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(AccountExtController.class);
	
	static UserInfo DEFAULT = new UserInfo();
	
	public AccountExtController(){
		
		DEFAULT.setPassword("_blank");
		DEFAULT.setPhone("99998888");
		DEFAULT.setLanguage("zh_CN");
		DEFAULT.setTimeZone("GMT+08:00");
		DEFAULT.setType(GroupUsers.UserType.EXTERNAL.name());
		DEFAULT.setStorageId(-999);
		DEFAULT.setState(GroupUsers.UserState.ACTIVE.name());
	}
	
	@RequestMapping("account-ext")
	public ModelAndView doInitial(){
		
		ModelAndView mav = getJspModelView("ga/config/account-ext");
		
		return mav;
	}
	
	@RequestMapping("account-ext-search")
	public ModelAndView doGetExternalAccounts(HttpServletRequest request){
		
		ModelAndView mav = super.getJsonModelView();
		
		List<AccountExt> alist = new ArrayList<AccountExt>();
		
		for(int i= 0 ; i<27; i++){
			
			AccountExt ae = new AccountExt();
			ae.setEntity("E00001");
			ae.setNode("N00003");
			ae.setAccount("user"+i);
			ae.setGlobalAccount("user"+i);
			ae.setEmail("user"+i+"@164.com");
			ae.setMobile("1923645738");
			ae.setName("gary"+i +"diao");
			ae.setSource("Demo entity");
			
			alist.add(ae);
		}
		
		mav.addObject(MODEL_KEY_STATE, ActionResult.SUCCESS);
		mav.addObject(MODEL_KEY_MESSAGE, "Success find external users");
		mav.addObject(MODEL_KEY_DATA, alist);

		return mav;
	}
	
	@RequestMapping("account-ext-new")
	public ModelAndView doNewExternalAccounts(HttpServletRequest request){
		
		CustomWebUtils.dumpRequestAttributes(request);
		
		AccountExt account = new AccountExt();
		super.readRequestData(request, account);
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		ActionResult result = new ActionResult();
		
		UserInfo uinfo = new UserInfo();
		uinfo.setAccount(account.getAccount());
		uinfo.setGlobalAccount(account.getGlobalAccount());
		uinfo.setFullName(account.getName());
		uinfo.setLanguage(DEFAULT.getLanguage());
		uinfo.setEmail(account.getEmail());
		uinfo.setPassword(DEFAULT.getPassword());
		uinfo.setPhone(DEFAULT.getPhone());
		uinfo.setMobile(account.getMobile());
		uinfo.setTimeZone(DEFAULT.getTimeZone());
		uinfo.setType(DEFAULT.getType());
		uinfo.setStorageId(DEFAULT.getStorageId());
		uinfo.setState(DEFAULT.getState());

		try{
			InfoId<Long> gresult = SecurityFacade.newAccountExt(accesspoint, principal, 
				uinfo, 
				account.getEntity(), 
				account.getNode());
			result.setState(ActionResult.SUCCESS);
			result.setMessage(getMessage("mesg.save.account.ext"));
			
		}catch(CoreException ce){			
			result.setState(ActionResult.ERROR);
			result.setMessage(ce.getMessage());
			result.setDetailmsgs(ce.getValidateMessages());
		}
		
		ModelAndView mav = getJsonModelView();		
		mav.addAllObjects(result.asMap());

		return mav;
	}
}
