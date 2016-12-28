
package com.gp.web.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

import com.gp.audit.AccessPoint;

/**
 * @author despird 2014-3-2
 * 
 **/
public class AuthenToken extends UsernamePasswordToken {
	
	private static final long serialVersionUID = -3725717335876726792L;

	private String language = null;

	private AccessPoint accesspoint = null;
	
	/**
	 * Constructor for user password and language.
	 * 
	 * @param user the user 
	 * @param password the password
	 * @param language the language 
	 **/
	public AuthenToken(String user,String password, String language){
		
		super(user, password);
		this.language = language;
	}
	
	public AuthenToken(String user,String password, AccessPoint accesspoint){
		
		super(user, password);
		this.accesspoint = accesspoint;
	}
	
	/**
	 * Get the language setting
	 * 
	 * @return String the language 
	 **/
	public String language() {
		return language;
	}

	public AccessPoint getAccessPoint() {
		return accesspoint;
	}

	public void setAccessPoint(AccessPoint accesspoint) {
		this.accesspoint = accesspoint;
	}
	
}
