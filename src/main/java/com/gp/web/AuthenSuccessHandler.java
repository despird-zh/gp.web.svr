package com.gp.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import com.gp.common.GPrincipal;
import com.gp.web.util.ExWebUtils;

public class AuthenSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	static Logger LOGGER = LoggerFactory.getLogger(AuthenSuccessHandler.class);
	
	@Override
	protected void handle(HttpServletRequest request, 
			HttpServletResponse response, 
			Authentication authentication) throws IOException, ServletException {
		
		Object detail = authentication.getDetails();
		if(detail != null && detail instanceof GPrincipal) {
		
			if(LOGGER.isDebugEnabled())
				LOGGER.debug("Customized success handler to attatch GPrincipal to request");
			
			GPrincipal principal = (GPrincipal) detail;
			ExWebUtils.setPrincipal(request, principal);
		}
		
		super.handle(request, response, authentication);
	}

}
