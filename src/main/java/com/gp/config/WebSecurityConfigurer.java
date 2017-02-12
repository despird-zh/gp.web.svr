package com.gp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.session.ExpiringSession;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

@Configuration
@EnableOAuth2Sso
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter{

	@Autowired
    FindByIndexNameSessionRepository<ExpiringSession> sessionRepository;
	
	@Override
 	public void configure(WebSecurity web) throws Exception {
 		web.ignoring()
 		// Spring Security should completely ignore URLs starting with /resources/
 				.antMatchers("/resources/**");
 	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/**")
	      .authorizeRequests()
	      .antMatchers("/", "/login**", "/webjars/**")
	      .permitAll()
	      .anyRequest()
	      .authenticated();
		
		http
        .sessionManagement()
            .maximumSessions(2)
            .sessionRegistry(sessionRegistry());
	}
	
	@Bean
    SpringSessionBackedSessionRegistry sessionRegistry() {
		return new SpringSessionBackedSessionRegistry(this.sessionRepository);
    }
	
	@Override
 	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
 		auth
 		// enable in memory based authentication with a user named "user" and "admin"
 		.inMemoryAuthentication().withUser("user").password("password").roles("USER")
 				.and().withUser("admin").password("password").roles("USER", "ADMIN");
 	}
}
