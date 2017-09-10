package com.gp.web.api;

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
import com.gp.common.GroupUsers;
import com.gp.core.SecurityFacade;
import com.gp.exception.CoreException;
import com.gp.dao.info.UserInfo;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.AccountExt;
import com.gp.web.servlet.ServiceTokenFilter;
import com.gp.web.util.ExWebUtils;

@Controller
@RequestMapping(ServiceTokenFilter.FILTER_PREFIX)
public class ExterUserController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(ExterUserController.class);
	
	static UserInfo DEFAULT = new UserInfo();

	public ExterUserController(){
		
		DEFAULT.setPassword("_blank");
		DEFAULT.setPhone("99998888");
		DEFAULT.setLanguage("zh_CN");
		DEFAULT.setTimezone("GMT+08:00");
		DEFAULT.setType(GroupUsers.UserType.EXTERNAL.name());
		DEFAULT.setStorageId(-999);
		DEFAULT.setState(GroupUsers.UserState.ACTIVE.name());
	}

	@RequestMapping(value = "ext-users-query",
			method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doGetExternalAccounts(){
		
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
		
		ActionResult result = ActionResult.success("Success find external users");
		
		result.setData(alist);

		return mav.addAllObjects(result.asMap());
	}
	
	@RequestMapping(value = "ext-user-add",
			method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doNewExternalAccounts(@RequestBody(required=false) String payload){
		
		if(LOGGER.isDebugEnabled())
			ExWebUtils.dumpRequestAttributes(request);
		
		AccountExt account = super.readRequestBody(payload, AccountExt.class);
		
		GPrincipal principal = super.getPrincipal();
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
		uinfo.setTimezone(DEFAULT.getTimezone());
		uinfo.setType(DEFAULT.getType());
		uinfo.setStorageId(DEFAULT.getStorageId());
		uinfo.setState(DEFAULT.getState());

		try{
			SecurityFacade.newAccountExt(accesspoint, principal, 
				uinfo, 
				account.getEntity(), 
				account.getNode());
			result = ActionResult.success(getMessage("mesg.save.account.ext"));
			
		}catch(CoreException ce){			
			result = super.wrapResult(ce);
		}
		
		ModelAndView mav = getJsonModelView();		
		mav.addAllObjects(result.asMap());

		return mav;
	}
}
