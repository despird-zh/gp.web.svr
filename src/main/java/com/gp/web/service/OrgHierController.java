package com.gp.web.service;

import java.util.ArrayList;
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
import com.gp.common.GeneralConstants;
import com.gp.common.IdKey;
import com.gp.common.Principal;
import com.gp.common.SystemOptions;
import com.gp.core.MasterFacade;
import com.gp.core.OrgHierFacade;
import com.gp.exception.CoreException;
import com.gp.info.InfoId;
import com.gp.svc.info.UserLiteInfo;
import com.gp.dao.info.OrgHierInfo;
import com.gp.dao.info.SysOptionInfo;
import com.gp.util.CommonUtils;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.Account;
import com.gp.web.model.OrgNode;
import com.gp.web.servlet.ServiceFilter;
import com.gp.web.util.ExWebUtils;

@Controller
@RequestMapping(ServiceFilter.FILTER_PREFIX)
public class OrgHierController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(OrgHierController.class);
	
	@RequestMapping(
		    value = "org-node-add.do", 
		    method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doAddOrgHier(@RequestBody String payload){
		
		if(LOGGER.isDebugEnabled()){
			ExWebUtils.dumpRequestAttributes(request);
		}
		ActionResult aresult = new ActionResult();
		ModelAndView mav = getJsonModelView();
		OrgNode params = super.readRequestBody(payload, OrgNode.class);
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
		orghier.setOrgName(params.getTitle());
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		try{
			
			OrgHierFacade.newOrgHier(accesspoint, principal, orghier);
			aresult = ActionResult.success(getMessage("mesg.new.orghier"));
			
		}catch(CoreException ce){
			aresult = super.wrapResult(ce);
		}
		
		mav.addAllObjects(aresult.asMap());
		return mav;
	}
	
	@RequestMapping(
		    value = "org-node-save.do", 
		    method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doSaveOrgHier(@RequestBody String payload){
		
		if(LOGGER.isDebugEnabled()){
			ExWebUtils.dumpRequestAttributes(request);
		}
		ActionResult aresult = new ActionResult();
		ModelAndView mav = getJsonModelView();
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		
		OrgNode params = super.readRequestBody(payload, OrgNode.class);
		InfoId<Long> nodeId = null;
		if(StringUtils.isNotBlank(params.getId()) && CommonUtils.isNumeric(params.getId())){
			Long nid = Long.valueOf(params.getId());
			nodeId = IdKey.ORG_HIER.getInfoId( nid);
		}else {
			aresult = ActionResult.failure(getMessage("mesg.post.unqualified"));
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
				orghier.setOrgName(params.getTitle());
				OrgHierFacade.saveOrgHier(accesspoint, principal, orghier);
				aresult = ActionResult.success(getMessage("mesg.save.orghier"));
			}else{
				aresult = ActionResult.success(getMessage("mesg.target.none"));
			}
		}catch(CoreException ce){
			aresult = super.wrapResult(ce);
		}
		mav.addAllObjects(aresult.asMap());
		return mav;
	}
	
	@RequestMapping(
		    value = "org-member-add.do", 
		    method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doAddOrgHierMember(@RequestBody String payload){
		
		if(LOGGER.isDebugEnabled()){
			ExWebUtils.dumpRequestAttributes(request);
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
			aresult = ActionResult.success(getMessage("mesg.save.org.mbr"));
		}catch(CoreException ce){
			aresult = super.wrapResult(ce);
		}
		
		ModelAndView mav = getJsonModelView();
		mav.addAllObjects(aresult.asMap());
		return mav;
	}

	@RequestMapping(
		    value = "org-member-remove.do", 
		    method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doRemoveOrgHierMember(@RequestBody String payload){
		
		if(LOGGER.isDebugEnabled()){
			ExWebUtils.dumpRequestAttributes(request);
		}
		
		Map<String, String > paramap = super.readRequestJson(payload);
		String orgIdStr = paramap.get("org-id");
		InfoId<Long> nodeId = null;
		if(StringUtils.isNotBlank(orgIdStr) && CommonUtils.isNumeric(orgIdStr)){
			
			Long nid = Long.valueOf(orgIdStr);
			nodeId = IdKey.ORG_HIER.getInfoId( nid);
		}
		String account = paramap.get("account");
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		ActionResult aresult = new ActionResult();
		
		try{
			OrgHierFacade.removeOrgHierMember(accesspoint, principal, nodeId, account);
			aresult = ActionResult.success(getMessage("mesg.remove.org.mbr"));
		}catch(CoreException ce){
			aresult = super.wrapResult(ce);
		}

		ModelAndView mav = getJsonModelView();
		mav.addAllObjects(aresult.asMap());
		return mav;
	}
	
	@RequestMapping(
		    value = "org-members-query.do", 
		    method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doOrgHierMemberSearch(@RequestBody String payload){
		
		if(LOGGER.isDebugEnabled()){
			ExWebUtils.dumpRequestAttributes(request);
		}
		Map<String, String > paramap = super.readRequestJson(payload);
		String orgIdStr = paramap.get("org-id");
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
			SysOptionInfo opt = MasterFacade.findSystemOption(accesspoint, principal, SystemOptions.PUBLIC_ACCESS);
			
			List<UserLiteInfo> ulist = OrgHierFacade.findOrgHierMembers(accesspoint, principal, nodeId);
			for(UserLiteInfo uinfo : ulist){
				String fulllink = opt.getOptionValue() + "/" + ServiceHelper.IMAGE_CACHE_PATH + '/' + uinfo.getAvatarLink();
				uinfo.setAvatarLink(fulllink);
			}
			aresult = ActionResult.success(getMessage("mesg.find.org.mbrs"));
			aresult.setData(ulist);
		}catch(CoreException ce){
			aresult = super.wrapResult(ce);
		}
		
		mav.addAllObjects(aresult.asMap());
		return mav;
	}
	
	@RequestMapping(
		    value = "org-hier-query.do", 
		    method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doGetOrghierNodes(@RequestBody String payload){
		
		ActionResult result = null;
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		
		Map<String, String > paramap = super.readRequestJson(payload);
		String orgIdStr = paramap.get("org-id");

		List<OrgNode> olist = new ArrayList<OrgNode>();		
		Long orgId = null;
		if(StringUtils.isNotBlank(orgIdStr) && CommonUtils.isNumeric(orgIdStr)){
		
			orgId = Long.valueOf(orgIdStr);
		}else if(StringUtils.isNotBlank(orgIdStr) && !CommonUtils.isNumeric(orgIdStr)){
			
			orgId = GeneralConstants.ORGHIER_ROOT;
		}else{
			result = ActionResult.success(getMessage("mesg.find.orgs"));
			result.setData(olist);
			ModelAndView mav = getJsonModelView();
			return mav.addAllObjects(result.asMap());
		}		

		try{
			InfoId<Long> oid = IdKey.ORG_HIER.getInfoId(orgId);
			List<OrgHierInfo> gresult = OrgHierFacade.findChildOrgHiers(accesspoint, principal, 
					oid);
			Map<Long, Integer> grandcnt = OrgHierFacade.findOrgHierGrandNodeCount(accesspoint, principal, 
					oid);
			
			for(OrgHierInfo orghier : gresult){
				OrgNode node = new OrgNode();
				Integer gcnt = grandcnt.get(orghier.getInfoId().getId());
				
				node.setId(String.valueOf(orghier.getInfoId().getId()));
				
				if(GeneralConstants.ORGHIER_ROOT != orghier.getParentOrg()){
					node.setParent(String.valueOf(orghier.getParentOrg()));
				}
				node.setTitle(orghier.getOrgName());
				node.setAdmin(orghier.getAdmin());
				node.setDescription(orghier.getDescription());
				node.setEmail(orghier.getEmail());
				node.setManager(orghier.getManager());
				node.setHasChild((gcnt == null ? 0 : gcnt) > 0);
				olist.add(node);
			}
			result = ActionResult.success(getMessage("mesg.find.orgs"));
			result.setData(olist);
		}catch(CoreException ce){
			result = super.wrapResult(ce);
		}
		
		ModelAndView mav = getJsonModelView();
		return mav.addAllObjects(result.asMap());		
	}
	
	@RequestMapping(
		    value = "org-node-query.do", 
		    method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doGetOrghierNode(@RequestBody String payload){
		
		ActionResult result = null;
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		
		Map<String, String > paramap = super.readRequestJson(payload);
		String orgIdStr = paramap.get("org-id");

		Long orgId = null;
		if(StringUtils.isNotBlank(orgIdStr) && CommonUtils.isNumeric(orgIdStr)){
		
			orgId = Long.valueOf(orgIdStr);
		}else if(StringUtils.isNotBlank(orgIdStr) && !CommonUtils.isNumeric(orgIdStr)){
			
			orgId = GeneralConstants.ORGHIER_ROOT;
		}else{
			result = ActionResult.failure(getMessage("excp.find.orgs"));
			ModelAndView mav = getJsonModelView();
			return mav.addAllObjects(result.asMap());
		}

		try{
			InfoId<Long> oid = IdKey.ORG_HIER.getInfoId(orgId);
			OrgHierInfo orghier = OrgHierFacade.findOrgHier(accesspoint, principal, oid);

			OrgNode node = new OrgNode();
			
			node.setId(String.valueOf(orghier.getInfoId().getId()));
			
			if(GeneralConstants.ORGHIER_ROOT != orghier.getParentOrg()){
				node.setParent(String.valueOf(orghier.getParentOrg()));
			}
			node.setTitle(orghier.getOrgName());
			node.setAdmin(orghier.getAdmin());
			node.setDescription(orghier.getDescription());
			node.setEmail(orghier.getEmail());
			node.setManager(orghier.getManager());
			
			result = ActionResult.success(getMessage("mesg.find.orgs"));
			result.setData(node);
			
		}catch(CoreException ce){
			result = super.wrapResult(ce);
		}
		
		ModelAndView mav = getJsonModelView();
		return mav.addAllObjects(result.asMap());		
	}
}
