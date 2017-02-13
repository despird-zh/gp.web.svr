package com.gp.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import com.gp.exception.ServiceException;
import com.gp.svc.AuthorizeService;

@Service
public class MetadataSourceService implements FilterInvocationSecurityMetadataSource {

	private static final Logger LOGGER = LoggerFactory.getLogger(MetadataSourceService.class);

	private AuthorizeService authoritysvc;

	@Autowired
	public MetadataSourceService(AuthorizeService authoritysvc) {
		this.authoritysvc = authoritysvc;
		initResources();
	}

	private HashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new HashMap<RequestMatcher, Collection<ConfigAttribute>>();

	private void initResources() {
		
		LOGGER.debug("init SecurityMetadataSource load all resources");
		
		List<DefaultKeyValue> all = new ArrayList<DefaultKeyValue>(0);
		try {
			all = authoritysvc.getRolePagePerms(null, null);
		} catch (ServiceException e) {
			LOGGER.error("fail load all role page perms", e);
		}
		
		for (DefaultKeyValue kv : all) {
			
			RequestMatcher matcher = new AntPathRequestMatcher((String)kv.getKey());
			Collection<ConfigAttribute> array = requestMap.get(matcher);
			if(null == array){
				array = new ArrayList<ConfigAttribute>();
				requestMap.put(matcher, array);
			}
			
			SecurityConfig securityConfig = new SecurityConfig((String)kv.getValue());
			array.add(securityConfig);

		}
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		LOGGER.debug("get resource " + object + " authority");
	
		final HttpServletRequest request = ((FilterInvocation) object).getRequest();
	
		Collection<ConfigAttribute> attrHashMap = new HashSet<ConfigAttribute>();
		for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
			if (entry.getKey().matches(request)) {
				LOGGER.debug("request matches :" + request.getRequestURL());
				attrHashMap.addAll(entry.getValue());
			}
		}
		if (attrHashMap.size() > 0) {
			
			Collection<ConfigAttribute> attr = new ArrayList<ConfigAttribute>(attrHashMap);
			return attr;
		}
		LOGGER.debug("request no matches");
		return Collections.emptyList();
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		Collection<ConfigAttribute> rtv = new HashSet<ConfigAttribute>();
		
		for(Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()){
			rtv.addAll(entry.getValue());
		}
		
		return rtv;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		
		return true;
	}

}
