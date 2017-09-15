package com.gp.config;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.gp.web.WebCoreLauncher;
import com.gp.web.DatabaseMessageSource;
import com.gp.web.PrincipalLocaleResolver;
import com.gp.web.servlet.ImageFilter;
import com.gp.web.servlet.TransferServlet;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + ServiceConfigurer.SERVICE_PRECEDENCE + 20)
@ComponentScan(basePackages = { 
		"com.gp.web.api",
		"com.gp.web.view" })
public class WebMVCConfigurer extends WebMvcConfigurerAdapter {

	/**
	 * The CoreStart listener, it starts the CoreEngine which detect and prepare the CoreInitializer via java serviceloader(SPI).
	 * assembly the initializer to sort the LifecycleHooker with priority. 
	 **/
	@Bean 
	ServletListenerRegistrationBean<ServletContextListener> coreStarterListener(){
		ServletListenerRegistrationBean<ServletContextListener> listenerReg = new ServletListenerRegistrationBean<ServletContextListener>();
		ServletContextListener coreListener = new  ServletContextListener() {

			@Override
			public void contextInitialized(ServletContextEvent sce) {}

			@Override
			public void contextDestroyed(ServletContextEvent sce) {
				WebCoreLauncher.getInstance(WebCoreLauncher.class).engineOff();
			}
		};
		listenerReg.setListener(coreListener);
		return listenerReg;
	}

	@Bean
	public InternalResourceViewResolver viewResolver() {
		
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}
	
	/**
	 * Create locale resolver to extract locale from request.
	 **/
	@Bean
	public LocaleResolver localeResolver() {
		return new PrincipalLocaleResolver();
	}

	/**
	 * Create the message source to inject it into Controller.
	 **/
	@Bean
	public MessageSource messageSource() {

		DatabaseMessageSource source = new DatabaseMessageSource();
		return source;
	}


	/**
	 * Register the image filter 
	 **/
	@Bean
	public FilterRegistrationBean imageFilterFilterBean() {
		
		FilterRegistrationBean registerBean = new FilterRegistrationBean();
		ImageFilter serviceFilter = new ImageFilter();
		registerBean.setName("ImageFilter");
		registerBean.setFilter(serviceFilter);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/img_cache/*");
        
        registerBean.setUrlPatterns(urlPatterns);
        registerBean.setAsyncSupported(true);
        registerBean.setOrder(4);
        return registerBean;

	}
	
	/**
	 * Register the transfer servlet 
	 **/
	@Bean
	public ServletRegistrationBean transferServletBean() {
		
		ServletRegistrationBean registerBean = new ServletRegistrationBean(new TransferServlet());
		registerBean.setName("TransferServlet");
		registerBean.addInitParameter("upload_path", "d:\\");
		registerBean.addUrlMappings("/transfer");
		registerBean.setAsyncSupported(true);
		registerBean.setMultipartConfig(getMultiPartConfig());
		registerBean.setAsyncSupported(true);
		registerBean.setLoadOnStartup(2);
		return registerBean;
	}
	
	/**
	 * Register the avatar servlet 
	 **/
	@Bean
	public ServletRegistrationBean avatarServletBean() {
		
		ServletRegistrationBean registerBean = new ServletRegistrationBean(new TransferServlet());
		registerBean.setName("AvatarServlet");
		registerBean.addUrlMappings("/avatar");
		registerBean.setAsyncSupported(true);
		registerBean.setMultipartConfig(getMultiPartConfig());
		registerBean.setAsyncSupported(true);
		registerBean.setLoadOnStartup(3);
		return registerBean;
	}

    /**
     *  Define the MultipartConfigElement for file transfer servlet
     */
    private MultipartConfigElement getMultiPartConfig() {
        String location = "";
        long maxFileSize = -1;
        long maxRequestSize = -1;
        int fileSizeThreshold = 0;
        return new MultipartConfigElement(
            location,
            maxFileSize,
            maxRequestSize,
            fileSizeThreshold
        );
    }
    
 
}