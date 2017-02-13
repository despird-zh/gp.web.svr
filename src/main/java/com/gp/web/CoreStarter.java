package com.gp.web;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gp.core.CoreEngine;
import com.gp.exception.BaseException;

import redis.embedded.RedisServer;

/**
 * the core starter of application event engine.
 *
 * @author gary diao 
 * @version 0.1 2015-12-12
 * 
 **/
public class CoreStarter implements ServletContextListener{
	
	static Logger LOGGER = LoggerFactory.getLogger(CoreStarter.class);
	RedisServer redisServer;
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		LOGGER.debug("ServletContextListener:CoreStarter destroying");
		try {
			LOGGER.debug("Embedded Redis stoping");
			redisServer.stop();
			CoreEngine.shutdown();
			LOGGER.debug("CoreEngine shutdown");
			
		} catch (BaseException e) {
			LOGGER.debug("fail to shutdown CoreFacade.",e);
		}
	}
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		LOGGER.debug("ContextInitFinishListener:CoreStarter starting");
		try {
			try {
				LOGGER.debug("Embedded Redis starting");
				redisServer = new RedisServer(6379);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			redisServer.start();
			CoreEngine.initial();
			LOGGER.debug("CoreEngine initialized");
			CoreEngine.startup();
			LOGGER.debug("CoreEngine startup");
		} catch (BaseException e) {
			LOGGER.debug("fail to startup CoreFacade.",e);
		}
	}
	
}
