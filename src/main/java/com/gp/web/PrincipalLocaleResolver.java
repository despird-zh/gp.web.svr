package com.gp.web;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.i18n.AbstractLocaleResolver;
import com.gp.common.Principal;
public class PrincipalLocaleResolver extends AbstractLocaleResolver{

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Principal principal = (Principal) authentication.getPrincipal();
		if(null != principal){
			setDefaultLocale(principal.getLocale());
		}else{
			setDefaultLocale(request.getLocale());
		}
        return getDefaultLocale();
	}

	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		 super.setDefaultLocale(locale);
	}

}
