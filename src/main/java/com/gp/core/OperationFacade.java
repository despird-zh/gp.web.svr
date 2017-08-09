package com.gp.core;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.common.ServiceContext;
import com.gp.dao.info.OperationInfo;
import com.gp.exception.CoreException;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.svc.AuditService;
import com.gp.svc.CommonService;
import com.gp.svc.OperationService;

@Component
public class OperationFacade {
	
	static Logger LOGGER = LoggerFactory.getLogger(CoreFacade.class);
	
	private static AuditService auditservice;

	private static CommonService idService;

	private static OperationService operlogservice;

	@Autowired
	private OperationFacade(AuditService auditservice,
					   OperationService operlogservice,
					   CommonService idService){
		
		OperationFacade.auditservice = auditservice;
		OperationFacade.idService = idService;
		OperationFacade.operlogservice = operlogservice;
	}
	

	/**
	 * Handle the core event payload
	 * @param  coreload the payload of event
	 **/
	public static void handleUpdateAccount(CoreEventLoad coreload)throws CoreException{

		try {
			ServiceContext svcctx = ServiceContext.getPseudoServiceContext();
			OperationInfo operinfo = new OperationInfo();
			InfoId<Long> operid = idService.generateId(IdKey.OPERATION, Long.class);
			operinfo.setInfoId(operid);
			operinfo.setSubject(coreload.getOperator());
			operinfo.setOperation(coreload.getOperation());
			
			if(InfoId.isValid(coreload.getObjectId()))
				operinfo.setObject(coreload.getObjectId().toString());
			
			operinfo.setOperationTime(new Date(coreload.getTimestamp()));
			
			svcctx.setTraceInfo(operinfo);
			operlogservice.addOperation(svcctx, operinfo);

		}catch (ServiceException e) {
			throw new CoreException("fail to handle the core event payload",e );
		}
	}


	public static void handleUpdateSysOption(CoreEventLoad coreload) throws CoreException{
		try {
			ServiceContext svcctx = ServiceContext.getPseudoServiceContext();
			OperationInfo operinfo = new OperationInfo();
			InfoId<Long> operid = idService.generateId(IdKey.OPERATION, Long.class);
			operinfo.setInfoId(operid);
			operinfo.setSubject(coreload.getOperator());
			operinfo.setOperation(coreload.getOperation());
			
			if(InfoId.isValid(coreload.getObjectId()))
				operinfo.setObject(coreload.getObjectId().toString());
			
			operinfo.setOperationTime(new Date(coreload.getTimestamp()));
			operinfo.setAuditId(coreload.getAutidId().getId());
			svcctx.setTraceInfo(operinfo);
			operlogservice.addOperation(svcctx, operinfo);

		}catch (ServiceException e) {
			throw new CoreException("fail to handle the core event payload",e );
		}
	}
}
