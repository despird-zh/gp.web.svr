package com.gp.sync;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseSyncServlet extends HttpServlet{

	private static final long serialVersionUID = 5724686555636975528L;
	
	static Logger LOGGER = LoggerFactory.getLogger(BaseSyncServlet.class);
	
	/** the message meta parameter key*/
	static public final String SYNC_META = "sync-meta";
	
	/** the message body parameter key */
	static public final String SYNC_PROP = "sync-prop";
	
	/** the message data parameter key */
	static public final String SYNC_BINARY = "sync-binary";
	
	static public final String BUFFER_SIZE = "buffersize";
	
	protected int bufferSize = -1;
	
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		
		requestProcess(request, response);
	}
	
	@Override
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		
		requestProcess(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{}
	
	@Override
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		
		requestProcess(request, response);
	}
	
	protected void requestProcess(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{

        
	}
	
	/**
	 * Shown header information
	 **/
	public void dumpHttpHeaders(HttpServletRequest request){
		
		Enumeration<?> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()) {
		  String headerName = (String)headerNames.nextElement();
		  LOGGER.debug("header name[{}] - value[{}]", headerName, request.getHeader(headerName));
		}
	}
	
	/**
	 * Read multi-part data from request.
	 * 
	 * @param request the http request
	 * @param partname the request part name
	 * 
	 * @return ByteBuffer the bytebuffer object.
	 **/
	public byte[] readMultipartData(HttpServletRequest request, String partname) throws ServletException, IOException{
		
		Part part = request.getPart(partname);
		
		if (part != null) {
			
			byte[] buffer = new byte[bufferSize];
			ByteArrayOutputStream outStream = new ByteArrayOutputStream(bufferSize);
			String header = part.getHeader("content-disposition");
			System.out.println("header="+header);

			InputStream ins = part.getInputStream();
			int readCnt = 0;
			try {
				while (true) {
					readCnt = 0;
					readCnt = ins.read(buffer);
					if (readCnt <= 0)
						break;
					outStream.write(buffer, 0, readCnt);
				}
			} finally {
				if(null != ins)
				ins.close();
			}
			
			byte[] byteData = outStream.toByteArray();
			
			return byteData;
		} else {

			return new byte[0];
		}
	}
}
