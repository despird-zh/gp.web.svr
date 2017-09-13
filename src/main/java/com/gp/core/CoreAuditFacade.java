package com.gp.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.exception.CoreException;
import com.gp.exception.ServiceException;
import com.gp.dao.info.AuditInfo;
import com.gp.info.InfoId;
import com.gp.svc.AuditService;
import com.gp.svc.CommonService;

/**
 * CoreFacade provides methods to handle the core event data
 * <ol>
 *     <li>process the audit event data</li>
 *     <li>process the action log data</li>
 *     <li>process the core event and generate the notification</li>
 * </ol>
 *
 * @author  gary diao
 * @version  0.1 2015-10-10
 *
 **/
@Component
public class CoreAuditFacade {

	static Logger LOGGER = LoggerFactory.getLogger(CoreAuditFacade.class);
	
	private static AuditService auditservice;

	private static CommonService idService;

	@Autowired
	private CoreAuditFacade(AuditService auditservice,
					   CommonService idService){
		
		CoreAuditFacade.auditservice = auditservice;
		CoreAuditFacade.idService = idService;
	}
	
	/**
	 * Audit the verbs generated from operation, here not user AuditServiceConext as for no need to collect audit information.
	 * otherwise it falls into dead loop.
	 * 
	 * @param auditlist the audit data of operation
	 * 
	 **/
	public static InfoId<Long> persistAudit(AuditInfo auditinfo) throws CoreException{
		
		if(null == auditinfo)
			return null;
		
		try {
			// retrieve and set audit id
			InfoId<Long> auditId = idService.generateId( IdKey.GP_AUDITS, Long.class);
			auditinfo.setInfoId(auditId); 

			auditservice.addAudit( auditinfo);
			return auditId;
		} catch (ServiceException e) {
			throw new CoreException("Fail to persist audit log.",e );
		}
	}

}
