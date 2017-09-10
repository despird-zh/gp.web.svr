package com.gp.web;

import com.gp.disruptor.EventDispatcher;
import com.gp.disruptor.EventType;
import com.gp.exception.BaseException;
import com.gp.launcher.CoreInitializer;
import com.gp.launcher.LifecycleHooker;

public class WebCoreInitializer extends CoreInitializer{

	public WebCoreInitializer() throws BaseException {
		super();
	}

	@Override
	public LifecycleHooker setupLifecycleHooker() throws BaseException {
		return new LifecycleHooker("WebCoreLifecycleHooker", 10){

			@Override
			public void initial() {
				
				CoreOperationHooker operHooker = new CoreOperationHooker(EventType.CORE);
				EventDispatcher.getInstance().regEventHooker( operHooker );
				
				sendFeedback(false, "WebCoreLifecycleHooker initial done");
				
			}

			@Override
			public void startup() { }

			@Override
			public void shutdown() {	}

		};
	}

}
