/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * Copyright (C) 2006-2011, QOS.ch. All rights reserved.
 * 
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *  
 *   or (per the licensee's choosing)
 *  
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */
package com.gp.audit;

import java.util.Map;

import com.gp.common.GeneralContext.ExecState;
import com.gp.disruptor.EventPayload;
import com.gp.disruptor.EventType;
import com.gp.info.InfoId;

/**
 * AuditInfo hold the audit data 
 * One object is the operation data. operation is business level definition
 * it holds many verbs.
 * A verb is action on table, eg. CRUD etc.
 * 
 * @author despird
 * @version 0.1 2014-3-2
 **/
public class AuditEventLoad implements EventPayload{
	
	private EventType eventType;
	
	/** workgroup id */
	private InfoId<Long> workgroupId = null;
	
	/** the subject - principal or user account */
	private String subject = null;

	/** primary audit verb */
	private AuditVerb auditVerb = null;

	/** access point */
	private AccessPoint accessPoint;

	/** the execute message */
	private String message = null;
	
	/** the execute flag */
	private String state = ExecState.SUCCESS.toString();
	
	public AuditEventLoad(){
		
		this.eventType = EventType.AUDIT;
	}	

	@Override
	public EventType getEventType() {
		
		return eventType;
	}	
	
	/**
	 * Constructor with operation 
	 **/
	public AuditEventLoad(String subject, String verb, InfoId<?> object) {
		this.subject = subject;
		this.auditVerb = new AuditVerb(verb, object);
	}

	public void addPredicates(Map<String, String> predicatemap){
		this.auditVerb.addPredicates(predicatemap);
	}
	
	public void endAuditVerb(String state, String message){
		this.state = state;
		this.message = message;

	}

	/**
	 * Get subject 
	 **/
	public String getSubject() {
		return subject;
	}

	/**
	 * Set subject - principal 
	 **/
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Get Access Point 
	 **/
	public AccessPoint getAccessPoint() {
		return accessPoint;
	}

	/**
	 * Set Access Point 
	 **/
	public void setAccessPoint(AccessPoint accessPoint) {
		this.accessPoint = accessPoint;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String execMsg) {
		this.message = execMsg;
	}

	public String getState() {
		return state;
	}

	public void setState(String execFlag) {
		this.state = execFlag;
	}

	public AuditVerb getAuditVerb(){
		
		return this.auditVerb;
	}
	
	public void setAuditVerb(AuditVerb operationVerb){
		
		this.auditVerb = operationVerb;
	}

	public InfoId<Long> getWorkgroupId() {
		return workgroupId;
	}

	public void setWorkgroupId(InfoId<Long> workgroupId) {
		this.workgroupId = workgroupId;
	}

}
