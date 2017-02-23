package com.gp.config;

import java.io.IOException;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.gp.web.DatabaseMessageSource;
import com.gp.web.PrincipalLocaleResolver;

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
	 * Create multiple view resolver bean instances 
	 * to support multiple view rendering.
	 * 
	@Bean 
	MultipleViewResolver custViewResolver() {
		MultipleViewResolver rtv = new MultipleViewResolver();
		Map<String, ViewResolver> resolvers = new HashMap<String, ViewResolver>();
		
		GenericFileViewResolver config = new GenericFileViewResolver();
		config.setViewName("configFileView");
		config.setLocation("/WEB-INF/config/");
		config.setCache(false);
		
		resolvers.put("config", config);
	   
		GenericFileViewResolver swf = new GenericFileViewResolver();
		swf.setViewName("swfFileView");
		swf.setLocation("/WEB-INF/swf/");
		swf.setCache(false);
		
		resolvers.put("swf", swf);
	   
		rtv.setResolvers(resolvers);
		return rtv;
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
	}*/
	
    /**
     * Define the internal view resolver 
     **/
	@Bean
	public InternalResourceViewResolver jspViewResolver(){
		
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		resolver.setOrder(1);
		
		return resolver;
	}
}