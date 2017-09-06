package com.gp.web;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;

import com.gp.common.AccessPoint;
import com.gp.common.GPrincipal;
import com.gp.core.SecurityFacade;
import com.gp.exception.CoreException;
import com.gp.svc.SecurityService;

public class UserPasswordAuthenProvider implements AuthenticationProvider{

	static Logger LOGGER = LoggerFactory.getLogger(UserPasswordAuthenProvider.class);
	
	@Autowired
	SecurityService securitysvc;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		String username = null;
		String password = null;
		
		LOGGER.debug("FormLogin Auth :{}", authentication.getName());
		username = authentication.getName();
		password = (String)authentication.getCredentials();
		
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) ) {
            throw new BadCredentialsException("Invalid Backend User Credentials");
        }
        
        AccessPoint accesspoint = new AccessPoint("sync","blind");
        try {
			GPrincipal principal = SecurityFacade.findPrincipal(accesspoint, null, username, null);
			if(null == principal){
				throw new AuthenticationCredentialsNotFoundException("Account not exist");
			}
			boolean pass = SecurityFacade.authenticate(accesspoint, principal, password);
			if(pass) {
				UserPasswordAuthenToken rtv =  new UserPasswordAuthenToken(username, "blind",
		                AuthorityUtils.commaSeparatedStringToAuthorityList("USER"));
				rtv.setDetails(principal);

		        return rtv;
			}else {
				throw new BadCredentialsException("Bad Credentials");
			}
		} catch (CoreException e) {
			throw new AuthenticationServiceException(e.getMessage());
		}
        
	}
    
	@Override
	public boolean supports(Class<?> tokenClazz) {
		return tokenClazz.equals(UsernamePasswordAuthenticationToken.class) ||
				tokenClazz.equals(UserPasswordAuthenToken.class);
	}

}
