package com.gp.web;

import com.gp.common.Operations;
import com.gp.core.CoreEventLoad;
import com.gp.core.CoreFacade;
import com.gp.core.CoreHooker;
import com.gp.core.OperationFacade;
import com.gp.disruptor.EventPayload;
import com.gp.exception.CoreException;
import com.gp.exception.RingEventException;

public class CoreWebHooker extends CoreHooker{

	@Override
	public void processPayload(EventPayload payload) throws RingEventException{
		// execute the default procession.
		super.processPayload(payload);
		
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
