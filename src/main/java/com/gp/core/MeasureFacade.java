package com.gp.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gp.audit.AccessPoint;
import com.gp.common.Operations;
import com.gp.common.Principal;
import com.gp.common.ServiceContext;
import com.gp.exception.CoreException;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.dao.info.UserSumInfo;
import com.gp.dao.info.WorkgroupSumInfo;
import com.gp.svc.MeasureService;
import com.gp.svc.PersonalService;
import com.gp.svc.WorkgroupService;

@Component
public class MeasureFacade {

	static Logger LOGGER = LoggerFactory.getLogger(MeasureFacade.class);
	
	private static MeasureService measuresvc;
	
	private static PersonalService personalservice;
	
	private static WorkgroupService workgroupsvc;
	
	@Autowired
	private MeasureFacade(MeasureService measuresvc,
			PersonalService personalservice,
			WorkgroupService workgroupsvc){
		
		MeasureFacade.measuresvc = measuresvc;
		MeasureFacade.personalservice = personalservice;
		MeasureFacade.workgroupsvc = workgroupsvc;
	}
	
	/**
	 * Find the work group latest summary information: docs amount etc.
	 * @param wid the work group id  
	 **/
	public static WorkgroupSumInfo findWorkgroupSummary(AccessPoint accesspoint,
			Principal principal,
			InfoId<Long> wid)throws CoreException{
		
		WorkgroupSumInfo gresult = null;
		
		if(!InfoId.isValid(wid)){
			throw new CoreException(principal.getLocale(), "mesg.prop.miss");
		}
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_MEASURE)){
			
			svcctx.setOperationObject(wid);
			
			gresult = workgroupsvc.getWorkgroupSummary(svcctx, wid);
		}catch (ServiceException e)  {
			ContextHelper.stampContext(e,"excp.find.wgroup.stat");
		}finally{
			ContextHelper.handleContext();
		}
		
		return gresult;
	}
	
	/**
	 * Find the work group latest summary information: docs amount etc.
	 * @param wid the work group id  
	 **/
	public static UserSumInfo findPersonalSummary(AccessPoint accesspoint, 
			Principal principal,
			String account) throws CoreException{
		
		UserSumInfo result = null;
		try (ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_USER_SUM)){
			
			result = personalservice.getUserSummary(svcctx, account);
			
		}catch (ServiceException e) {
			
			ContextHelper.stampContext(e, "excp.find.user.sum");
		}finally{
			
			ContextHelper.handleContext();
		}
		
		return result;
		
	}
}
