package com.gp.core;

import java.util.Map;

import com.gp.audit.AuditEventLoad;
import com.gp.audit.AuditVerb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gp.audit.AccessPoint;
import com.gp.audit.AuditConverter;
import com.gp.common.Principal;
import com.gp.common.ServiceContext;
import com.gp.disruptor.EventDispatcher;
import com.gp.info.InfoId;

/**
 * this class extends service context, collect necessary data of one operation.
 * these operation data is stored in a AuditData.
 * 
 * @author gary diao
 * @version 0.1 2014-12-12
 **/
public class CoreServiceContext extends ServiceContext{
	
	static Logger LOGGER = LoggerFactory.getLogger(CoreServiceContext.class);
	
	/**
	 * audit data holder 
	 **/
	private CoreEventLoad coreload = null;

	/**
	 * AuditState
	 **/
	private ExecState execstate = ExecState.UNKNOWN;
	
	/**
	 * constructor with principal 
	 **/
	public CoreServiceContext(Principal principal){
		
		super(principal);
		setAuditable(true);
		coreload = new CoreEventLoad();
	}
	
	/**
	 * constructor with principal and accesspoint 
	 **/
	public CoreServiceContext(Principal principal, AccessPoint accesspoint){
		
		this(principal);
		this.coreload.setAccessPoint(accesspoint);
	}
	
	/**
	 * set access point information
	 **/
	@Override
	public void setAccessPoint(String client, String host, String app, String version){
		
		this.coreload.setAccessPoint(new AccessPoint(client, host, app, version));
	}
	
	@Override
	public void beginOperation(String subject,String operation, InfoId<?> object, Object predicate){

		coreload.setOperator(subject);
		coreload.setOperation(operation);
		coreload.setObjectId(object);

		// convert object to map object so as to save as json
		Map<String,String> predicates = AuditConverter.beanToMap(predicate);		
		coreload.addPredicates(predicates);

	}
	
	@Override
	public void addOperationPredicate(String predicateKey, Object predicate){

		coreload.addPredicate(predicateKey, predicate.toString());
	}
	
	@Override
	public void addOperationPredicates(Object predicate){

		Map<String,String> predicates = AuditConverter.beanToMap(predicate);
		// set operation primary verb predicates
		coreload.addPredicates(predicates);
	}
	
	@Override 
	public void setOperationObject(InfoId<?> objectId){
		
		coreload.setObjectId(objectId);
	}
	
	/**
	 * endOperation will only be called once. 
	 **/
	@Override
	public void endOperation(ExecState state, String message){

		// already be set to other non-success state, don't change it to success.
		if(execstate != ExecState.UNKNOWN && state == ExecState.SUCCESS)
			return ;
		
		execstate = state;
		coreload.setState(state.name());
		coreload.setStarted(false);
		coreload.setMessage(message);

	}

	/**
	 * Set the work group key of current context
	 **/
	public void setWorkgroupId(InfoId<Long> workgroupId){
		
		super.setWorkgroupId(workgroupId);
		coreload.setWorkgroupId(workgroupId);
	}
	
	/**
	 * Get the work group key of current context.
	 **/
	public InfoId<Long> getWorkgroupId(){
		
		return super.getWorkgroupId();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <A> A getOperationData(Class<A> clazz){

		return (A)coreload;
	}
	
	@Override 
	public void handleOperationData(){
		// trigger the audit event with audit load
		if(this.isAuditable()) {
			AuditEventLoad auditload = new AuditEventLoad();
			auditload.setState(coreload.getState());
			auditload.setMessage(coreload.getMessage());
			auditload.setAccessPoint(coreload.getAccessPoint());
			auditload.setSubject(coreload.getOperator());
			auditload.setWorkgroupId(this.getWorkgroupId());

			AuditVerb verb = new AuditVerb();
			verb.setObjectId(coreload.getObjectId());
			verb.setElapsedTime(coreload.getElapsedTime());
			verb.setTimestamp(coreload.getTimestamp());
			verb.setVerb(coreload.getOperation());
			auditload.setAuditVerb(verb);

			EventDispatcher.getInstance().sendPayload(auditload);
		}
		EventDispatcher.getInstance().sendPayload(coreload);
	}
	
	/**
	 * enter this method means no exception occurs, then end operation.
	 * close : end the operation automatically. 
	 **/
	@Override
	public void close(){
			
		endOperation(ExecState.SUCCESS, "operation success.");

	}
}
