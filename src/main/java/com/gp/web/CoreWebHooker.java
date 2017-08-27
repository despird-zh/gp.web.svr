package com.gp.web;

import com.gp.common.Operations;
import com.gp.core.CoreEventLoad;
import com.gp.core.OperationFacade;
import com.gp.disruptor.EventHooker;
import com.gp.disruptor.EventPayload;
import com.gp.disruptor.EventType;
import com.gp.exception.CoreException;
import com.gp.exception.RingEventException;

public class CoreWebHooker extends EventHooker<EventPayload>{

	public CoreWebHooker(EventType eventType) {
		super(eventType);
	}

	@Override
	public void processPayload(EventPayload payload) throws RingEventException{

		CoreEventLoad coreload = (CoreEventLoad) payload;
		Operations operation = Operations.valueOf(coreload.getOperation());
		try {
			switch (operation) {
				case UPDATE_BASIC_SETTING:
					OperationFacade.handleUpdateAccount(coreload);
					break;
				case UPDATE_SYSOPTION:
					OperationFacade.handleUpdateSysOption(coreload);
					break;
				default:
					break;
			}
		}catch(CoreException ce){

			throw new RingEventException("Fail handle core event payload", ce);
		}
	}
}
