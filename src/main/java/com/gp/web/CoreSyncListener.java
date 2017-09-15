package com.gp.web;

import java.util.Map;

import com.gp.common.GPrincipal;
import com.gp.common.IdKeys;
import com.gp.common.Operations;
import com.gp.core.CoreEventLoad;
import com.gp.core.SyncFacade;
import com.gp.disruptor.EventListener;
import com.gp.disruptor.EventPayload;
import com.gp.disruptor.EventType;
import com.gp.exception.CoreException;
import com.gp.exception.RingEventException;
import com.gp.info.InfoId;
import com.gp.sync.SyncPayloads;
import com.gp.sync.client.SyncNodeClient;
import com.gp.sync.message.SyncTriggerMessage;

/**
 * This hooker monitor the SYNC {@link EventType}, then push a sync command to sync node
 * 
 *  @author gdiao 
 *  @version 0.1 2016-08-07
 *  
 **/
public class CoreSyncListener extends EventListener<EventPayload> {

	public CoreSyncListener() {
		super(EventType.SYNC);
	}

	@Override
	public void processPayload(EventPayload payload) throws RingEventException {

		CoreEventLoad coreload = (CoreEventLoad) payload;
		
		Operations operation = Operations.valueOf(coreload.getOperation());
		
		GPrincipal princ = new GPrincipal(coreload.getOperator());
		try {
			switch (operation) {
			case UPDATE_ACCOUNT:
				SyncTriggerMessage triggerMsg = convertTriggerMessage(
						operation, coreload.getWorkgroupId(), coreload.getObjectId(),
						coreload.getPredicates(), coreload.getTimestamp());
				
				InfoId<Long> outId = SyncFacade.newSyncMsgOut(princ, triggerMsg);
				triggerMsg.setInfoId(outId);
				SyncNodeClient.getInstance().sendMessage("/gpapi/sync-notify", triggerMsg);
				
				break;
			default:
				
				break;
			}
		} catch (CoreException ce) {

			throw new RingEventException("Fail to handle core event and persist operation log", ce);
		}
	}
	
	/**
	 * Prepare the synchronize push message with data extract out of CoreEventLoad object.
	 * 
	 * @param wId the wrokgroupId
	 * @param infoId the information id
	 * @param predicates the predicates
	 * @param timestamp the time stamp
	 *  
	 **/
	private SyncTriggerMessage convertTriggerMessage(Operations operation, InfoId<?> wId, InfoId<?> infoId, Map<String, Object> predicates, Long timestamp) {
		
		SyncTriggerMessage push = new SyncTriggerMessage();
		String traceCode  = IdKeys.getTraceCode("N0010", infoId);
		
		push.setTraceCode(traceCode);
		push.setType(SyncPayloads.CMD_UPD_SOURCE);
		push.setNode("N0010");
		
		Map<String, Object> payload = SyncPayloads.includePayload(predicates, 
				"email","full_name","phone","mobile","classification", "global_account","account");
		push.setPayload(payload);
		
		return push;
	}
}
