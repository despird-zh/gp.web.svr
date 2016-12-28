package com.gp.ga.config;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.audit.AccessPoint;
import com.gp.common.GeneralConstants;
import com.gp.common.IdKey;
import com.gp.common.Principal;
import com.gp.core.OrgHierFacade;
import com.gp.exception.CoreException;
import com.gp.info.InfoId;
import com.gp.dao.info.OrgHierInfo;
import com.gp.dao.info.UserInfo;
import com.gp.util.CommonUtils;
import com.gp.util.DateTimeUtils;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.Account;
import com.gp.web.model.OrgNode;
import com.gp.web.util.CustomWebUtils;

@Controller("ga-orghier-ctlr")
@RequestMapping("/ga")
public class OrgHierController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(OrgHierController.class);
	
	@RequestMapping("orghier")
	public ModelAndView doInitial(){
		
		return getJspModelView("ga/config/orghier");
	}
	
	@RequestMapping("orghier-add")
	public ModelAndView doAddOrgHier(HttpServletRequest request){
		
		if(LOGGER.isDebugEnabled()){
			CustomWebUtils.dumpRequestAttributes(request);
		}
		ActionResult aresult = new ActionResult();
		ModelAndView mav = getJsonModelView();
		OrgNode params = new OrgNode();		
		readRequestData(request, params);
		Long parentId = null;
		if(StringUtils.isNotBlank(params.getParent()) && CommonUtils.isNumeric(params.getParent())){
		
			parentId = Long.valueOf(params.getParent());
		}else {
			
			parentId = GeneralConstants.ORGHIER_ROOT;
		}
		OrgHierInfo orghier = new OrgHierInfo();
		
		orghier.setParentOrg(parentId);
		orghier.setAdmin(params.getAdmin());
		orghier.setDescription(params.getDescription());
		orghier.setEmail(params.getEmail());
		orghier.setManager(params.getManager());
		orghier.setOrgName(params.getText());
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		try{
			OrgHierFacade.newOrgHier(accesspoint, principal, orghier);
			aresult.setState(ActionResult.SUCCESS);
			aresult.setMessage(getMessage("mesg.new.orghier"));
		}catch(CoreException ce){
			aresult.setState(ActionResult.ERROR);
			aresult.setMessage(ce.getMessage());
			aresult.setDetailmsgs(ce.getValidateMessages());
		}
		
		mav.addAllObjects(aresult.asMap());
		return mav;
	}
	
	@RequestMapping("orghier-save")
	public ModelAndView doSaveOrgHier(HttpServletRequest request){
		
		if(LOGGER.isDebugEnabled()){
			CustomWebUtils.dumpRequestAttributes(request);
		}
		ActionResult aresult = new ActionResult();
		ModelAndView mav = getJsonModelView();
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		
		OrgNode params = new OrgNode();		
		readRequestData(request, params);
		InfoId<Long> nodeId = null;
		if(StringUtils.isNotBlank(params.getId()) && CommonUtils.isNumeric(params.getId())){
			Long nid = Long.valueOf(params.getId());
			nodeId = IdKey.ORG_HIER.getInfoId( nid);
		}else {
			aresult.setState(ActionResult.ERROR);
			aresult.setMessage(getMessage("mesg.post.unqualified"));
			mav.addAllObjects(aresult.asMap());
			return mav;
		}
		
		try{
			OrgHierInfo orghier = OrgHierFacade.findOrgHier(accesspoint, principal, nodeId);
			if(null != orghier){
				orghier.setAdmin(params.getAdmin());
				orghier.setDescription(params.getDescription());
				orghier.setEmail(params.getEmail());
				orghier.setManager(params.getManager());
				orghier.setOrgName(params.getText());
				OrgHierFacade.saveOrgHier(accesspoint, principal, orghier);
				aresult.setState(ActionResult.SUCCESS);
				aresult.setMessage(getMessage("mesg.save.orghier"));
			}else{
				aresult.setState(ActionResult.ERROR);
				aresult.setMessage(getMessage("mesg.target.none"));
			}
		}catch(CoreException ce){
			aresult.setState(ActionResult.ERROR);
			aresult.setMessage(ce.getMessage());
			aresult.setDetailmsgs(ce.getValidateMessages());
		}
		mav.addAllObjects(aresult.asMap());
		return mav;
	}
	
	@RequestMapping("orghier-member-add")
	public ModelAndView doAddOrgHierMember(HttpServletRequest request){
		
		if(LOGGER.isDebugEnabled()){
			CustomWebUtils.dumpRequestAttributes(request);
		}
		String orgIdStr = super.readRequestParam("org_id");
		InfoId<Long> nodeId = null;
		if(StringUtils.isNotBlank(orgIdStr) && CommonUtils.isNumeric(orgIdStr)){
			
			Long nid = Long.valueOf(orgIdStr);
			nodeId = IdKey.ORG_HIER.getInfoId(nid);
		}
		Account[] users = super.readRequestJson("users", Account[].class);
		String[] accounts = new String[users.length];
		int count = 0;
		for(Account a: users){
			accounts[count] = a.getAccount();
			count++;
		}
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		ActionResult aresult = new ActionResult();
		try{
			OrgHierFacade.addOrgHierMember(accesspoint, principal, nodeId, accounts);
			aresult.setState(ActionResult.SUCCESS);
			aresult.setMessage(getMessage("mesg.save.org.mbr"));
		}catch(CoreException ce){
			aresult.setState(ActionResult.ERROR);
			aresult.setMessage(ce.getMessage());
			aresult.setDetailmsgs(ce.getValidateMessages());
		}
		
		ModelAndView mav = getJsonModelView();
		mav.addAllObjects(aresult.asMap());
		return mav;
	}

	@RequestMapping("orghier-member-remove")
	public ModelAndView doRemoveOrgHierMember(HttpServletRequest request){
		
		if(LOGGER.isDebugEnabled()){
			CustomWebUtils.dumpRequestAttributes(request);
		}
		String orgIdStr = super.readRequestParam("org_id");
		InfoId<Long> nodeId = null;
		if(StringUtils.isNotBlank(orgIdStr) && CommonUtils.isNumeric(orgIdStr)){
			
			Long nid = Long.valueOf(orgIdStr);
			nodeId = IdKey.ORG_HIER.getInfoId( nid);
		}
		String account = super.readRequestParam("account");
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		ActionResult aresult = new ActionResult();
		
		try{
			OrgHierFacade.removeOrgHierMember(accesspoint, principal, nodeId, account);
			aresult.setState(ActionResult.SUCCESS);
			aresult.setMessage(getMessage("mesg.remove.org.mbr"));
		}catch(CoreException ce){
			aresult.setState(ActionResult.ERROR);
			aresult.setMessage(ce.getMessage());
			aresult.setDetailmsgs(ce.getValidateMessages());
		}

		ModelAndView mav = getJsonModelView();
		mav.addAllObjects(aresult.asMap());
		return mav;
	}
	
	@RequestMapping("orghier-member-search")
	public ModelAndView doOrgHierMemberSearch(HttpServletRequest request){
		
		if(LOGGER.isDebugEnabled()){
			CustomWebUtils.dumpRequestAttributes(request);
		}
		String orgIdStr = super.readRequestParam("org_id");
		InfoId<Long> nodeId = null;
		if(StringUtils.isNotBlank(orgIdStr) && CommonUtils.isNumeric(orgIdStr)){
			
			Long nid = Long.valueOf(orgIdStr);
			nodeId = IdKey.ORG_HIER.getInfoId( nid);
		}

		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		ActionResult aresult = new ActionResult();
		ModelAndView mav = getJsonModelView();
		
		try{
			List<UserInfo> ulist = OrgHierFacade.findOrgHierMembers(accesspoint, principal, nodeId);
			List<Account> list = new ArrayList<Account>();

			for(UserInfo info: ulist){
				
				Account ui = new Account();
				ui.setSourceId(info.getSourceId());
				ui.setUserId(info.getInfoId().getId());
				ui.setAccount(info.getAccount());
				ui.setEmail(info.getEmail());
				ui.setMobile(info.getMobile());
				ui.setType(info.getType());
				if(info.getCreateDate() != null)
					ui.setCreateDate(DateTimeUtils.toYearMonthDay(info.getCreateDate()));
				
				ui.setName(info.getFullName());
				ui.setState(info.getState());
	
				list.add(ui);
			}	
			aresult.setData(list);
			aresult.setState(ActionResult.SUCCESS);
			aresult.setMessage(getMessage("mesg.find.org.mbrs"));
		}catch(CoreException ce){
			aresult.setState(ActionResult.ERROR);
			aresult.setMessage(ce.getMessage());
			aresult.setDetailmsgs(ce.getValidateMessages());
		}
		
		mav.addAllObjects(aresult.asMap());
		return mav;
	}
}
