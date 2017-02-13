package com.gp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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
	
	@Bean  
    public SecurityInterceptor securityInterceptor(){
		return new SecurityInterceptor();
	}
	
	@Bean  
    public SecurityDecisionManager accessDecisionManager(){
		return new SecurityDecisionManager();
	}
	
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
		
		http.addFilterBefore(securityInterceptor(), FilterSecurityInterceptor.class)
			.antMatcher("/**")
			.authorizeRequests()
			.antMatchers("/", "/login**", "/webjars/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin()  
	        .loginPage("/login")//指定登录页是”/login”  
	        .permitAll()  
	        .successHandler(logonSuccessHandler())
	        .and()
	        .logout()  
	        .logoutSuccessUrl("/home") //退出登录后的默认网址是”/home”  
	        .permitAll()  
	        .logoutSuccessHandler(logoffSuccessHandler())
	        .invalidateHttpSession(true)  
	        .and()
			.sessionManagement()
			.sessionFixation().migrateSession()
			.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
			.invalidSessionUrl("/invalidSession.html")
			.maximumSessions(2);
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
		auth
			.userDetailsService(new PrincipalsService())
				.passwordEncoder(new BCryptPasswordEncoder());
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
