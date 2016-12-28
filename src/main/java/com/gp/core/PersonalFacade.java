package com.gp.core;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gp.validate.ValidateMessage;
import com.gp.validate.ValidateUtils;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gp.audit.AccessPoint;
import com.gp.common.FlatColumns;
import com.gp.common.IdKey;
import com.gp.common.Operations;
import com.gp.common.Principal;
import com.gp.common.ServiceContext;
import com.gp.exception.CoreException;
import com.gp.exception.ServiceException;
import com.gp.info.CombineInfo;
import com.gp.info.InfoId;
import com.gp.dao.info.NotificationInfo;
import com.gp.dao.info.ChatMessageInfo;
import com.gp.dao.info.OrgHierInfo;
import com.gp.dao.info.TaskInfo;
import com.gp.dao.info.UserInfo;
import com.gp.dao.info.WorkgroupInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.svc.PersonalService;
import com.gp.svc.SecurityService;
import com.gp.svc.TaskService;

@Component
public class PersonalFacade {

	private static TaskService taskservice;
	
	private static SecurityService securityservice;
	
	private static PersonalService personalservice;
	
	@Autowired
	public PersonalFacade(SecurityService securityservice, 
			TaskService taskservice,
			PersonalService personalservice){
		
		PersonalFacade.taskservice = taskservice;
		PersonalFacade.securityservice = securityservice;
		PersonalFacade.personalservice = personalservice;
	}
	
	/**
	 * find the messages sent to account
	 * 
	 * @param account the account to receive the messages
	 * 
	 **/
	public PageWrapper<ChatMessageInfo> findMessages(ServiceContext svcctx, String account, PageQuery pquery){
		return null;
	}
	
	/**
	 * find the notifications sent to account
	 * 
	 * @param account the account to receive the notifications
	 * 
	 **/
	public PageWrapper<NotificationInfo> findNotifications(ServiceContext svcctx, String account, PageQuery pquery){
		return null;
		
	}
	
	/**
	 * find the tasks sent to account
	 * 
	 * @param account the account to receive the tasks
	 * 
	 **/
	public PageWrapper<TaskInfo> findTasks(ServiceContext svcctx, String account, PageQuery pquery){
		return null;
		
	}
	
