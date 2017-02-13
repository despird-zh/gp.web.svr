package com.gp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.gp.dao.info.UserInfo;
import com.gp.svc.AuthorizeService;
import com.gp.svc.SecurityService;

public class PrincipalsService implements UserDetailsService {
	
	@Autowired
	private SecurityService securitysvc;
	
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

    	UserInfo uinfo = null;
        try {
        	
            uinfo = securitysvc.getAccountLite(null, null, userName, null);

        } catch (Exception e) {
            throw new UsernameNotFoundException("user select fail");
        }
        if(uinfo == null){
            throw new UsernameNotFoundException("no user found");
        } else {
            PrincipalDetails princd = new PrincipalDetails(userName);
            //princd
            
            return princd;
        }
    }
}

