package com.gp.config;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import com.gp.web.CoreStarter;
import com.gp.web.servlet.ImageFilter;
import com.gp.web.servlet.ServiceFilter;
import com.gp.web.servlet.TransferServlet;

@Configuration
public class WebComConfigurer {
	
	@Bean ServletListenerRegistrationBean<CoreStarter> coreStarterListener(){
		ServletListenerRegistrationBean<CoreStarter> listenerReg = new ServletListenerRegistrationBean<CoreStarter>();
		
		listenerReg.setListener(new CoreStarter());
		return listenerReg;
	}
	
	@Bean ServletListenerRegistrationBean<RequestContextListener> requestContextListener(){
		ServletListenerRegistrationBean<RequestContextListener> listenerReg = new ServletListenerRegistrationBean<RequestContextListener>();
		
		listenerReg.setListener(new RequestContextListener());
		
		return listenerReg;
	}
	
	@Bean
	public DispatcherServlet dispatcherServlet() {

		 // Create ApplicationContext
	        AnnotationConfigWebApplicationContext webMvcContext = new AnnotationConfigWebApplicationContext();
	        webMvcContext.register(WebMVCConfigurer.class);
	        webMvcContext.register(WebSocketConfigurer.class);
	        
	    DispatcherServlet servlet=new DispatcherServlet(webMvcContext);
 
	    return  servlet;
	}

	@Bean
	public ServletRegistrationBean dispatcherServletRegistration() {
		
	    ServletRegistrationBean registrationBean = new ServletRegistrationBean(dispatcherServlet());
	    registrationBean.setName("Groupress");
	    registrationBean.addInitParameter("throwExceptionIfNoHandlerFound", "true");
	    registrationBean.addUrlMappings("*.do");
	    registrationBean.setAsyncSupported(true);
	    registrationBean.setLoadOnStartup(1);
	    return registrationBean;
	}
	
	@Bean
	public FilterRegistrationBean encodingFilterBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		CharacterEncodingFilter encodeingFilter = new CharacterEncodingFilter();
		registrationBean.setName("EncodingFilter");
        registrationBean.setFilter(encodeingFilter);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("*.do");
        registrationBean.addInitParameter("encoding", "UTF-8");
        registrationBean.addInitParameter("forceEncoding", "true");
        registrationBean.setUrlPatterns(urlPatterns);
        registrationBean.setOrder(1);
        return registrationBean;
	}
	
	@Bean
	public FilterRegistrationBean serviceFilterBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		ServiceFilter serviceFilter = new ServiceFilter();
		registrationBean.setName("ServiceFilter");
        registrationBean.setFilter(serviceFilter);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/gp_svc/*");
        registrationBean.setUrlPatterns(urlPatterns);
        registrationBean.setOrder(2);
        return registrationBean;
	}
	
	@Bean
	public FilterRegistrationBean shiroFilterFilterBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setName("shiroFilter");
		DelegatingFilterProxy serviceFilter = new DelegatingFilterProxy();
		serviceFilter.setTargetBeanName("shiroFilter");
        registrationBean.setFilter(serviceFilter);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");
        
        registrationBean.setUrlPatterns(urlPatterns);
        registrationBean.addInitParameter("targetFilterLifecycle", "true");
        registrationBean.setOrder(3);
        return registrationBean;
	}
	
	@Bean
	public FilterRegistrationBean imageFilterFilterBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		ImageFilter serviceFilter = new ImageFilter();
		registrationBean.setName("ImageFilter");
        registrationBean.setFilter(serviceFilter);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/img_cache/*");
        
        registrationBean.setUrlPatterns(urlPatterns);
        registrationBean.setOrder(4);
        return registrationBean;

	}
	
	@Bean
	public ServletRegistrationBean transferServletBean() {
		
		ServletRegistrationBean registrationBean = new ServletRegistrationBean(new TransferServlet());
		registrationBean.setName("TransferServlet");
	    registrationBean.addInitParameter("upload_path", "d:\\");
	    registrationBean.addUrlMappings("/transfer");
	    registrationBean.setAsyncSupported(true);
	    registrationBean.setMultipartConfig(getMultiPartConfig());
	    registrationBean.setLoadOnStartup(2);
		return registrationBean;
	}
	
	@Bean
	public ServletRegistrationBean avatarServletBean() {
		
		ServletRegistrationBean registrationBean = new ServletRegistrationBean(new TransferServlet());
		registrationBean.setName("AvatarServlet");
	    registrationBean.addUrlMappings("/avatar");
	    registrationBean.setAsyncSupported(true);
	    registrationBean.setMultipartConfig(getMultiPartConfig());
	    registrationBean.setLoadOnStartup(3);
		return registrationBean;
	}

    // Define the MultipartConfigElement for file transfer servlet
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
