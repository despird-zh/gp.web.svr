package com.gp.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

import com.gp.core.AppContextListener;

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
	})
public class GPressApplication extends SpringBootServletInitializer{

	/**
	 * The main entrance of application 
	 **/
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(GPressApplication.class);
		app.addListeners(new AppContextListener());
        app.run( args);
    }

	/**
	 * configure the application 
	 **/
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
      return application.sources(GPressApplication.class);
    }

}
