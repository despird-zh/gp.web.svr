package com.gp.web.view.workgroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.common.AccessPoint;
import com.gp.common.GeneralConstants;
import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.common.GPrincipal;
import com.gp.core.WorkgroupFacade;
import com.gp.exception.CoreException;
import com.gp.dao.info.GroupInfo;
import com.gp.dao.info.GroupMemberInfo;
import com.gp.info.InfoId;
import com.gp.dao.info.UserInfo;
import com.gp.dao.info.WorkgroupInfo;
import com.gp.util.CommonUtils;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.Account;
import com.gp.web.model.GroupMember;
import com.gp.web.model.UserGroup;
import com.gp.web.model.Workgroup;
import com.gp.web.util.ExWebUtils;
import com.gp.web.util.ServletUtils;
import com.gp.web.model.WGroupMember;

@Controller
public class WorkgroupAddController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(WorkgroupAddController.class);
	
	@RequestMapping("workgroup-new")
	public ModelAndView doInitial(){
		
		return getJspModelView("config/workgroup-new");
	}
	
	@RequestMapping("workgroup-add")
	public ModelAndView doWorkgroupAdd(HttpServletRequest request){
		
		if(LOGGER.isDebugEnabled())
			ExWebUtils.dumpRequestAttributes(request);
		
		ModelAndView mav = getJsonModelView();
		Workgroup group = new Workgroup();
		readRequestParams( group);
		
		GPrincipal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		ActionResult result = new ActionResult();
		WorkgroupInfo info = new WorkgroupInfo();
		
		info.setSourceId(GeneralConstants.LOCAL_SOURCE);// set local workgroup id
		info.setWorkgroupName(group.getWorkgroupName());
		info.setDescription(group.getDescription());
		info.setState(group.getState());
		info.setAdmin(group.getAdmin());
		info.setManager(group.getManager());
		info.setCreator(principal.getAccount());
		info.setCreateDate(new Date(System.currentTimeMillis()));
		info.setOrgId(group.getOrgId());
		info.setPublishEnable(group.getPublishOn());
		info.setNetdiskEnable(group.getNetdiskOn());
		info.setPostEnable(group.getTopicOn());
		info.setTaskEnable(group.getTaskOn());
		info.setShareEnable(group.getShareOn());
		info.setLinkEnable(group.getLinkOn());
		info.setStorageId(group.getStorageId());
	
		// convert the url into disk path, ignore [..]
		String basePath = ServletUtils.getBaseUrl(request);
		String imagePath = request.getServletContext().getRealPath(group.getImagePath().substring(basePath.length()));
		LOGGER.debug("image file path : {}", imagePath);

		try{
			WorkgroupFacade.newWorkgroup(accesspoint, principal, 
				info, 
				(long)group.getPublishCapacity()*1024*1024, 
				(long)group.getNetdiskCapacity()*1024*1024,
				imagePath);
			
			result = ActionResult.success(getMessage("mesg.new.wgroup"));
		}catch(CoreException ce){
			result = super.wrapResult(ce);
		}
	
		mav.addAllObjects(result.asMap());

		return mav;

	}
	
	@RequestMapping("workgroup-member-search")
	public ModelAndView doSearchWorkgroupMembers(HttpServletRequest request){
		
		if(LOGGER.isDebugEnabled())
			ExWebUtils.dumpRequestAttributes(request);
		
		String account = super.readRequestParam("account");
		String enode = super.readRequestParam("enode_id");
		String wgroupid = super.readRequestParam("wgroup_id");
		
		GPrincipal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);

		InfoId<Integer> entityId = null;
		if(StringUtils.isNotBlank(enode) && CommonUtils.isNumeric(enode)){
			
			entityId = IdKeys.getInfoId(IdKey.GP_SOURCES, Integer.valueOf(enode));
		}
		
		InfoId<Long> wkey = null;
		if(StringUtils.isNotBlank(wgroupid) && CommonUtils.isNumeric(wgroupid)){
			
			wkey = IdKeys.getInfoId(IdKey.GP_WORKGROUPS, Long.valueOf(wgroupid));
		}

		ModelAndView mav = getJsonModelView();
		List<WGroupMember> list = new ArrayList<WGroupMember>();
		ActionResult result = null;
		try{
			
			List<GroupMemberInfo> ulist = WorkgroupFacade.findWorkgroupMembers(accesspoint, principal, 
					wkey, account, entityId);
			for(GroupMemberInfo info: ulist){
				
				WGroupMember wmember = new WGroupMember();
				wmember.setAccount(info.getAccount());
				wmember.setEmail(info.getEmail());
				wmember.setSourceName(info.getSourceName());
				wmember.setRole(info.getRole());
				wmember.setType(info.getUserType());
				wmember.setUname(info.getUserName());
				
				list.add(wmember);
			}			

			result = ActionResult.success(getMessage("mesg.find.wgroup.mbr"));
			result.setData(list);
			
		}catch(CoreException ce){

			result = super.wrapResult(ce);
			
		}
		mav.addAllObjects(result.asMap());
		return mav;
	}
	
	@RequestMapping("workgroup-member-remove")
	public ModelAndView doRemoveWorkgroupMember(HttpServletRequest request){
		
		if(LOGGER.isDebugEnabled())
			ExWebUtils.dumpRequestAttributes(request);
		
		String account = super.readRequestParam("account");
		String wgroupid = super.readRequestParam("wgroup_id");
		
		GPrincipal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		
		InfoId<Long> wkey = null;
		if(StringUtils.isNotBlank(wgroupid) && CommonUtils.isNumeric(wgroupid)){
			
			wkey = IdKeys.getInfoId(IdKey.GP_WORKGROUPS, Long.valueOf(wgroupid));
		}

		ActionResult aresult = new ActionResult();
		try{
			WorkgroupFacade.removeWorkgroupMember(accesspoint, principal, wkey, account);
			aresult = ActionResult.success(getMessage("mesg.remove.wgroup.mbr"));
		}catch(CoreException ce){
			aresult = super.wrapResult(ce);
		}
		
		ModelAndView mav = getJsonModelView();
		mav.addAllObjects(aresult.asMap());
		return mav;
	}
	
	@RequestMapping("workgroup-member-add")
	public ModelAndView doAddWorkgroupMember(HttpServletRequest request){
		
		if(LOGGER.isDebugEnabled())
			ExWebUtils.dumpRequestAttributes(request);
		
		String account = super.readRequestParam("account");
		String wgroupid = super.readRequestParam("wgroup_id");
		String role = super.readRequestParam("role");
		
		GPrincipal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		
		InfoId<Long> wkey = null;
		if(StringUtils.isNotBlank(wgroupid) && CommonUtils.isNumeric(wgroupid)){
			
			wkey = IdKeys.getInfoId(IdKey.GP_WORKGROUPS , Long.valueOf(wgroupid));
		}

		ActionResult aresult = new ActionResult();
		try{
			WorkgroupFacade.addWorkgroupMember(accesspoint, principal, wkey, account,role);
			aresult = ActionResult.success(getMessage("mesg.add.wgroup.mbr"));
		}catch(CoreException ce){
			aresult = super.wrapResult(ce);
		}
		
		ModelAndView mav = getJsonModelView();
		mav.addAllObjects(aresult.asMap());
		return mav;
	}
	
	@RequestMapping("workgroup-group-add")
	public ModelAndView doAddWorkgroupGroup(HttpServletRequest request){
		
		if(LOGGER.isDebugEnabled())
			ExWebUtils.dumpRequestAttributes(request);
		
		String group = super.readRequestParam("group");
		String wgroupid = super.readRequestParam("wgroup_id");
		String description = super.readRequestParam("description");
		
		GPrincipal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		
		GroupInfo ginfo = new GroupInfo();

		if(StringUtils.isNotBlank(wgroupid) && CommonUtils.isNumeric(wgroupid)){
			
			ginfo.setManageId(Long.valueOf(wgroupid));
		}
		ginfo.setDescription(description);
		ginfo.setGroupName(group);

		ActionResult aresult = new ActionResult();
		try{
			WorkgroupFacade.newWorkgroupGroup(accesspoint, principal, ginfo);
			aresult = ActionResult.success(getMessage("mesg.new.wgroup.group"));
		}catch(CoreException ce){
			aresult = super.wrapResult(ce);
		}
		
		ModelAndView mav = getJsonModelView();
		mav.addAllObjects(aresult.asMap());
		return mav;
	}
	
	@RequestMapping("workgroup-group-search")
	public ModelAndView doSearchWorkgroupGroup(HttpServletRequest request){
		
		if(LOGGER.isDebugEnabled())
			ExWebUtils.dumpRequestAttributes(request);
		
		String group = super.readRequestParam("group");
		String wgroupid = super.readRequestParam("wgroup_id");
		
		GPrincipal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		InfoId<Long> wkey = null;
		if(StringUtils.isNotBlank(wgroupid) && CommonUtils.isNumeric(wgroupid)){
			
			wkey = IdKeys.getInfoId(IdKey.GP_WORKGROUPS, Long.valueOf(wgroupid));
		}

		ModelAndView mav = getJsonModelView();
		List<UserGroup> list = new ArrayList<UserGroup>();
		ActionResult aresult = new ActionResult();
		try{
			List<GroupInfo> ulist = WorkgroupFacade.findWorkgroupGroups(accesspoint, principal, wkey, group);
			for(GroupInfo info: ulist){
				
				UserGroup ug = new UserGroup();
				ug.setGroupId(info.getInfoId().getId());
				ug.setDescription(info.getDescription());
				ug.setGroup(info.getGroupName());
				ug.setWorkgroupId(info.getManageId());
				list.add(ug);
			}

			aresult = ActionResult.success(getMessage("mesg.find.wgroup.group"));

			aresult.setData(list);
		}catch(CoreException ce){

			aresult = super.wrapResult(ce);
		}
		
		return mav.addAllObjects(aresult.asMap());		
	}
	
	@RequestMapping("workgroup-group-remove")
	public ModelAndView doRemoveWorkgroupGroup(HttpServletRequest request){
		
		if(LOGGER.isDebugEnabled())
			ExWebUtils.dumpRequestAttributes(request);
		
		String groupid = super.readRequestParam("group_id");

		GPrincipal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		
		InfoId<Long> gid = null;
		if(StringUtils.isNotBlank(groupid) && CommonUtils.isNumeric(groupid)){
			
			gid = IdKeys.getInfoId(IdKey.GP_GROUPS, Long.valueOf(groupid));
		}

		ActionResult aresult = new ActionResult();
		try{
			WorkgroupFacade.removeWorkgroupGroup(accesspoint, principal, gid);
			aresult = ActionResult.success(getMessage("mesg.remove.wgroup.group"));
		}catch(CoreException ce){
			aresult = super.wrapResult(ce);
		}
		
		ModelAndView mav = getJsonModelView();
		mav.addAllObjects(aresult.asMap());
		return mav;
	}
	
	@RequestMapping("workgroup-group-member-add")
	public ModelAndView doAddWorkgroupGroupMember(HttpServletRequest request){
		
		if(LOGGER.isDebugEnabled())
			ExWebUtils.dumpRequestAttributes(request);
		
		String groupid = super.readRequestParam("group_id");

		GPrincipal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		
		InfoId<Long> gid = null;
		if(StringUtils.isNotBlank(groupid) && CommonUtils.isNumeric(groupid)){
			
			gid = IdKeys.getInfoId(IdKey.GP_GROUPS, Long.valueOf(groupid));
		}
		Account[] users = super.readRequestJson("users", Account[].class);
		String[] accounts = new String[users.length];
		int count = 0;
		for(Account a: users){
			accounts[count] = a.getAccount();
			count++;
		}
		
		ActionResult aresult = new ActionResult();
		try{
			WorkgroupFacade.addWorkgroupGroupMembers(accesspoint, principal, gid, accounts);
			aresult = ActionResult.success(getMessage("mesg.add.group.mbr"));
		}catch(CoreException ce){
			aresult = super.wrapResult(ce);
		}
		
		ModelAndView mav = getJsonModelView();
		mav.addAllObjects(aresult.asMap());
		return mav;
	}
	
	@RequestMapping("workgroup-group-member-search")
	public ModelAndView doSearchWorkgroupGroupMember(HttpServletRequest request){
		
		if(LOGGER.isDebugEnabled())
			ExWebUtils.dumpRequestAttributes(request);
		
		String groupid = super.readRequestParam("group_id");
		GPrincipal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);

		InfoId<Long> gid = null;
		if(StringUtils.isNotBlank(groupid) && CommonUtils.isNumeric(groupid)){
			
			gid = IdKeys.getInfoId(IdKey.GP_GROUPS, Long.valueOf(groupid));
		}

		ModelAndView mav = getJsonModelView();
		List<GroupMember> list = new ArrayList<GroupMember>();
		ActionResult aresult = null;
		try{
			List<UserInfo> ulist = WorkgroupFacade.findWorkgroupGroupMembers(accesspoint, principal, gid);
			for(UserInfo info: ulist){
				
				GroupMember gm = new GroupMember();
				gm.setUname(info.getFullName());
				gm.setAccount(info.getAccount());
				gm.setEmail(info.getEmail());
				gm.setType(info.getType());
				list.add(gm);
			}

			aresult = ActionResult.success(getMessage("mesg.find.group.mbr"));
			aresult.setData(list);
		}catch(CoreException ce){

			aresult = super.wrapResult(ce);
		}

		return mav.addAllObjects(aresult.asMap());
	}
	
	@RequestMapping("workgroup-group-member-remove")
	public ModelAndView doRemoveWorkgroupGroupMember(HttpServletRequest request){
		
		if(LOGGER.isDebugEnabled())
			ExWebUtils.dumpRequestAttributes(request);
		
		String groupid = super.readRequestParam("group_id");
		String account = super.readRequestParam("account");
		GPrincipal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		
		InfoId<Long> gid = null;
		if(StringUtils.isNotBlank(groupid) && CommonUtils.isNumeric(groupid)){
			
			gid = IdKeys.getInfoId(IdKey.GP_GROUPS, Long.valueOf(groupid));
		}
		
		
		ActionResult aresult = new ActionResult();
		try{
			WorkgroupFacade.removeWorkgroupGroupMember(accesspoint, principal, gid, account);
			aresult = ActionResult.success(getMessage("mesg.remove.group.mbr"));
			
		}catch(CoreException ce){
			aresult = super.wrapResult(ce);
		}
		
		ModelAndView mav = getJsonModelView();
		mav.addAllObjects(aresult.asMap());
		return mav;
	}
}
