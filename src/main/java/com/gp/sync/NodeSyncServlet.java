package com.gp.sync;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gp.sync.SyncMeta.SyncType;
import com.gp.sync.SyncPacket.SyncResult;

public class NodeSyncServlet extends BaseSyncServlet{

	private static final long serialVersionUID = 5265917566064206401L;

	static Logger LOGGER = LoggerFactory.getLogger(NodeSyncServlet.class);
	
	@Override
	public void init() throws ServletException {
		
		String value = getInitParameter(BUFFER_SIZE);		
		if(StringUtils.isNumeric(value)){
			
			bufferSize = Integer.valueOf(value);
		}else{
			
			bufferSize = 204800; // 200K bytes
		}
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
		
		SyncPacket syncpacket = readSyncPack(request);
		SyncMeta syncmeta = SyncDataUtils.getSyncMeta(syncpacket.getMetaJsonData());
		if(SyncType.BYTES == syncmeta.getSyncType()){
			
			
		}else{
			
			SyncInInfo syncin = SyncDataUtils.getSyncInInfo(syncpacket);
			SyncDataAccessor sda = new SyncDataAccessor();
			sda.saveSyncInInfo(syncin);			
		}
		
		SyncResult result = new SyncResult();
		String json = SyncDataUtils.generateJsonResult(result);
		PrintWriter writer = response.getWriter();
		writer.write(json);
		writer.flush();
		writer.close();
		
	}
	
	/**
	 * read the sync packet data.
	 **/
	protected SyncPacket readSyncPack(HttpServletRequest request) throws ServletException,IOException{
		
		String metaStr = request.getParameter(SYNC_META);	
		String propStr = request.getParameter(SYNC_PROP);
		byte[] bytes = readMultipartData(request, SYNC_BINARY);
		
		SyncPacket syncpacket = new SyncPacket();
		syncpacket.setMetaJsonData(metaStr);
		syncpacket.setPropJsonData(propStr);
		syncpacket.setBytesData(bytes);
		
		return syncpacket;
	}
}
