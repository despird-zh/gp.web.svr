/*
 * Licensed to the Ultrabroad Company 
 * 
 */
package com.gp.web.shiro;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gp.audit.AccessPoint;
import com.gp.common.Principal;
import com.gp.common.GroupUsers;
import com.gp.common.GroupUsers.UserState;
import com.gp.core.SecurityFacade;
import com.gp.exception.CoreException;
import com.gp.dao.info.UserInfo;

/**
 * @author despird 2014-02-01
 * @version V0.1
 * */
public class AuthorizeRealm extends AuthorizingRealm {

	public static Logger logger = LoggerFactory.getLogger(AuthorizeRealm.class);
	@Override
	public boolean supports(AuthenticationToken token) {
		if (token instanceof AuthenToken) {
			return true;
		}

		return false;
	}

	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		Set<String>			roles			= new HashSet<String>();
		Set<Permission>		permissions		= new HashSet<Permission>();
		Collection<Principal>	principalsList	= principals.byType(Principal.class);
		
		if (principalsList.isEmpty()) {
			throw new AuthorizationException("Empty principals list!");
		}
		//LOADING STUFF FOR PRINCIPAL 
		for (Principal principal : principalsList) {
			// Only when dctm standard mode we try to fetch the group and role information
			//if(ServiceConstants.REALM_TYPE_DCTM.equals(userPrincipal.getRealm())){
				// ignore group query processing
			//}			
		}
		//THIS IS THE MAIN CODE YOU NEED TO DO !!!!		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles);
		info.setRoles(roles); //fill in roles 
	
		info.setObjectPermissions(permissions); //add permisions (MUST IMPLEMENT SHIRO PERMISSION INTERFACE)
		
		return info;
	}

	/**
	 * It Covers two kinds of Token processing:
	 * UbDctmToken   --> UbDctmAuthenInfo
	 * UbSpnegoToken --> UbSpnegoAuthenInfo
	 **/
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		AuthenticationInfo authenInfo = null;
		if (token instanceof AuthenToken) {
			authenInfo = getDBAuthenInfo((AuthenToken)token);
		}

		return authenInfo;
	}

	
	/**
	 * Get database standard password authentication info via AuthenToken
	 **/
	private AuthenInfo getDBAuthenInfo(AuthenToken authtoken){
		
		// here user name is the login account
		Principal principal = null;
		UserInfo uinfo = null;
		AccessPoint ap = authtoken.getAccessPoint();
		try {		
			
			uinfo = SecurityFacade.findAccountLite(ap, GroupUsers.PSEUDO_USER, null, authtoken.getUsername(), null);

			if (uinfo == null) {
				throw new UnknownAccountException("Login name [" + authtoken.getUsername() + "] not found!");
			}			
			else{ 
				
				principal = new Principal(uinfo.getInfoId());
				principal.setSourceId(uinfo.getSourceId());
				principal.setAccount(authtoken.getUsername());
		        principal.setEmail(uinfo.getEmail());
		        principal.setPassword(uinfo.getPassword());
		        
				if(uinfo.getRetryTimes() >= 3){
			
					SecurityFacade.changeAccountState(ap, principal, UserState.FROZEN);
					throw new ExcessiveAttemptsException("Login name [" + authtoken.getUsername() + "] attempt too much!");
					
				}else if(UserState.DEACTIVE.name().equalsIgnoreCase(uinfo.getState())){
					
					throw new DisabledAccountException("Login name [" + authtoken.getUsername() + "] be deactive!");
				}else if(UserState.FROZEN.name().equalsIgnoreCase(uinfo.getState())){
					
					throw new LockedAccountException("Login name [" + authtoken.getUsername() + "] be locked!");
				}
			}
		} catch (CoreException se) {
			throw new AuthenticationException(se);
		}
				
        // zh_CN / en_US / fr_FR
        String[] localeStr = StringUtils.split(uinfo.getLanguage(), "_");
        Locale locale = Locale.ENGLISH;
        if(localeStr.length == 2){
        	locale = new Locale(localeStr[0], localeStr[1]);
        }
        principal.setLocale(locale);
         
 		principal.setTimeZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone(uinfo.getTimeZone())));
		
		logger.info("Found user with username [" + authtoken.getUsername() + "]");

		return new AuthenInfo(principal, principal.getPassword(), getName());
	}	

}
