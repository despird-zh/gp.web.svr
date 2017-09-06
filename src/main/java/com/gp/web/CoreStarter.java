package com.gp.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gp.core.CoreEngine;
import com.gp.core.CoreAuditHandler;
import com.gp.disruptor.EventDispatcher;
import com.gp.exception.BaseException;

/**
 * the core starter of application event engine.
 *
 * @author gary diao 
 * @version 0.1 2015-12-12
 * 
 **/
public class CoreStarter implements ServletContextListener{
	
	static Logger LOGGER = LoggerFactory.getLogger(CoreStarter.class);

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		LOGGER.debug("ServletContextListener:CoreStarter destroying");
		try {

			CoreEngine.shutdown();
			LOGGER.debug("CoreEngine shutdown");
			
		} catch (BaseException e) {
			LOGGER.debug("fail to shutdown CoreFacade.",e);
		}
	}
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		LOGGER.debug("ServletContextListener:CoreStarter starting");
		try {
			
			// register the core event hooker with customized one
			//CoreHooker coreHooker = new CoreWebHooker();
			//EventDispatcher.getInstance().regEventHooker(coreHooker);
			CoreAuditHandler coreHandler = new CoreAuditHandler();
			EventDispatcher.getInstance().regEventHandler(coreHandler);
			// initialize the engine
			CoreEngine.initial();
			LOGGER.debug("CoreEngine initialized");
			CoreEngine.startup();
			LOGGER.debug("CoreEngine startup");
		} catch (BaseException e) {
			LOGGER.debug("fail to startup CoreFacade.",e);
		}
	}
	
}
