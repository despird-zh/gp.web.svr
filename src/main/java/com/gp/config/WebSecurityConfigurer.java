package com.gp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.gp.security.LogoffSuccessHandler;
import com.gp.security.LogonSuccessHandler;
import com.gp.security.MetadataSourceService;
import com.gp.security.PrincipalsService;
import com.gp.security.SecurityDecisionManager;
import com.gp.security.SecurityInterceptor;
import com.gp.web.servlet.ServiceFilter;

@EnableWebSecurity
@ComponentScan(basePackages = { 
		"com.gp.security"
	})
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter{
	
	@Autowired
	MetadataSourceService metadataSourceService;
	
	@Autowired
	private SecurityDecisionManager accessDecisionManager;
	
	@Override
 	public void configure(WebSecurity web) throws Exception {
 		web.ignoring()
 		// Spring Security should completely ignore URLs starting with /resources/
 		.antMatchers("/resources/**");
 	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().csrfTokenRepository(csrfTokenRepository());
		
		http.cors().configurationSource(getCorsConfigureSource());
		
		http//.addFilterBefore(securityInterceptor(), FilterSecurityInterceptor.class)
			.antMatcher("/**")
			.authorizeRequests()
			.antMatchers("/", "/login**", "/webjars/**").permitAll()
			.anyRequest().authenticated()
			.withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
				public <O extends FilterSecurityInterceptor> O postProcess(
						O fsi) {
					fsi.setPublishAuthorizationSuccess(true);
					fsi.setSecurityMetadataSource(metadataSourceService);
					fsi.setAccessDecisionManager(accessDecisionManager);
					return fsi;
				}
			});
		
	}
	
	/**
	 * Declare a logon success handler 
	 **/
	@Bean  
    public LogonSuccessHandler logonSuccessHandler(){  
        return new LogonSuccessHandler();  
    }  
	
	/**
	 * Declare a logoff success handler 
	 **/
	@Bean  
    public LogoffSuccessHandler logoffSuccessHandler(){  
        return new LogoffSuccessHandler();  
    } 
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(new PrincipalsService());
	}
	
	@Bean
	protected CsrfTokenRepository csrfTokenRepository()
	{
	    HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
	    repository.setHeaderName( "X-XSRF-TOKEN" );
	    return repository;
	}
	
	@Bean
	protected CorsConfigurationSource getCorsConfigureSource(){
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(false);
		config.addAllowedOrigin("*");
		config.addAllowedHeader(ServiceFilter.AUTH_TOKEN);
		config.addAllowedHeader("content-type");// required, otherwise the preflight not work
		config.addAllowedMethod("*");
		source.registerCorsConfiguration( ServiceFilter.FILTER_PREFIX + "/**", config);
		return source;
	}
}
