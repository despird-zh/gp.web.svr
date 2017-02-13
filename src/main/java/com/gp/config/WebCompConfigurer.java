package com.gp.config;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.SessionRepository;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import com.gp.web.CoreStarter;
import com.gp.web.servlet.ImageFilter;
import com.gp.web.servlet.TransferServlet;

/**
 * Weave the web components: filters, listeners, servlets required in the application.
 * 
 * @author diaogc
 * @version 0.1 2016-12-29
 **/
@Configuration
@EnableRedisHttpSession
public class WebCompConfigurer {
	
	/**
	 * Declare the jedis connection factory 
	 **/
	@Bean
    public JedisConnectionFactory connectionFactory() {
        return new JedisConnectionFactory();
    }

	@SuppressWarnings("rawtypes")
	@Bean
	SessionRepository sessionRepository() {
		
		RedisOperationsSessionRepository redisSessionRepository = new RedisOperationsSessionRepository(
				connectionFactory());
		
		return redisSessionRepository;
    }
	
	/**
	 * The CoreStart listener 
	 **/
	@Bean ServletListenerRegistrationBean<CoreStarter> coreStarterListener(){
		ServletListenerRegistrationBean<CoreStarter> listenerReg = new ServletListenerRegistrationBean<CoreStarter>();
		
		listenerReg.setListener(new CoreStarter());
		return listenerReg;
	}
	
	/**
	 * The Request Context Listener 
	 **/
	@Bean ServletListenerRegistrationBean<RequestContextListener> requestContextListener(){
		ServletListenerRegistrationBean<RequestContextListener> listenerReg = new ServletListenerRegistrationBean<RequestContextListener>();
		
		listenerReg.setListener(new RequestContextListener());
		
		return listenerReg;
	}
	
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
	
	/**
	 * Register the encoding filter bean 
	 **/
	@Bean
	public FilterRegistrationBean encodingFilterBean() {
		FilterRegistrationBean registerBean = new FilterRegistrationBean();
		CharacterEncodingFilter encodeingFilter = new CharacterEncodingFilter();
		registerBean.setName("EncodingFilter");
		registerBean.setFilter(encodeingFilter);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("*.do");
        registerBean.addInitParameter("encoding", "UTF-8");
        registerBean.addInitParameter("forceEncoding", "true");
        registerBean.setUrlPatterns(urlPatterns);
        registerBean.setOrder(1);
        registerBean.setAsyncSupported(true);
        return registerBean;
	}
	
	/*@Bean
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
		bean.setAsyncSupported(true);
		bean.setOrder(2);
		return bean;
	}*/
	
	/**
	 * Register the service filter to validate all the rpc request 
	 *
	@Bean
	public FilterRegistrationBean serviceFilterBean() {
		FilterRegistrationBean registerBean = new FilterRegistrationBean();
		Service1Filter serviceFilter = new Service1Filter();
		registerBean.setName("ServiceFilter");
		registerBean.setFilter(serviceFilter);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/gpapi/*");
        registerBean.setUrlPatterns(urlPatterns);
        registerBean.setOrder(2);
        return registerBean;
	}*/
	
	/**
	 * Register the shiro filter 
	 **
	@Bean
	public FilterRegistrationBean shiroFilterFilterBean() {
		FilterRegistrationBean registerBean = new FilterRegistrationBean();
		registerBean.setName("shiroFilter");
		DelegatingFilterProxy serviceFilter = new DelegatingFilterProxy();
		serviceFilter.setTargetBeanName("shiroFilter");
		registerBean.setFilter(serviceFilter);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");
        
        registerBean.setUrlPatterns(urlPatterns);
        registerBean.addInitParameter("targetFilterLifecycle", "true");
        registerBean.setAsyncSupported(true);
        registerBean.setOrder(3);
        return registerBean;
	}*/
	
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
