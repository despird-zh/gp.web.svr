package com.gp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.gp.core.AppContextHelper;
import com.gp.core.AppContextListener;
import com.gp.sync.client.SyncClientConfigurer;

/**
 *
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + ServiceConfigurer.SERVICE_PRECEDENCE + 10)
@PropertySource("classpath:/gpress-context.properties")
@ImportResource({
		"classpath:/gpress-datasource.xml",
		"classpath:/gpress-context.xml"
	})
@ComponentScan(basePackages = { 
		"com.gp.core"
 })
@Import({
	SyncClientConfigurer.class
})
public class RootConfigurer {
	
	@Autowired(required=true)
	private Environment env;
	
	@Bean
	AppContextListener initListener(){
		
		return new AppContextListener();
	}
	
	@Bean
	@Order(1)
	public AppContextHelper appContextHelper() {
		return new AppContextHelper();
	}
	
    /**
     * Prepare the rest template for http json data requesting 
     **/
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory){
        return new RestTemplate(factory);
    }
    
    /**
     * Prepare the http request factory  
     **/
    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);//ms
        factory.setConnectTimeout(15000);//ms
        return factory;
    }
}