	/**
	 * find the account basic setting
	 * @param userId the user id 
	 **/
	public static UserInfo findAccountBasic(AccessPoint accesspoint, 
			Principal principal,
			InfoId<Long> userId) throws CoreException{
		
		UserInfo uinfo = null;
		try (ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_ACCOUNT)){
			
			svcctx.setOperationObject(userId);
			
			uinfo = securityservice.getAccountLite(svcctx, userId, null, null);
			
		} catch (ServiceException e) {
			
			ContextHelper.stampContext(e, "excp.find.account");
		}finally{
			
			ContextHelper.handleContext();
		}
		return uinfo;
	}
	
	/**
	 * find all the workgroups that the user joins
	 * 
	 * @param account the account of user
	 **/
	public static List<CombineInfo<WorkgroupInfo, Boolean>> findAccountWorkgroups(AccessPoint accesspoint, 
			Principal principal,
			String account)throws CoreException{
		
		List<CombineInfo<WorkgroupInfo, Boolean>> result = null;
		 
		try (ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_WORKGROUPS)){
		
			result = personalservice.getWorkgroups(svcctx, account);
		
		}catch (ServiceException e) {
			
			ContextHelper.stampContext(e, "excp.find.belongs");
		}finally{
			
			ContextHelper.handleContext();
		}
		
		return result;
	}
	
	/**
	 * find all the organization hierarchy that account join
	 * 
	 * @param account the account information 
	 **/
	public static List<CombineInfo<OrgHierInfo, Boolean>> findUserOrgHierNodes(AccessPoint accesspoint, 
			Principal principal,
			String account)throws CoreException{
		
		List<CombineInfo<OrgHierInfo, Boolean>> result = null;
		
		try (ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_ORGHIERS)){
			
			result = personalservice.getOrgHierNodes(svcctx, account);

		}catch (ServiceException e) {
			
			ContextHelper.stampContext(e, "excp.find.belongs");
		}finally{
			
			ContextHelper.handleContext();
		}
		
		return result;
	}

	/**
	 * Save the account basic setting
	 *
	 * @param uinfo the user information object
	 * @param imagePath the avatar image path
	 **/
	public static boolean saveBasicSetting(AccessPoint accesspoint, 
			Principal principal,
			UserInfo uinfo, String imagePath)throws CoreException{
		
		boolean result = false;
		// validate the basic setting properties and value
		Set<ValidateMessage> vmsg = ValidateUtils.validateProperty(
				principal.getLocale(),
				uinfo,
				"state","type","phone","mobile","email","fullName");
		// prepare the core exception and throw it out
		if(!CollectionUtils.isEmpty(vmsg)){ // fail pass validation
			CoreException coreexcp = new CoreException(principal.getLocale(), "excp.validate");
			coreexcp.addValidateMessages(vmsg);
			throw coreexcp;
		}

		try (ServiceContext svcctx = ContextHelper.beginServiceContext(principal,
				accesspoint,
				Operations.UPDATE_BASIC_SETTING)){

			svcctx.setOperationObject(uinfo.getInfoId());

			result = personalservice.updateBasicSetting(svcctx, uinfo, imagePath);

		}catch (ServiceException e) {
			
			ContextHelper.stampContext(e, "excp.update.account");
		}finally{
			
			ContextHelper.handleContext();
		}
		return result;
	}
	
	/**
	 * Save accounts belong setting
	 *
	 * @param setting setting map key is the blonging id, value is the setting of belonging
	 *
	 **/
	public static boolean saveBelongSetting(AccessPoint accesspoint, 
			Principal principal, Map<InfoId<Long>, Boolean> settings)throws CoreException{
		boolean result = false;
		try (ServiceContext svcctx = ContextHelper.beginServiceContext(principal,
				accesspoint,
				Operations.UPDATE_BASIC_SETTING)){

			svcctx.setOperationObject(principal.getUserId());

			svcctx.addOperationPredicates(settings);
			result = personalservice.updateBelongSetting(svcctx, principal.getAccount(), settings);
			
		}catch (ServiceException e) {
			
			ContextHelper.stampContext(e, "excp.save.belong.setting");
		}finally{
			
			ContextHelper.handleContext();
		}
		return result;
		
	}

	/**
	 * Save the personal's storage setting
	 *
	 * @param storageId the id of storage
	 * @param publishcap the capacity of publish cabinet
	 * @param netdiskcap the capacity of netdisk cabinet
	 **/
	public static boolean saveStorageSetting(AccessPoint accesspoint, 
			Principal principal, 
			Long storageId, 
			Long publishcap,
			Long netdiskcap)throws CoreException{
		boolean result = false;
		try (ServiceContext svcctx = ContextHelper.beginServiceContext(principal,
				accesspoint,
				Operations.UPDATE_STORAGE_SETTING)){

			svcctx.setOperationObject(principal.getUserId());

			svcctx.addOperationPredicate("id",storageId);
			svcctx.addOperationPredicate("publish_cab",publishcap);
			svcctx.addOperationPredicate("netdisk_cab",netdiskcap);
			
			result = personalservice.updateStorageSetting(svcctx, principal.getUserId(), storageId, publishcap, netdiskcap);
			
		}catch (ServiceException e) {
			
			ContextHelper.stampContext(e,"excp.save.storage.setting");
		}finally{
			
			ContextHelper.handleContext();
		}
		return result;
		
	}

	/**
	 * Save the personal's Region setting, this method use principal to locate the user
	 *
	 * @param timezone the time zone
	 * @param language the language setting of user
	 **/
	public static boolean saveRegionSetting(AccessPoint accesspoint, 
			Principal principal, String timezone, String language)throws CoreException{
		boolean result = false;
		try (ServiceContext svcctx = ContextHelper.beginServiceContext(principal,
				accesspoint,
				Operations.UPDATE_REGION_SETTING)){

			svcctx.setOperationObject(principal.getUserId());

			svcctx.addOperationPredicate("timezone",timezone);
			svcctx.addOperationPredicate("language",language);
			
			result = personalservice.updateRegionSetting(svcctx, principal.getUserId(), timezone, language);
			
		}catch (ServiceException e) {
			
			ContextHelper.stampContext(e, "excp.save.region.setting");
		}finally{
			
			ContextHelper.handleContext();
		}
		return result;
		
	}
}
