/*
 * Licensed to the G.Obal under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  G.Obal licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */
package com.gp.audit;

import java.io.IOException;
import java.util.Date;
import org.apache.commons.lang.mutable.MutableObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gp.core.CoreFacade;
import com.gp.disruptor.EventHooker;
import com.gp.disruptor.EventPayload;
import com.gp.disruptor.EventType;
import com.gp.exception.CoreException;
import com.gp.exception.RingEventException;
import com.gp.dao.info.AuditInfo;

/**
 * Hooker class digest all the audit event and persist it to storage(local, remote)
 *  
 * @author gary diao
 * @version 0.1 2015-12-12
 **/
public class AuditHooker extends EventHooker<AuditEventLoad>{

	static Logger LOGGER = LoggerFactory.getLogger(AuditHooker.class);
	
	/**
	 * Default constructor, set event type :AUDIT.
	 **/
	public AuditHooker() {
		super(EventType.AUDIT);
	}
	
	@Override
	public void processPayload(EventPayload payload) throws RingEventException {
		// if configuration setting is local
		persistLocal(payload);
		// else configuration setting is remote
		persistRemote(payload);
	}
	
	/**
	 * persist the audit data remotely to other host 
	 **/
	private void persistRemote(EventPayload payload) throws RingEventException {
		
	}
	
	/**
	 * Persist the audit data locally to database directly. 
	 **/
	private void persistLocal(EventPayload payload) throws RingEventException {
		AuditEventLoad auditdata = (AuditEventLoad)payload;
		// prepare access point
		AccessPoint apt = auditdata.getAccessPoint();

		// prepare the operation primary audit
		AuditInfo operaudit = new AuditInfo();	
		operaudit.setSubject(auditdata.getSubject());
		AuditVerb pverb = auditdata.getAuditVerb();
		if(null != pverb.getObjectId())
			operaudit.setTarget(pverb.getObjectId().toString());
		
		operaudit.setOperation(pverb.getVerb());
		MutableObject pjson;
		try {
			pjson = AuditConverter.mapToJson(pverb.getPredicates());
			operaudit.setPredicates((String)pjson.getValue());
		} catch (IOException e) {
			LOGGER.error("error to convert predicate map");
		}
		
		operaudit.setApp(apt.getApp());
		operaudit.setClient(apt.getClient());
		operaudit.setHost(apt.getHost());
		operaudit.setVersion(apt.getVersion());
		
		if(auditdata.getWorkgroupId()!= null)
			operaudit.setWorkgroupId(auditdata.getWorkgroupId().getId());
		
		operaudit.setState(auditdata.getState());
		operaudit.setMessage(auditdata.getMessage());
		operaudit.setAuditDate(new Date(pverb.getTimestamp()));
		operaudit.setElapseTime(pverb.getElapsedTime());
		
		try {
			// store data to database.
			CoreFacade.auditOperation(operaudit);
		} catch (CoreException e) {
			
			LOGGER.error("Fail to persist audit to database.",e);
			throw new RingEventException("Fail to persist audit to database.",e);
		}
	}
	
}