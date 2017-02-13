package com.gp.config;

import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

import com.gp.common.GeneralConfig;

/**
 * The the start point the Spring-Boot Applicaiton
 * 
 * @author gary diao
 * 
 * @version 0.1 2016-12-20 
 **/
@SpringBootApplication
@Import({ 
	RootConfigurer.class, 
	ServiceConfigurer.class,
	WebCompConfigurer.class,
	WebSecurityConfigurer.class,
	WebSocketConfigurer.class
	})
public class GPressApplication extends SpringBootServletInitializer{

	/**
	 * The main entrance of application 
	 **/
	public static void main(String[] args) {
		initialLogger();
		
        SpringApplication.run(new Class[] { GPressApplication.class}, args);
    }

	/**
	 * configure the application 
	 **/
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
      return application.sources(GPressApplication.class);
    }
	
	/**
	 * Initial the logger setting 
	 **/
	private static void initialLogger(){
	
		try{
			InputStream log4jIS = GeneralConfig.class.getClassLoader().getResourceAsStream("log4j2.xml");
	        final LoggerContext loggerCtx = (LoggerContext) LogManager.getContext(false);
	        ConfigurationSource cs = new ConfigurationSource(log4jIS);
	        XmlConfiguration config = new XmlConfiguration(loggerCtx, cs);
	        loggerCtx.updateLoggers(config);
		}catch(Exception ce){
			//
			ce.printStackTrace();
		}
	}

}
