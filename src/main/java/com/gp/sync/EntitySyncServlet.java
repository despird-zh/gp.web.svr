package com.gp.sync;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Deprecated
public class EntitySyncServlet extends BaseSyncServlet{

	private static final long serialVersionUID = 5724686555636975528L;
	
	static Logger LOGGER = LoggerFactory.getLogger(EntitySyncServlet.class);
	
	@Override
	public void init() throws ServletException {
		
	}
	
	@Override
	public void destroy() {
		
	}
	
	@Override
	protected void requestProcess(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		
		request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
		if(LOGGER.isDebugEnabled()){
			dumpHttpHeaders(request);
			String sessionid = request.getSession(true).getId();
			LOGGER.debug("session id:{}" , sessionid);
		}
		
		
	}
}
