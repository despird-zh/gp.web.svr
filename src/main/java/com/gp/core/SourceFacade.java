package com.gp.core;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gp.audit.AccessPoint;
import com.gp.common.IdKey;
import com.gp.common.Operations;
import com.gp.common.Principal;
import com.gp.common.ServiceContext;
import com.gp.common.Sources.State;
import com.gp.exception.CoreException;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.dao.info.SourceInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.svc.SourceService;
import com.gp.validate.ValidateMessage;
import com.gp.validate.ValidateUtils;

@Component
public class SourceFacade {

	static Logger LOGGER = LoggerFactory.getLogger(SourceFacade.class);
	
	private static SourceService instanceservice;
	
	@Autowired
	private SourceFacade(SourceService instanceservice){
		SourceFacade.instanceservice = instanceservice;
	}
	

	/**
	 * Get the local instance information 
	 **/
	public static SourceInfo findSource(AccessPoint accesspoint,
			Principal principal,
			InfoId<Integer> instanceid) throws CoreException{

		SourceInfo rst = null;
		
		if(!InfoId.isValid(instanceid)){
			CoreException cexcp = new CoreException(principal.getLocale(), "excp.find.instance");
			cexcp.addValidateMessage("prop.instanceid", "mesg.prop.miss");
			throw cexcp;
		}
		
		try (ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_SOURCE)){
			
			svcctx.setOperationObject(instanceid);
			
			rst = instanceservice.getSource(svcctx, instanceid);
		} catch (ServiceException e)  {
			ContextHelper.stampContext(e, "excp.find.instance");
		}finally{
			ContextHelper.handleContext();
		}
		return rst;
	}
	
	public static Boolean changeSourceState(AccessPoint accesspoint,
			Principal principal,
			InfoId<Integer> instance, State state)throws CoreException{
		
		Boolean result = false;
		try (ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.UPDATE_SOURCE)){
			
			svcctx.setOperationObject(instance);
			svcctx.addOperationPredicates(new DefaultKeyValue("state", state.name()));
			
			result =  instanceservice.changeSourceState(svcctx, instance, state);

		} catch (ServiceException e)  {
			ContextHelper.stampContext(e, "excp.update.instance");
		}finally{
			
			ContextHelper.handleContext();
		}
		return result;
	}
	
	public static Boolean saveSource(AccessPoint accesspoint,
			Principal principal,
			SourceInfo instance)throws CoreException{
		
		Boolean result = false;
		if(!InfoId.isValid(instance.getInfoId())){
			CoreException cexcp = new CoreException(principal.getLocale(), "excp.save.instance");
			cexcp.addValidateMessage("prop.instanceid", "mesg.prop.miss");
			throw cexcp;
		}
		
		try (ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.UPDATE_SOURCE)){
			
			svcctx.setOperationObject(instance.getInfoId());
			svcctx.addOperationPredicates(instance);
			// check the validation of user information
			Set<ValidateMessage> vmsg = ValidateUtils.validate(principal.getLocale(), instance);
			if(!CollectionUtils.isEmpty(vmsg)){ // fail pass validation
				CoreException cexcp = new CoreException(principal.getLocale(), "excp.validate");
				cexcp.addValidateMessages(vmsg);
				throw cexcp;
			}
			result =  instanceservice.saveSource(svcctx, instance);
		} catch (ServiceException e)  {
			ContextHelper.stampContext(e , "excp.save.instance");
		}finally{
			ContextHelper.handleContext();
		}
		return result;
	}

	public static Boolean saveExtSource(AccessPoint accesspoint,
			Principal principal,
			SourceInfo instance)throws CoreException{
		
		Boolean result = false;
		
		try (ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.NEW_EXT_SOURCE)){
			
			InfoId<Integer> instanceId = CommonFacade.generateId(IdKey.SOURCE, Integer.class);
			
			svcctx.setOperationObject(instanceId);
			svcctx.addOperationPredicates(instance);
			// check the validation of user information
			Set<ValidateMessage> vmsg = ValidateUtils.validate(principal.getLocale(), instance);
			if(null != vmsg && vmsg.size() > 0){ // fail pass validation
				CoreException cexcp = new CoreException(principal.getLocale(), "excp.validate");
				cexcp.addValidateMessages(vmsg);
				throw cexcp;
			}
			instance.setInfoId(instanceId);
			result =  instanceservice.addExtSource(svcctx, instance);
			
		} catch (ServiceException e)  {
			ContextHelper.stampContext(e, "excp.save.instance");
		}finally{
			
			ContextHelper.handleContext();
		}
		return result;
	}
	
	public static List<SourceInfo> findSources(AccessPoint accesspoint,
			Principal principal,
			String instancename)throws CoreException{
		
		List<SourceInfo> result = null;
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_SOURCES)){
			
			String[][] parms = new String[][]{
				{"instancename",instancename}};				
			Map<?,?> parmap = ArrayUtils.toMap(parms);			
			svcctx.addOperationPredicates(parmap);
						
			// query accounts information
			result = instanceservice.getSources(svcctx, instancename);
			
		} catch (Exception e) {
			ContextHelper.stampContext(e,"excp.find.instance");
		}finally{
			ContextHelper.handleContext();
		}
		return result;
	}
	
	
	public static PageWrapper<SourceInfo> findSources(AccessPoint accesspoint,
			Principal principal,
			String instancename, PageQuery pquery)throws CoreException{
		
		PageWrapper<SourceInfo> result = null;
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_SOURCES)){
			
			String[][] parms = new String[][]{
				{"instancename",instancename}};				
			Map<?,?> parmap = ArrayUtils.toMap(parms);			
			svcctx.addOperationPredicates(parmap);
						
			// query accounts information
			result = instanceservice.getSources(svcctx, instancename, pquery);
			
		} catch (Exception e) {
			ContextHelper.stampContext(e,"excp.find.instance");
		}finally{
			
			ContextHelper.handleContext();
		}
		return result;
	}
	
	public static Map<String,SourceInfo> findSources(AccessPoint accesspoint,
			Principal principal,
			List<String> accounts)throws CoreException{
		
		Map<String,SourceInfo> result = null;
		
		if(CollectionUtils.isEmpty(accounts)){
			throw new CoreException(principal.getLocale(),"mesg.account.miss");
		}
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_SOURCES)){
						
			// query accounts information
			result = instanceservice.getAccountSources(svcctx, accounts);
			
		} catch (Exception e) {
			ContextHelper.stampContext(e, "excp.find.instance");
		}finally{
			ContextHelper.handleContext();
		}
		return result;
	}
}
