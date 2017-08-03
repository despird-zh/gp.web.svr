package com.gp.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * This is just a place holder, it do nothing. 
 **/
public class AppContextListener implements ApplicationListener<ContextRefreshedEvent> {

	static Logger LOGGER = LoggerFactory.getLogger(AppContextListener.class);
	
	public ApplicationContext referContext;
	
	public AppContextListener() {
		LOGGER.debug("AppContextListener initialized");
	}
	
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
		this.referContext = event.getApplicationContext();
    }
}
