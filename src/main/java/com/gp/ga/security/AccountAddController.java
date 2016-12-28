package com.gp.ga.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.Account;
import com.gp.web.util.CustomWebUtils;
import com.gp.audit.AccessPoint;
import com.gp.common.Principal;
import com.gp.common.GroupUsers;
import com.gp.core.SecurityFacade;
import com.gp.exception.CoreException;
import com.gp.exception.WebException;
import com.gp.dao.info.UserInfo;

@Controller("ga-account-new-ctlr")
@RequestMapping("/ga")
public class AccountAddController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(AccountAddController.class);

	@RequestMapping("account-new")
	public ModelAndView doInitial(){
		
		ModelAndView mav = getJspModelView("ga/config/account-new");
		Account account = new Account();
		account.setPubcapacity(123l);
		account.setPricapacity(125l);
		mav.addObject("item",account);
		return mav;
	}
		
	@RequestMapping("account-add")
	public ModelAndView doNewAccount(HttpServletRequest request, HttpServletResponse response) throws WebException {
		
		CustomWebUtils.dumpRequestAttributes(request);
		
		Account account = new Account();
		super.readRequestData(request, account);
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		ActionResult result = new ActionResult();		
		ModelAndView mav = getJsonModelView();		
		
		String confirmPwd = super.readRequestParam("confirm");
		UserInfo uinfo = new UserInfo();
		uinfo.setAccount(account.getAccount());
		uinfo.setFullName(account.getName());
		uinfo.setLanguage(account.getLanguage());
		uinfo.setEmail(account.getEmail());
		uinfo.setPassword(account.getPassword());
		uinfo.setPhone(account.getPhone());
		uinfo.setMobile(account.getMobile());
		uinfo.setTimeZone(account.getTimezone());
		uinfo.setType(account.getType());
		uinfo.setStorageId(account.getStorageId());
		uinfo.setState(GroupUsers.UserState.ACTIVE.name());
	
		Long pubcapacity = account.getPubcapacity();
		Long pricapacity = account.getPricapacity();
		
		// password not consistent
		if(!StringUtils.equals(confirmPwd, account.getPassword())){
		
			result.setState(ActionResult.FAIL);
			result.setMessage(getMessage("mesg.pwd.diff.cfm"));
			mav.addAllObjects(result.asMap());

		}else{
			try{
				SecurityFacade.newAccount(accesspoint, principal, uinfo, pubcapacity, pricapacity);
				result.setState(ActionResult.SUCCESS);
				result.setMessage(getMessage("mesg.save.account"));
			}catch(CoreException ce){
				result.setState(ActionResult.ERROR);
				result.setMessage(ce.getMessage());
				result.setDetailmsgs(ce.getValidateMessages());
			}
			mav.addAllObjects(result.asMap());
		}

		return mav;
	}

}
