package com.gp.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.session.ExpiringSession;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import com.gp.web.security.PrincipalsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter{

	
	@Override
 	public void configure(WebSecurity web) throws Exception {
 		web.ignoring()
 		// Spring Security should completely ignore URLs starting with /resources/
 				.antMatchers("/resources/**");
 	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().csrfTokenRepository(csrfTokenRepository());
		
		http.antMatcher("/**")
	      .authorizeRequests()
	      .antMatchers("/", "/login**", "/webjars/**")
	      .permitAll()
	      .anyRequest()
	      .authenticated()
	      ;
		
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(new PrincipalsService())
				.passwordEncoder(new BCryptPasswordEncoder());
	}

	
	@Override
 	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
 		auth
 		// enable in memory based authentication with a user named "user" and "admin"
 		.inMemoryAuthentication().withUser("user").password("password").roles("USER")
 				.and().withUser("admin").password("password").roles("USER", "ADMIN");
 	}
	
	@Bean
	protected CsrfTokenRepository csrfTokenRepository()
	{
	    HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
	    repository.setHeaderName( "X-XSRF-TOKEN" );
	    return repository;
	}
}
