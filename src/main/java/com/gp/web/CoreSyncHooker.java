package com.gp.web;

import com.gp.common.Operations;
import com.gp.core.CoreEventLoad;
import com.gp.core.OperationFacade;
import com.gp.disruptor.EventHooker;
import com.gp.disruptor.EventPayload;
import com.gp.disruptor.EventType;
import com.gp.exception.CoreException;
import com.gp.exception.RingEventException;

/**
 * Here generate the operation log for resource, member, workgroup
 * the trace logs are content of time line.
 * 
 *  @author gdiao 
 *  @version 0.1 2016-08-07
 *  
 **/
public class CoreSyncHooker extends EventHooker<EventPayload> {

	public CoreSyncHooker() {
		super(EventType.SYNC);
	}

	@Override
	public void processPayload(EventPayload payload) throws RingEventException {

		CoreEventLoad coreload = (CoreEventLoad) payload;
		
		Operations operation = Operations.valueOf(coreload.getOperation());
		try {
			switch (operation) {
			case UPDATE_ACCOUNT:
				OperationFacade.handleUpdateAccount(coreload);
				break;
			case UPDATE_SYSOPTION:
				OperationFacade.handleUpdateSysOption(coreload);
				break;
			default:
				System.out.println("----xxxx"+operation);
				break;
			}
		} catch (CoreException ce) {

			throw new RingEventException("Fail to handle core event and persist operation log", ce);
		}
	}
}
