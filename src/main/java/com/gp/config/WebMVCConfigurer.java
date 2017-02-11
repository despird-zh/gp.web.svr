package com.gp.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.gp.web.DatabaseMessageSource;
import com.gp.web.PrincipalLocaleResolver;
import com.gp.web.view.GenericFileView;
import com.gp.web.view.GenericFileViewResolver;
import com.gp.web.view.MultipleViewResolver;

@EnableWebMvc
@ComponentScan(basePackages = { 
	"com.gp.web"
})
public class WebMVCConfigurer extends WebMvcConfigurerAdapter {

	/**
	 * Create a multiple resolver to handle the multipart/form-data request.
	 **/
	@Bean
	public MultipartResolver multipartResolver() throws IOException {

		CommonsMultipartResolver mpresolver = new CommonsMultipartResolver();

		//mpresolver.setUploadTempDir(new FileSystemResource("/WEB-INF/tmp/spittr/uploads"));  
		mpresolver.setDefaultEncoding("utf-8");
		mpresolver.setMaxUploadSize(10485760000l);
		mpresolver.setMaxInMemorySize(40960);

		return mpresolver;
	}

	/**
	 * Create multiple view resolver bean instance
	 **/
	@Bean MultipleViewResolver custViewResolver() {
		   MultipleViewResolver rtv = new MultipleViewResolver();
		   Map<String, ViewResolver> resolvers = new HashMap<String, ViewResolver>();
		   resolvers.put("config", getConfigResolver());
		   resolvers.put("swf", getConfigResolver());
		   return rtv;
	}
	
	@Bean
	public LocaleResolver localeResolver() {
	    return new PrincipalLocaleResolver();
	}
	
    @Bean
    public MessageSource messageSource() {
    	
        DatabaseMessageSource source = new DatabaseMessageSource();
        return source;
    }
    
	public GenericFileViewResolver getConfigResolver(){
		
		GenericFileViewResolver config = new GenericFileViewResolver();
		config.setViewName("configFileView");
		config.setLocation("/WEB-INF/config/");
		config.setCache(false);
		
		return config;
	}
	
	public GenericFileViewResolver getSWFResolver(){
		
		GenericFileViewResolver config = new GenericFileViewResolver();
		config.setViewName("swfFileView");
		config.setLocation("/WEB-INF/swf/");
		config.setCache(false);
		
		return config;
	}
	
	@Scope("prototype")
	@Bean 
	public GenericFileView swfFileView(){
		GenericFileView fv = new GenericFileView();
		fv.setContentType("application/x-shockwave-flash");
		fv.setUrl("");
		return fv;
	}
	
	@Scope("prototype")
	@Bean 
	public GenericFileView configFileView(){
		GenericFileView fv = new GenericFileView();
		fv.setContentType("text/plain");
		fv.setUrl("");
		return fv;
	}
	
	@Bean
	public InternalResourceViewResolver jspViewResolver(){
		
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		resolver.setOrder(1);
		
		return resolver;
	}
}