
package com.gp.web.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gp.common.Principal;
import com.gp.core.SecurityFacade;

public class CredMatcher implements CredentialsMatcher {
	
	public static Logger LOGGER = LoggerFactory.getLogger(CredMatcher.class);
	
	public boolean doCredentialsMatch(AuthenticationToken token,
			AuthenticationInfo info) {
		
		LOGGER.debug("entering matcher...");
		if((info instanceof AuthenInfo) && (token instanceof AuthenToken)){

			return authenModeDB((AuthenToken)token, (AuthenInfo)info);
		}
		
		return true;
	}
	
	/**
	 * Compare the Dctm standard mode processing 
	 **/ 
	private boolean authenModeDB(AuthenToken token, AuthenInfo info){
		
		String password = new String(token.getPassword());
		Principal principal = (Principal)info.getPrincipals().getPrimaryPrincipal();

		boolean pass = false;
		try {
			
			pass = SecurityFacade.authenticate(token.getAccessPoint(),principal, password);
		
		} catch (Exception e) {
			LOGGER.error("Error matching verification...", e);
		}

		return pass;
	}

}
