package com.gp.web;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.i18n.AbstractLocaleResolver;
import com.gp.common.Principal;
public class PrincipalLocaleResolver extends AbstractLocaleResolver{

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		Subject current = SecurityUtils.getSubject();
		Principal principal = (Principal)current.getPrincipal();
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
