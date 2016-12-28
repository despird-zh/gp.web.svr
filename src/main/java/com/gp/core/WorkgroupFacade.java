package com.gp.core;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gp.audit.AccessPoint;
import com.gp.common.GeneralConstants;
import com.gp.common.GroupUsers;
import com.gp.common.IdKey;
import com.gp.common.Operations;
import com.gp.common.Principal;
import com.gp.common.ServiceContext;
import com.gp.exception.CoreException;
import com.gp.exception.ServiceException;
import com.gp.dao.info.OperLogInfo;
import com.gp.info.CombineInfo;
import com.gp.dao.info.GroupInfo;
import com.gp.dao.info.GroupMemberInfo;
import com.gp.dao.info.GroupUserInfo;
import com.gp.info.InfoId;
import com.gp.dao.info.TagInfo;
import com.gp.dao.info.UserInfo;
import com.gp.dao.info.WorkgroupInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.svc.OperLogService;
import com.gp.svc.CommonService;
import com.gp.svc.TagService;
import com.gp.svc.WorkgroupService;
import com.gp.svc.info.UserExtInfo;
import com.gp.svc.info.WorkgroupExtInfo;
import com.gp.svc.info.WorkgroupLite;

import com.gp.validate.ValidateMessage;
import com.gp.validate.ValidateUtils;

@Component
public class WorkgroupFacade {

	static Logger LOGGER = LoggerFactory.getLogger(WorkgroupFacade.class);
	
	private static WorkgroupService workgroupservice;
	
	private static CommonService idservice;
	
	private static OperLogService actlogservice;
	
	private static TagService tagservice;
	
	@Autowired
	private WorkgroupFacade(WorkgroupService workgroupservice,
			CommonService idservice,
			OperLogService actlogservice,
			TagService tagservice){
		WorkgroupFacade.workgroupservice = workgroupservice;
		WorkgroupFacade.idservice = idservice;
		WorkgroupFacade.actlogservice = actlogservice;
		WorkgroupFacade.tagservice = tagservice;
	}
	
