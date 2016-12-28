package com.gp.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = { 
		"com.aq.eib",
		"com.aq.eib.svc.impl",
		"com.aq.eib.dao.impl",
		"com.aq.svc.impl",
		"com.aq.dao.impl",
		"com.aq.util",
		"com.aq.eib.web",
		"com.aq.eib.rest"})
@SpringBootApplication
public class EIBApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
        SpringApplication.run(EIBApplication.class, args);
    }
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(EIBApplication.class);
	}
}
