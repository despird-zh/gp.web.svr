package com.gp.config;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.DispatcherServlet;

import com.gp.web.CoreStarter;
import com.gp.web.servlet.ImageFilter;
import com.gp.web.servlet.ServiceFilter;
import com.gp.web.servlet.TransferServlet;

/**
 * Weave the web components: filters, listeners, servlets required in the application.
 * 
 * @author diaogc
 * @version 0.1 2016-12-29
 **/
@Configuration
public class WebCompConfigurer {
	
	/**
	 * The CoreStart listener 
	 **/
	@Bean ServletListenerRegistrationBean<CoreStarter> coreStarterListener(){
		ServletListenerRegistrationBean<CoreStarter> listenerReg = new ServletListenerRegistrationBean<CoreStarter>();
		
		listenerReg.setListener(new CoreStarter());
		return listenerReg;
	}
	
	/**
	 * The Request Context Listener,
	 * This help to access the request binded to certain thread 
	 * RequestContextHolder.getRequestAttributes() to get request and locale etc.
	 *
	@Bean ServletListenerRegistrationBean<RequestContextListener> requestContextListener(){
		ServletListenerRegistrationBean<RequestContextListener> listenerReg = new ServletListenerRegistrationBean<RequestContextListener>();
		
		listenerReg.setListener(new RequestContextListener());
		RequestContextHolder.getRequestAttributes()
		return listenerReg;
	}*/
	
	/**
	 * The dispatcher servlet to handle all the request. 
	 **/
	@Bean
	public DispatcherServlet dispatcherServlet() {

		 // Create ApplicationContext
        AnnotationConfigWebApplicationContext webMvcContext = new AnnotationConfigWebApplicationContext();
        webMvcContext.register(WebMVCConfigurer.class);

	    DispatcherServlet servlet=new DispatcherServlet(webMvcContext);
 
	    return  servlet;
	}

	/**
	 * Register the dispatch servlet 
	 **/
	@Bean
	public ServletRegistrationBean dispatcherServletRegistration() {
		
	    ServletRegistrationBean registerBean = new ServletRegistrationBean(dispatcherServlet());
	    registerBean.setName("Groupress");
	    registerBean.addInitParameter("throwExceptionIfNoHandlerFound", "true");
	    registerBean.addUrlMappings("*.do");
	    registerBean.setAsyncSupported(true);
	    registerBean.setLoadOnStartup(1);
	    return registerBean;
	}
	
	@Bean
	public FilterRegistrationBean corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(false);
		config.addAllowedOrigin("*");
		config.addAllowedHeader(ServiceFilter.AUTH_TOKEN);
		config.addAllowedHeader("content-type");// required, otherwise the preflight not work
		config.addAllowedMethod("*");
		source.registerCorsConfiguration( ServiceFilter.FILTER_PREFIX + "/**", config);
		
		FilterRegistrationBean bean = new FilterRegistrationBean(new ServiceFilter(source));
		bean.setOrder(2);
		return bean;
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
