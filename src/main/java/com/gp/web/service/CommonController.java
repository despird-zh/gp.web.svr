package com.gp.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.gp.common.GeneralConstants;
import com.gp.common.IdKey;
import com.gp.common.Principal;
import com.gp.core.OrgHierFacade;
import com.gp.core.SecurityFacade;
import com.gp.core.SourceFacade;
import com.gp.core.StorageFacade;
import com.gp.core.WorkgroupFacade;
import com.gp.dao.info.OrgHierInfo;
import com.gp.dao.info.SourceInfo;
import com.gp.dao.info.StorageInfo;
import com.gp.exception.CoreException;
import com.gp.exception.WebException;
import com.gp.info.InfoId;
import com.gp.info.KVPair;
import com.gp.svc.info.UserExtInfo;
import com.gp.util.CommonUtils;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.Account;
import com.gp.web.model.OrgNode;
import com.gp.web.servlet.ServiceFilter;

@Controller
@RequestMapping(ServiceFilter.FILTER_PREFIX)
public class CommonController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(CommonController.class);
	
	@RequestMapping(value = "common-entity-list.do",
	method = RequestMethod.POST,
    consumes = {"text/plain", "application/*"})
	public ModelAndView doGetEntityNodeList(@RequestBody String payload) throws WebException {

		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		
		Map<String, String> paramMap = super.readRequestJson(payload) ;
		
		String namecond = paramMap.get("instance_name");
		
		ModelAndView mav = getJsonModelView();		
		List<KVPair<String, String>> enlist = new ArrayList<KVPair<String, String>>();
		ActionResult result = null;
		
		try{
			List<SourceInfo> gresult = SourceFacade.findSources(accesspoint, principal, namecond);
			for(SourceInfo einfo : gresult){
				Integer id = einfo.getInfoId().getId();
				KVPair<String, String> kv = new KVPair<String, String>(String.valueOf(id), einfo.getSourceName());
				enlist.add(kv);
			}
			
			result = ActionResult.failure(getMessage("mesg.find.sources"));
			result.setData(enlist);
			
		}catch(CoreException ce){
			result = super.wrapResult(ce);
		}

		return mav.addAllObjects(result.asMap());
	}

	/**
	 * Get the storage list,  
	 **/
	@RequestMapping(value = "common-storage-list.do",
	method = RequestMethod.POST,
    consumes = {"text/plain", "application/*"})
	public ModelAndView doGetStorageList(@RequestBody String payload){
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		
		Map<String, String> paramMap = super.readRequestJson(payload) ;
		
		String namecond = paramMap.get("storage_name");
	
		ModelAndView mav = getJsonModelView();		
		List<KVPair<String, String>> stglist = new ArrayList<KVPair<String, String>>();
		ActionResult result = null;
		String[] types = null;
		String[] states = null;
		try{
			
			List<StorageInfo> gresult = StorageFacade.findStorages(accesspoint, principal, namecond, types, states );
			for(StorageInfo sinfo : gresult){
				Integer id = sinfo.getInfoId().getId();
				KVPair<String, String> kv = new KVPair<String, String>(String.valueOf(id), 
						sinfo.getStorageName());
				stglist.add(kv);
			}
			
			result = ActionResult.failure(getMessage("mesg.find.sources"));
			result.setData(stglist);

		}catch(CoreException ce){
			result = super.wrapResult(ce);
		}
	
		return mav.addAllObjects(result.asMap());
	} 
	
	/**
	 * Support Select User Dialog to list all the users in system 
	 **/
	@RequestMapping(value = "common-user-list.do",
	method = RequestMethod.POST,
    consumes = {"text/plain", "application/*"})
	public ModelAndView doGetUserList(@RequestBody String payload){

		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		Map<String, String> paramMap = super.readRequestJson(payload) ;
		
		String uname = paramMap.get("user_name");
		String instanceIdStr = paramMap.get("instanceId");
		Integer instanceId = StringUtils.isBlank(instanceIdStr) ? null:NumberUtils.toInt(instanceIdStr);
		
		ActionResult ars = new ActionResult();
		
		List<Account> list = new ArrayList<Account>();
		try{
			
			List<UserExtInfo> cresult = SecurityFacade.findAccounts(accesspoint, principal, uname, instanceId, new String[0],new String[0]);
		
			for(UserExtInfo info: cresult){
				Account ui = new Account();
				ui.setSourceId(info.getSourceId());
				ui.setUserId(info.getInfoId().getId());
				ui.setAccount(info.getAccount());
				ui.setEmail(info.getEmail());
				ui.setMobile(info.getMobile());
				ui.setType(info.getType());
				ui.setName(info.getFullName());
				ui.setState(info.getState());
				ui.setSourceName(info.getSourceName());
				list.add(ui);
			}			

			ars = ActionResult.success(getMessage("mesg.find.users"));
			ars.setData(list);
			
		}catch(CoreException ce){
			ars = super.wrapResult(ce);
		}
		
		ModelAndView mav = getJsonModelView();		
		mav.addAllObjects(ars.asMap());

		return mav;
		
	}
	
	@RequestMapping(value = "common-org-nodes.do",
	method = RequestMethod.POST,
    consumes = {"text/plain", "application/*"})
	public ModelAndView doGetOrghierNodes(@RequestBody String payload){
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		Map<String, String> paramMap = super.readRequestJson(payload) ;
		String orgIdStr = paramMap.get("org-id");

		List<OrgNode> olist = new ArrayList<OrgNode>();		
		Long orgId = null;
		if(StringUtils.isNotBlank(orgIdStr) && CommonUtils.isNumeric(orgIdStr)){
		
			orgId = Long.valueOf(orgIdStr);
		}else if(StringUtils.isNotBlank(orgIdStr) && !CommonUtils.isNumeric(orgIdStr)){
			
			orgId = GeneralConstants.ORGHIER_ROOT;
		}else{
			
			ModelAndView mav = getJsonModelView(olist);
			return mav;
		}		
		ActionResult ars = new ActionResult();
		try{
			InfoId<Long> oid = IdKey.ORG_HIER.getInfoId(orgId);
			List<OrgHierInfo> gresult = OrgHierFacade.findAllChildOrgHiers(accesspoint, principal, 
					oid);
			
			for(OrgHierInfo orghier : gresult){
				OrgNode node = new OrgNode();
				node.setId(String.valueOf(orghier.getInfoId().getId()));
				
				if(GeneralConstants.ORGHIER_ROOT != orghier.getParentOrg()){
					node.setParent(String.valueOf(orghier.getParentOrg()));
				}
				node.setText(orghier.getOrgName());
				node.setAdmin(orghier.getAdmin());
				node.setDescription(orghier.getDescription());
				node.setEmail(orghier.getEmail());
				node.setManager(orghier.getManager());
				olist.add(node);
			}
			
			ars = ActionResult.success(getMessage("mesg.find.orgnodes"));
			ars.setData(olist);
			
		}catch(CoreException ce){
			ars = super.wrapResult(ce);
		}
		
		ModelAndView mav = getJsonModelView();
		return mav.addAllObjects(ars.asMap());		
	}

	/**
	 * This is used in dropdown widget to list available users could be assigned to a given workgroup
	 **/
	@RequestMapping(value = "common-avail-user-list.do",
	method = RequestMethod.POST,
    consumes = {"text/plain", "application/*"})
	public ModelAndView doGetAvailableUserList(@RequestBody String payload){
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		
		Map<String, String> paramMap = super.readRequestJson(payload) ;
		
		String wgroupid = paramMap.get("wgroup_id");
		String account = paramMap.get("account");
	
		List<Account> list = new ArrayList<Account>();

		InfoId<Long> wkey = null;
		if(StringUtils.isNotBlank(wgroupid) && CommonUtils.isNumeric(wgroupid)){
			
			wkey = IdKey.WORKGROUP.getInfoId(Long.valueOf(wgroupid));
		}

		ActionResult ars = new ActionResult();
		ModelAndView mav = super.getJsonModelView();
		try{
			List<UserExtInfo> ulist = WorkgroupFacade.findWrokgroupAvailUsers(accesspoint, principal, wkey, account);
			
			for(UserExtInfo info: ulist){
				
				Account ui = new Account();
				ui.setSourceId(info.getSourceId());
				ui.setUserId(info.getInfoId().getId());
				ui.setAccount(info.getAccount());
				ui.setEmail(info.getEmail());
				ui.setMobile(info.getMobile());
				ui.setType(info.getType());
				ui.setName(info.getFullName());
				ui.setState(info.getState());
				ui.setSourceName(info.getSourceName());
				list.add(ui);
			}			

			ars = ActionResult.success(getMessage("mesg.find.users"));
			ars.setData(list);

		}catch(CoreException ce){
			ars = super.wrapResult(ce);
		}
		
		return mav.addAllObjects(ars.asMap());	
	}
}
