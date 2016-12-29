package com.gp.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import com.gp.web.CoreStarter;
import com.gp.web.servlet.AvatarServlet;
import com.gp.web.servlet.ImageFilter;
import com.gp.web.servlet.ServiceFilter;
import com.gp.web.servlet.TransferServlet;

import java.io.File;

import javax.servlet.FilterRegistration;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * This is the start point of whole web application initialization.
 * Here load the database and mvc etc. spring setting.
 *
 * @author gary diao
 * @version 0.1 2015-12-10
 **/
public class GPressAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        System.out.println("Initializing Application for " + servletContext.getServerInfo());
         
        String prefix =  servletContext.getRealPath("/");     
        String filepath = "WEB-INF"
        		+ System.getProperty("file.separator")
        		+ "classes"+System.getProperty("file.separator")
        		+ "log4j2.xml";
        
        final LoggerContext loggerCtx = (LoggerContext) LogManager.getContext(false);
        File file = new File(prefix + filepath);
        loggerCtx.setConfigLocation(file.toURI());
        
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(RootConfigurer.class);
        rootContext.register(ServiceConfigurer.class);
        try {
            Class searchconfig = Class.forName("com.gp.config.SearchConfigurer");
            rootContext.register(searchconfig);
        } catch (ClassNotFoundException e) {
            System.out.println("The SearchConfigurer class not found!");
        }
        // since we registered RootConfig instead of passing it to the constructor
        rootContext.refresh(); 

        // Manage the life cycle of the root application context
        servletContext.addListener(new ContextLoaderListener(rootContext));

        // Add context listeners
        servletContext.addListener(new CoreStarter());        
        servletContext.addListener(new RequestContextListener());

        // Add encoding filter
        FilterRegistration.Dynamic encodingFilter = servletContext.addFilter("encodingFilter",  
        	      new CharacterEncodingFilter());
        encodingFilter.setInitParameter("encoding", "UTF-8");
        encodingFilter.setInitParameter("forceEncoding", "true");
        encodingFilter.addMappingForUrlPatterns(null, true, "/*");

        // Add service filter
        FilterRegistration.Dynamic serviceFilter = servletContext.addFilter("serviceFilter",  
         	      new ServiceFilter());
        serviceFilter.addMappingForUrlPatterns(null, true, "/gp_svc/*"); 
        
        // Add security shiro filter
        FilterRegistration.Dynamic shiroFilter = servletContext.addFilter("shiroFilter",  
         	      new DelegatingFilterProxy());
        shiroFilter.setInitParameter("targetFilterLifecycle", "true");
        shiroFilter.addMappingForUrlPatterns(null, true, "/*"); 
         	   
        // Add image cache filter
        FilterRegistration.Dynamic imageFilter = servletContext.addFilter("GroupressImageFilter",  
              	      new ImageFilter());
        imageFilter.addMappingForUrlPatterns(null, true, "/img_cache/*"); 
        
        // Create ApplicationContext
        AnnotationConfigWebApplicationContext webMvcContext = new AnnotationConfigWebApplicationContext();
        webMvcContext.register(WebMVCConfigurer.class);
        webMvcContext.register(WebSocketConfigurer.class);
        /***********************************************************************
         * Add the SpringMVC DispatcherServlet
         ***********************************************************************/
        DispatcherServlet dispatcherServlet = new DispatcherServlet(webMvcContext);
        ServletRegistration.Dynamic servlet = servletContext.addServlet("Groupress", dispatcherServlet);
        servlet.setInitParameter("throwExceptionIfNoHandlerFound", "true");
        servlet.addMapping("*.do");
        servlet.setAsyncSupported(true);
        servlet.setLoadOnStartup(1);
        
        /***********************************************************************
         * Add the servlet mapping manually and make it initialize automatically
         ************************************************************************/
        TransferServlet transferServlet = new TransferServlet();
        ServletRegistration.Dynamic servlet1 = servletContext.addServlet("TransferServlet", transferServlet);
        servlet1.setInitParameter("upload_path", "d:\\");
        servlet1.addMapping("/transfer");
        servlet1.setAsyncSupported(true);
        servlet1.setMultipartConfig( getMultiPartConfig() );
        servlet1.setLoadOnStartup(2);
        
        /**********************************************************************
         * Add avatar image crop servlet handle resize and rotate process
         *
         ***********************************************************************/
        AvatarServlet avatarServlet = new AvatarServlet();
        ServletRegistration.Dynamic servlet2 = servletContext.addServlet("AvatarServlet", avatarServlet);
        servlet2.addMapping("/avatar");
        servlet2.setAsyncSupported(true);
        servlet2.setMultipartConfig( getMultiPartConfig() );
        servlet2.setLoadOnStartup(3);
        
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