package com.gp.web.view.security;

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
import com.gp.web.util.ExWebUtils;
import com.gp.common.AccessPoint;
import com.gp.common.GPrincipal;
import com.gp.common.GroupUsers;
import com.gp.core.SecurityFacade;
import com.gp.exception.CoreException;
import com.gp.exception.WebException;
import com.gp.dao.info.UserInfo;

@Controller
public class AccountAddController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(AccountAddController.class);

	@RequestMapping("account-new")
	public ModelAndView doInitial(){
		
		ModelAndView mav = getJspModelView("config/account-new");
		Account account = new Account();
		account.setPubcapacity(123l);
		account.setPricapacity(125l);
		mav.addObject("item",account);
		return mav;
	}
//		
//	@RequestMapping("account-add")
//	public ModelAndView doNewAccount(HttpServletRequest request, HttpServletResponse response) throws WebException {
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
//		ModelAndView mav = getJsonModelView();		
//		
//		String confirmPwd = super.readRequestParam("confirm");
//		UserInfo uinfo = new UserInfo();
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
//		uinfo.setState(GroupUsers.UserState.ACTIVE.name());
//	
//		Long pubcapacity = account.getPubcapacity();
//		Long pricapacity = account.getPricapacity();
//		
//		// password not consistent
//		if(!StringUtils.equals(confirmPwd, account.getPassword())){
//		
//			result = ActionResult.failure(getMessage("mesg.pwd.diff.cfm"));
//			mav.addAllObjects(result.asMap());
//
//		}else{
//			try{
//				SecurityFacade.newAccount(accesspoint, principal, uinfo, pubcapacity, pricapacity);
//				result = ActionResult.success(getMessage("mesg.save.account"));
//			}catch(CoreException ce){
//				result = super.wrapResult(ce);
//			}
//			mav.addAllObjects(result.asMap());
//		}
//
//		return mav;
//	}

}
