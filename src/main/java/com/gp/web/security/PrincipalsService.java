package com.gp.web.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class PrincipalsService implements UserDetailsService {
	
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        
    	return null;
//        try {
//            user = systemUserService.findByName(userName);
//        } catch (Exception e) {
//            throw new UsernameNotFoundException("user select fail");
//        }
//        if(user == null){
//            throw new UsernameNotFoundException("no user found");
//        } else {
//            try {
//                List<UserRole> roles = userRoleService.getRoleByUser(user);
//                return new MyUserDetails(user, roles);
//            } catch (Exception e) {
//                throw new UsernameNotFoundException("user role select fail");
//            }
//        }
    }
}

