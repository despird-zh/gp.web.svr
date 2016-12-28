/*
 * Licensed to the Ultrabroad Company 
 * 
 */
package com.gp.web.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.util.WebUtils;

import com.gp.audit.AccessPoint;
import com.gp.common.SystemOptions;
import com.gp.util.ConfigSettingUtils;

/**
 * @author Gary Diao 
 * @version V0.1 2012-07-01
 * */
public abstract class CustomWebUtils extends WebUtils{
	
	static Logger logger = LoggerFactory.getLogger(CustomWebUtils.class);
	
	/**
	 * Dump the attributes in session to facilitate the development
	 * 
	 * @param HttpServletRequest the request from client.
	 * 
	 * */
	public static void dumpSessionAttributes(HttpServletRequest  request){
		HttpSession session = request.getSession(false);
		if(session != null) {
			Enumeration<String> keys = session.getAttributeNames();
			logger.info("Start Dumping the session attributes ----" );
			while (keys.hasMoreElements())
			{
			  String key = (String)keys.nextElement();
			  logger.info("Attribute KEY : " + key + " - VALUE : " + session.getAttribute(key) );
			}
			logger.info("End Dumping the session attributes ----" );
		}else{
		
			logger.info("session is null!" );
		}
	}

	/**
	 * Dump the attributes in request to facilitate the development
	 * 
	 * @param HttpServletRequest the request from client.
	 * 
	 * */
	public static void dumpRequestAttributes(HttpServletRequest  request){
		
		Enumeration<String> keys = request.getParameterNames();
		logger.info("Start Dumping the request attributes ----" );
		Cookie[] cookies = request.getCookies();
		while( keys.hasMoreElements() ) {
			String key = keys.nextElement();

			for( String value : request.getParameterValues( key ) ) {
				logger.info("Attribute KEY : " + key + " - VALUE : " + value );
			}
		}
		if(cookies != null && cookies.length > 0){
			logger.info("Dumping the request cookies ----" );
			for (Cookie cookie:cookies){
				
				logger.info("Cookie Name : " + cookie.getName() + " - VALUE : " + cookie.getValue() );
			}
			
		}else {
			
			logger.info("No cookie in request ----" );
		}
		logger.info("End Dumping the request attributes ----" );
	}
	
	/**
	 * Dump the content of request body, it's used for ajax debuging, some time we
	 * need to log out the ajax string carried by http request.
	 * 
	 * Important!!!!: this method is only used for debuging operation. coz it traverse the InputStream
	 * cause the reader index placed on end. if use stream again, it pops IOException;
	 *
	 * @param HttpServletRequest the request object.
	 **/
	public static void dumpRequestBody(HttpServletRequest  request){
		
		ServletServerHttpRequest req = new ServletServerHttpRequest(request);
		try {
			logger.info("Start Dumping the request body content ----" );
			String text = CustomWebUtils.getRequestBody(req.getBody());
			logger.info(text);
			logger.info("End Dumping the request body content ----" );
		} catch (IOException e) {

			e.printStackTrace();
		}  
		
	}
	
	/**
	 * Generate and UUID 
	 * @return String 
	 **/
	public static String generateUUID(){
	
		UUID s = UUID.randomUUID();
		return s.toString();
	}
	
	/**
	 * Get the UUID object
	 * @param the uuid string
	 * 
	 * @return the UUID
	 **/
	public static UUID toUUID(String s){
		
		if ( s==null || s.length() ==0){
			logger.debug("The String value is null, can't convert to UUID!");
			return null;
		}
		return UUID.fromString(s);
	}
	
	/**
	 * Get the RequestBody String, default is UTF-8 encoding
	 * 
	 * @param InputStream the inputstream extract from httprequest.
	 * @return String request body content
	 * 
	 **/
	public static String getRequestBody(InputStream  is){
		
		return getRequestBody(is, "UTF-8");
	}

	/**
	 * Get the RequestBody String
	 * 
	 * @param InputStream the inputstream extract from httprequest.
	 * @param coding , UTF-8 etc.
	 * @return String request body content
	 * 
	 **/
	public static String getRequestBody(InputStream  is, String coding){
		
        InputStreamReader input = null;
		try {
			input = new InputStreamReader(is, coding);
		} catch (UnsupportedEncodingException e) {

			//e.printStackTrace();
			logger.debug("getRequestBody UnsupportedEncodingException:",e);
		}
        final int CHARS_PER_PAGE = 5000; //counting spaces
        final char[] buffer = new char[CHARS_PER_PAGE];
        StringBuilder output = new StringBuilder(CHARS_PER_PAGE);
        try {
            for(int read = input.read(buffer, 0, buffer.length);
                    read != -1;
                    read = input.read(buffer, 0, buffer.length)) {
                output.append(buffer, 0, read);
            }
        } catch (IOException ignore) { 
        	
        	logger.debug("getRequestBody IOException",ignore);
        }

        String text = output.toString();
        return text;
	}
	
	/**
	 * Pings a HTTP URL. This effectively sends a HEAD request and returns <code>true</code> if the response code is in 
	 * the 200-399 range.
	 * @param url The HTTP URL to be pinged.
	 * @param timeout The timeout in millis for both the connection timeout and the response read timeout. Note that
	 * the total timeout is effectively two times the given timeout.
	 * @return <code>true</code> if the given HTTP URL has returned response code 200-399 on a HEAD request within the
	 * given timeout, otherwise <code>false</code>.
	 */
	public static boolean ping(String url, int timeout) {
	    url = url.replaceFirst("https", "http"); // Otherwise an exception may be thrown on invalid SSL certificates.

	    try {
	        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
	        connection.setConnectTimeout(timeout);
	        connection.setReadTimeout(timeout);
	        connection.setRequestMethod("HEAD");
	        int responseCode = connection.getResponseCode();
	        return (200 <= responseCode && responseCode <= 399);
	    } catch (IOException exception) {
	        return false;
	    }
	}
	
	public static AccessPoint getAccessPoint(HttpServletRequest  request){
		
		String client = request.getHeader("User-Agent");
		String host = request.getRemoteHost();
		String app = ConfigSettingUtils.getSystemOption(SystemOptions.SYSTEM_APP);
		String version = ConfigSettingUtils.getSystemOption(SystemOptions.SYSTEM_VERSION);
		
		AccessPoint ap = new AccessPoint(client, host, app, version);
		
		return ap;
	}
	
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}