	public static boolean newWorkgroup(AccessPoint accesspoint,
			Principal principal,
			WorkgroupInfo winfo, 
			Long pubcapacity, 
			Long pricapacity, 
			String imagePath)throws CoreException{
		
		boolean gresult = false;		
		
		// check the validation of user information
		Set<ValidateMessage> vmsg = ValidateUtils.validate(principal.getLocale(), winfo);
		if(CollectionUtils.isNotEmpty(vmsg)){ // fail pass validation
			CoreException cexcp = new CoreException(principal.getLocale(), "excp.validate");
			cexcp.addValidateMessages(vmsg);
			throw cexcp;
		}
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.NEW_WORKGROUP)){
			
			// amend the information key data
			if(!InfoId.isValid(winfo.getInfoId())){
				
				InfoId<Long> wkey = idservice.generateId( IdKey.WORKGROUP, Long.class);
				winfo.setInfoId(wkey);				
			}

			// amend the operation information
			svcctx.setOperationObject(winfo.getInfoId());
			svcctx.addOperationPredicates(winfo);

			// append the capacity setting to context and send to service
			svcctx.putContextData(WorkgroupService.CTX_KEY_IMAGE_PATH, imagePath);
			winfo.setSourceId(GeneralConstants.LOCAL_SOURCE);
			gresult = workgroupservice.newWorkgroup(svcctx, winfo, pubcapacity, pricapacity);
	
		}catch(ServiceException e){
			ContextHelper.stampContext(e, "excp.new.wgroup");
		}finally{
			
			ContextHelper.handleContext();
		}
		return gresult;	
	}
	
	public static Boolean updateWorkgroup(AccessPoint accesspoint,
			Principal principal,
			WorkgroupInfo winfo, 
			Long pubcapacity, 
			Long pricapacity, 
			String imagePath) throws CoreException{
			
		Boolean gresult = false;	
		// id not set return directly
		if(!InfoId.isValid(winfo.getInfoId())){
			
			CoreException cexcp = new CoreException(principal.getLocale(), "excp.save.wgroup");
			cexcp.addValidateMessage("wrokgroupid", "mesg.prop.miss");
			throw cexcp;
		}

		// check the validation of user information
		Set<ValidateMessage> vmsg = ValidateUtils.validate(principal.getLocale(), winfo);
		if(null != vmsg && vmsg.size() > 0){ // fail pass validation
			CoreException cexcp = new CoreException(principal.getLocale(), "excp.validate");
			cexcp.addValidateMessages(vmsg);
			throw cexcp;
		}
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.UPDATE_WORKGROUP)){
			
			// amend the operation information
			svcctx.setOperationObject(winfo.getInfoId());
			svcctx.addOperationPredicates(winfo);

			// append the capacity setting to context and send to service
			svcctx.putContextData(WorkgroupService.CTX_KEY_IMAGE_PATH, imagePath);
			
			gresult = workgroupservice.updateWorkgroup(svcctx, winfo, pubcapacity, pricapacity);
			
		}catch(ServiceException e){
			ContextHelper.stampContext(e, "excp.save.wgroup");

		}finally{
			
			ContextHelper.handleContext();
		}
		return gresult;	
	}
	
	public static WorkgroupInfo findWorkgroup(AccessPoint accesspoint,
			Principal principal,
			InfoId<Long> wkey)throws CoreException{
		
		WorkgroupInfo gresult = null;
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_WORKGROUP)){

			// amend the operation information
			svcctx.setOperationObject(wkey);
			
			gresult = workgroupservice.getWorkgroup(svcctx, wkey);
		}catch(ServiceException e){
			ContextHelper.stampContext(e ,"excp.find.wgroups");
		}finally{
			
			ContextHelper.handleContext();
		}
		
		return gresult;
	}
	
	public static WorkgroupExtInfo findWorkgroupExt(AccessPoint accesspoint,
			Principal principal,
			InfoId<Long> wkey)throws CoreException{
		
		WorkgroupExtInfo gresult = null;
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_WORKGROUP)){
			
			// amend the operation information
			svcctx.setOperationObject(wkey);
			
			gresult = workgroupservice.getWorkgroupExt(svcctx, wkey);
						
			
		}catch(ServiceException e){
			ContextHelper.stampContext(e,"excp.find.wgroups");
		}finally{
			
			ContextHelper.handleContext();
		}
		
		return gresult;
	}
	
	/**
	 * Find all the workgroup members 
	 **/
	public static List<GroupMemberInfo> findWorkgroupMembers(AccessPoint accesspoint,
			Principal principal,
			InfoId<Long> wkey, String uname,
			InfoId<Integer> entityid)throws CoreException{
		
		List<GroupMemberInfo> gresult = null;
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_WORKGROUP_USERS)){
			
			// amend the operation information
			svcctx.setOperationObject(wkey);
			
			gresult = workgroupservice.getWorkgroupMembers(svcctx, wkey, uname, entityid);

		}catch(ServiceException e){

			ContextHelper.stampContext(e, "excp.find.wgroup.mbr");
		}finally{
			
			ContextHelper.handleContext();
		}
		
		return gresult;
	}
	
	/**
	 * find all the workgroup members support paging on server-side 
	 **/
	public static PageWrapper<GroupMemberInfo> findWorkgroupMembers(AccessPoint accesspoint,
			Principal principal,
			InfoId<Long> wkey, String uname, 
			InfoId<Integer> entityid, PageQuery pagequery)throws CoreException{
		
		PageWrapper<GroupMemberInfo> gresult = null;
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_WORKGROUP_USERS)){

			// amend the operation information
			svcctx.setOperationObject(wkey);
			
			gresult = workgroupservice.getWorkgroupMembers(svcctx, wkey, uname, entityid, pagequery);

		}catch(ServiceException e){

			ContextHelper.stampContext(e, "excp.find.wgroup.mbr");
		}finally{
			
			ContextHelper.handleContext();
		}
		
		return gresult;
	}
	
	public static Boolean removeWorkgroupMember(AccessPoint accesspoint,
			Principal principal,
			InfoId<Long> wkey, String account)throws CoreException{
		
		Boolean gresult = false;
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.REMOVE_WORKGROUP_USER)){

			// amend the operation information
			svcctx.setOperationObject(wkey);
			svcctx.addOperationPredicates(new DefaultKeyValue("member",account));
			gresult = workgroupservice.removeWorkgroupMember(svcctx, wkey, account);

		}catch(ServiceException e){

			ContextHelper.stampContext(e, "excp.remove.wgroup.mbr");
		}finally{
			
			ContextHelper.handleContext();
		}
		
		return gresult;
	}
	
	public static Boolean addWorkgroupMember(AccessPoint accesspoint,
			Principal principal,
			InfoId<Long> wkey, String account, String role)throws CoreException{
		
		Boolean gresult = false;
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.ADD_WORKGROUP_USER)){

			// amend the operation information
			svcctx.setOperationObject(wkey);
			svcctx.addOperationPredicates(new DefaultKeyValue("member",account));

			GroupUserInfo wuinfo = new GroupUserInfo();
			wuinfo.setAccount(account);
			wuinfo.setRole(role);

			// check the validation of user information
			Set<ValidateMessage> vmsg = ValidateUtils.validate(principal.getLocale(), wuinfo);
			if(CollectionUtils.isNotEmpty(vmsg)){ // fail pass validation
				CoreException cexcp = new CoreException(principal.getLocale(), "excp.validate");
				cexcp.addValidateMessages(vmsg);
				throw cexcp;
			}
			
			gresult = workgroupservice.addWorkgroupMember(svcctx,wkey, wuinfo);

		}catch(ServiceException e){
			ContextHelper.stampContext(e, "excp.add.wgroup.mbr");
		}finally{
			
			ContextHelper.handleContext();
		}
		
		return gresult;
	}
	
	public static List<UserExtInfo> findWrokgroupAvailUsers(AccessPoint accesspoint,
			Principal principal, 
			InfoId<Long> wkey,
			String uname)throws CoreException{
		
		List<UserExtInfo> result = null;	
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_ORGHIER_MEMBER)){
			svcctx.setOperationObject(wkey);
			// query accounts information
			result = workgroupservice.getAvailableUsers(svcctx, wkey, uname);

		} catch (ServiceException e) {
			ContextHelper.stampContext(e, "excp.find.aval.users");
			
		}finally{
			
			ContextHelper.handleContext();
		}
		
		return result;
	}
	
	public static PageWrapper<UserExtInfo> findWrokgroupAvailUsers(AccessPoint accesspoint,
			Principal principal, 
			InfoId<Long> wkey,
			String uname, PageQuery pagequery)throws CoreException{
		
		PageWrapper<UserExtInfo> result = null;	
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_ORGHIER_MEMBER)){
			svcctx.setOperationObject(wkey);
			// query accounts information
			result = workgroupservice.getAvailableUsers(svcctx, wkey, uname, pagequery);
			
		} catch (ServiceException e) {
			
			ContextHelper.stampContext(e, "excp.find.wgroup.aval.users");

		}finally{
			
			ContextHelper.handleContext();
		}
		
		return result;
	}
	
	public static boolean newWorkgroupGroup(AccessPoint accesspoint,
			Principal principal, GroupInfo ginfo)throws CoreException{
		
		boolean gresult = false;
		if(StringUtils.isBlank(ginfo.getGroupType())){
			ginfo.setGroupType(GroupUsers.GroupType.WORKGROUP_GRP.name());
		}
		// check the validation of user information
		Set<ValidateMessage> vmsg = ValidateUtils.validate(principal.getLocale(), ginfo);
		if(CollectionUtils.isNotEmpty(vmsg)){ // fail pass validation
			CoreException cexcp = new CoreException(principal.getLocale(), "excp.validate");
			cexcp.addValidateMessages(vmsg);
			throw cexcp;
		}
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.NEW_GROUP)){

			if(!InfoId.isValid(ginfo.getInfoId())){
				
				InfoId<Long> gid = idservice.generateId(IdKey.GROUP, Long.class);
				ginfo.setInfoId(gid);
			}
			
			svcctx.setOperationObject(ginfo.getInfoId());
			svcctx.addOperationPredicates(ginfo);
			// query accounts information
			gresult  = workgroupservice.addWorkgroupGroup(svcctx, ginfo);

		} catch (ServiceException e) {

			ContextHelper.stampContext(e, "excp.add.group");
			
		}finally{
			
			ContextHelper.handleContext();
		}
		
		return gresult;
	}
	
	public static List<GroupInfo> findWorkgroupGroups(AccessPoint accesspoint,
			Principal principal,
			InfoId<Long> wkey, 
			String gname) throws CoreException{
		
		List<GroupInfo> gresult = null;
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_GROUPS)){
			
			// amend the operation information
			svcctx.setOperationObject(wkey);
			
			gresult = workgroupservice.getWorkgroupGroups(svcctx, wkey, gname);
		}catch(ServiceException e){
			ContextHelper.stampContext(e, "excp.find.groups");
		}finally{
			
			ContextHelper.handleContext();
		}
		
		return gresult;
	}
	
	public static Boolean removeWorkgroupGroup(AccessPoint accesspoint,
			Principal principal,
			InfoId<Long> wkey, String group)throws CoreException{
		
		Boolean gresult = false;
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.REMOVE_GROUP)){

			// amend the operation information
			svcctx.setOperationObject(wkey);
			svcctx.addOperationPredicates(new DefaultKeyValue("group",group));
			gresult = workgroupservice.removeWorkgroupGroup(svcctx, wkey, group);

		}catch(ServiceException e){
			
			ContextHelper.stampContext(e, "excp.remove.group");
		}finally{
			ContextHelper.handleContext();
		}
		
		return gresult;
	}
	
	public static Boolean removeWorkgroupGroup(AccessPoint accesspoint,
			Principal principal,
			InfoId<Long> groupid)throws CoreException{
		
		Boolean gresult = false;
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.REMOVE_GROUP)){

			// amend the operation information
			svcctx.setOperationObject(groupid);

			gresult = workgroupservice.removeWorkgroupGroup(svcctx,  groupid);

		}catch(ServiceException e){

			ContextHelper.stampContext(e,"excp.remove.group");
		}finally{
			
			ContextHelper.handleContext();
		}
		
		return gresult;
	}
	
	public static Boolean addWorkgroupGroupMembers(AccessPoint accesspoint,
			Principal principal,
			InfoId<Long> groupid, String ...accounts)throws CoreException{
		
		Boolean gresult = false;
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.ADD_GROUP_USER)){

			// amend the operation information
			svcctx.setOperationObject(groupid);

			gresult = workgroupservice.addWorkgroupGroupMember(svcctx, groupid, accounts);
		}catch(ServiceException e){
			ContextHelper.stampContext(e,"excp.add.group.mbr");
		}finally{
			ContextHelper.handleContext();
		}
		
		return gresult;
	}
	
	public static List<UserInfo> findWorkgroupGroupMembers(AccessPoint accesspoint,
			Principal principal,
			InfoId<Long> groupid)throws CoreException{
		
		List<UserInfo> gresult = null;
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_GROUP_USERS)){

			// amend the operation information
			svcctx.setOperationObject(groupid);

			gresult = workgroupservice.getWorkgroupGroupMembers(svcctx, groupid);

		}catch(ServiceException e){
			ContextHelper.stampContext(e, "excp.find.group.mbrs");
		}finally{
			
			ContextHelper.handleContext();
		}
		
		return gresult;
	}
	
	public static Boolean removeWorkgroupGroupMember(AccessPoint accesspoint,
			Principal principal,
			InfoId<Long> groupid, String account)throws CoreException{
		
		Boolean gresult = false;
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.REMOVE_GROUP_USER)){

			// amend the operation information
			svcctx.setOperationObject(groupid);

			gresult = workgroupservice.removeWorkgroupGroupMember(svcctx, groupid, account);

		}catch(ServiceException e){
			ContextHelper.stampContext(e, "excp.remove.group.mbr");
		}finally{
			ContextHelper.handleContext();
		}
		
		return gresult;
	}
	
	public static List<WorkgroupExtInfo> findLocalWorkgroups(AccessPoint accesspoint,
			Principal principal,
			String wgroupname)throws CoreException{
		
		List<WorkgroupExtInfo> gresult = null;
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_WORKGROUPS)){

			// amend the operation information
			svcctx.addOperationPredicates(new DefaultKeyValue("workgroup_name", wgroupname));
			gresult = workgroupservice.getLocalWorkgroups(svcctx, wgroupname);
		}catch(ServiceException e){
			ContextHelper.stampContext(e, "excp.find.wgroups");
		}finally{
			
			ContextHelper.handleContext();
		}
		
		return gresult;
	}
	
	/**
	 * Find the local workgroup in paging mode
	 **/
	public static PageWrapper<WorkgroupLite> findLocalWorkgroups(AccessPoint accesspoint,
			Principal principal,
			String wgroupname,List<String> tags, PageQuery pagequery)throws CoreException{
		
		PageWrapper<WorkgroupLite> gresult = null;
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_WORKGROUPS)){

			// amend the operation information
			svcctx.addOperationPredicates(new DefaultKeyValue("workgroup_name", wgroupname));
			gresult = workgroupservice.getLocalWorkgroups(svcctx, wgroupname, tags, pagequery);
			
		}catch(ServiceException e){
			ContextHelper.stampContext(e, "excp.find.wgroups");
		}finally{
			ContextHelper.handleContext();
		}
		return gresult;
	}
	
	public static List<WorkgroupExtInfo> findMirrorWorkgroups(AccessPoint accesspoint,
			Principal principal,
			String wgroupname)throws CoreException{
		
		List<WorkgroupExtInfo> gresult = null;
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_WORKGROUPS)){

			// amend the operation information
			svcctx.addOperationPredicates(new DefaultKeyValue("workgroup_name", wgroupname));
			gresult = workgroupservice.getMirrorWorkgroups(svcctx, wgroupname);

		}catch(ServiceException e){
			ContextHelper.stampContext(e, "excp.find.wgroup");
		}finally{
			
			ContextHelper.handleContext();
		}
		
		return gresult;
	}
	
	/**
	 * Find the activity logs of work group
	 * 
	 * @param wid the work group id
	 * @param pquery the page query
	 *  
	 **/
	public static PageWrapper<OperLogInfo> findWorkgroupOperLogs(AccessPoint accesspoint,
																 Principal principal,
																 InfoId<Long> wid, PageQuery pquery)throws CoreException{
		
		PageWrapper<OperLogInfo> gresult = null;
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_OPER_LOGS)){

			// amend the operation information
			svcctx.setOperationObject(wid);
			gresult = actlogservice.getWorkgroupOperLogs(svcctx, wid, pquery);

		}catch(ServiceException e){

			ContextHelper.stampContext(e, "excp.find.actlogs");
		}finally{
			
			ContextHelper.handleContext();
		}
		
		return gresult;
	}
	
	/**
	 * Find the tags attached on the work group
	 * @param wid the work group id 
	 **/
	public static List<TagInfo> findWorkgroupTags(AccessPoint accesspoint,
			Principal principal,
			InfoId<Long> wid )throws CoreException{
		
		List<TagInfo> gresult = null;
		if(!InfoId.isValid(wid)){
			CoreException cexcp = new CoreException(principal.getLocale(), "excp.find.actlogs");
			cexcp.addValidateMessage("wrokgroupid", "mesg.prop.miss");
			throw cexcp;
		}
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_TAGS)){
			
			gresult = tagservice.getTags(svcctx, null, wid);

		}catch(ServiceException e){
			ContextHelper.stampContext(e, "excp.find.actlogs");
		}finally{
			
			ContextHelper.handleContext();
		}
		return gresult;
	}

}
