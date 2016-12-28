package com.gp.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.dao.info.OrgHierInfo;
import com.gp.dao.info.UserInfo;
import com.gp.svc.CommonService;
import com.gp.svc.OrgHierService;
import com.gp.validate.ValidateMessage;
import com.gp.validate.ValidateUtils;

@Component
public class OrgHierFacade {

	static Logger LOGGER = LoggerFactory.getLogger(OrgHierFacade.class);
	
	static OrgHierService orghierservice;
	
	static CommonService commonservice;
	
	@Autowired
	private OrgHierFacade(OrgHierService orghierservice,
			CommonService commonservice){
		OrgHierFacade.orghierservice = orghierservice;
		OrgHierFacade.commonservice = commonservice;
	}
	
	/**
	 * Find organization hierarchy nodes under parent node
	 * 
	 * @param accesspoint the access point
	 * @param principal the principal
	 * @param orgNodeId the organization node id 
	 **/
	public static List<OrgHierInfo> findChildOrgHiers(AccessPoint accesspoint,
			Principal principal, InfoId<Long> orgNodeId) throws CoreException{
		
		List<OrgHierInfo> gresult = null;
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_ORGHIERS)){

			gresult = orghierservice.getOrgHierNodes(svcctx, orgNodeId);
	
		} catch (Exception e) {

			ContextHelper.stampContext(e, "excp.find.orghider");

		}finally{
			
			ContextHelper.handleContext();
		}
		return gresult;
	}
	
	public static Map<Long, Integer> findOrgHierGrandNodeCount(AccessPoint accesspoint,
			Principal principal, InfoId<Long> orgNodeId) throws CoreException{
		
		Map<Long, Integer> gresult = null;
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_ORGHIERS)){

			gresult = orghierservice.getOrgHierGrandNodeCount(svcctx, orgNodeId);
	
		} catch (Exception e) {

			ContextHelper.stampContext(e, "excp.find.orghider");

		}finally{
			
			ContextHelper.handleContext();
		}
		return gresult;
	}
	
	public static boolean newOrgHier(AccessPoint accesspoint,
			Principal principal, OrgHierInfo orghier) throws CoreException{
		
		boolean result = false;
		
		if(!InfoId.isValid(orghier.getInfoId())){
			InfoId<Long> id = CommonFacade.generateId(IdKey.ORG_HIER, Long.class);
			orghier.setInfoId(id);
		}
		// check the validation of user information
		Set<ValidateMessage> vmsg = ValidateUtils.validate(principal.getLocale(), orghier);
		if(!CollectionUtils.isEmpty(vmsg)){ // fail pass validation
			CoreException cexcp = new CoreException(principal.getLocale(), "excp.validate");
			cexcp.addValidateMessages(vmsg);
			throw cexcp;
		}
		
		try (ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.NEW_ORGHIER)){
			
			svcctx.setOperationObject(orghier.getInfoId());
			svcctx.addOperationPredicates(orghier);

			result = orghierservice.newOrgHierNode(svcctx, orghier);
	
		} catch (ServiceException e)  {
			ContextHelper.stampContext(e, "excp.save.orghier");
		}finally{
			
			ContextHelper.handleContext();
		}
		return result;
		
	}
	
	public static Boolean saveOrgHier(AccessPoint accesspoint,
			Principal principal, OrgHierInfo orghier) throws CoreException{
		
		boolean result = false;
		
		if(!InfoId.isValid(orghier.getInfoId())){

			CoreException cexcp = new CoreException(principal.getLocale(), "excp.save.orghier");
			cexcp.addValidateMessage("orghierid", "mesg.prop.miss");
			throw cexcp;
		}
		// check the validation of user information
		Set<ValidateMessage> vmsg = ValidateUtils.validate(principal.getLocale(), orghier);
		if(CollectionUtils.isNotEmpty(vmsg)){ // fail pass validation
			CoreException cexcp = new CoreException(principal.getLocale(), "excp.validate");
			cexcp.addValidateMessages(vmsg);
			throw cexcp;
		}		
		try (ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.UPDATE_ORGHIER)){
			
			svcctx.setOperationObject(orghier.getInfoId());
			svcctx.addOperationPredicates(orghier);

			result =  orghierservice.saveOrgHierNode(svcctx, orghier);
			
		} catch (ServiceException e)  {
			ContextHelper.stampContext(e, "excp.save.orghier");
		}finally{
			
			ContextHelper.handleContext();
		}
		return result;
		
	}
	
	public static OrgHierInfo findOrgHier(AccessPoint accesspoint,
			Principal principal, InfoId<Long> orgid) throws CoreException{
		
		OrgHierInfo rst = null;
		
		if(!InfoId.isValid(orgid)){
			CoreException cexcp = new CoreException(principal.getLocale(), "excp.find.orghier");
			cexcp.addValidateMessage("orghierid", "mesg.prop.miss");
		}
		
		try (ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_ORGHIER)){
			
			svcctx.setOperationObject(orgid);
			rst = orghierservice.getOrgHierNode(svcctx, orgid);
		} catch (ServiceException e)  {
			ContextHelper.stampContext(e, "excp.find.orghier");
		}finally{
			
			ContextHelper.handleContext();
		}
		return rst;
	}
	
	public static Boolean addOrgHierMember(AccessPoint accesspoint,
			Principal principal, InfoId<Long> orgid, String ...accounts)throws CoreException{

		Boolean rst = false;
		
		if(!InfoId.isValid(orgid)){
			CoreException cexcp = new CoreException(principal.getLocale(), "excp.add.org.mbr");
			cexcp.addValidateMessage("orghierid", "mesg.prop.miss");
		}
		if(ArrayUtils.isEmpty(accounts)){
			CoreException cexcp = new CoreException(principal.getLocale(), "excp.add.org.mbr");
			cexcp.addValidateMessage("account", "mesg.prop.miss");
		}
		
		try (ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.ADD_ORGHIER_MEMBER)){
			
			svcctx.setOperationObject(orgid);
			
			orghierservice.addOrgHierMember(svcctx, orgid, accounts);
			rst = true;
		} catch (ServiceException e)  {
			ContextHelper.stampContext(e, "excp.add.org.mbr");
		}finally{
			
			ContextHelper.handleContext();
		}
		return rst;
		
	}
	
	public static Boolean removeOrgHierMember(AccessPoint accesspoint,
			Principal principal, InfoId<Long> orgid, String ...accounts)throws CoreException{

		Boolean rst = false;
		
		if(!InfoId.isValid(orgid)){
			CoreException cexcp = new CoreException(principal.getLocale(), "excp.remove.org.mbr");
			cexcp.addValidateMessage("orghierid", "mesg.prop.miss");
		}
		if(ArrayUtils.isEmpty(accounts)){
			CoreException cexcp = new CoreException(principal.getLocale(), "excp.remove.org.mbr");
			cexcp.addValidateMessage("account", "mesg.prop.miss");
		}
		
		try (ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.REMOVE_ORGHIER_MEMBER)){
			
			svcctx.setOperationObject(orgid);
			Map<String, Object> vals = commonservice.query(orgid, new FlatColLocator[]{
				FlatColumns.ADMIN, FlatColumns.MANAGER
			});
			String admin = (String)vals.get(FlatColumns.ADMIN.getColumn());
			String manager = (String)vals.get(FlatColumns.MANAGER.getColumn());
			
			for(String account : accounts){
				
				if(StringUtils.equals(admin, account) || StringUtils.equals(manager, account)){
					throw new CoreException(principal.getLocale(), "excp.reserv.org.mbr");
				}
			}
			orghierservice.removeOrgHierMember(svcctx, orgid, accounts);
			rst = true;
		} catch (ServiceException e)  {
			ContextHelper.stampContext(e, "excp.remove.org.mbr");
		}finally{
			
			ContextHelper.handleContext();
		}
		return rst;
		
	}
	
	public static List<UserInfo> findOrgHierMembers(AccessPoint accesspoint,
			Principal principal, 
			InfoId<Long> orgid)throws CoreException{
		
		List<UserInfo> result = null;	
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_ORGHIER_MEMBER)){
			svcctx.setOperationObject(orgid);
			// query accounts information
			result = orghierservice.getOrgHierMembers(svcctx, orgid);

		} catch (ServiceException e) {
			ContextHelper.stampContext(e, "excp.find.org.mbr");
		}finally{
			
			ContextHelper.handleContext();
		}
		
		return result;
	}
	
	public static List<OrgHierInfo> findRouteOrgHiers(AccessPoint accesspoint,
			Principal principal, 
			InfoId<Long> orgid)throws CoreException{
		
		List<OrgHierInfo> result = null;
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_ORGHIERS)){
			
			String routeIdStr = orghierservice.getOrgHierRoute(svcctx, orgid);
			String[] oids = StringUtils.split(routeIdStr, '-');
			List<InfoId<Long>> ids = new ArrayList<InfoId<Long>>();
			
			for(String idstr : oids){
				Long id = NumberUtils.toLong(idstr);
				if(id > 0){
					ids.add(IdKey.ORG_HIER.getInfoId(id));
				}
			}
			
			result = orghierservice.getOrgHierNodes(svcctx, ids.toArray(new InfoId<?>[0]));
			
		} catch (ServiceException e) {
			ContextHelper.stampContext(e, "excp.find.orghier");
		}finally{
			
			ContextHelper.handleContext();
		}
		
		return result;
	}
}
