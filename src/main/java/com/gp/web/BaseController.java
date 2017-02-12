package com.gp.web;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.util.UriUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.gp.audit.AccessPoint;
import com.gp.common.Principal;
import com.gp.web.util.CustomWebUtils;


/**
 * Define the basic method be used in Controller
 * 
 * @author gary diao
 * @version 0.1 2015-12-12 
 **/
public abstract class BaseController implements MessageSourceAware{

	static Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

	/** the action operation state : success */ 
	public static final String SUCCESS = "success";
	/** the action operation state : error */ 
	public static final String ERROR = "error";
	/** the action operation state : error */ 
	public static final String FAIL = "fail";
	
	/** Model key : data */
	public static final String MODEL_KEY_DATA = "data";
	/** Model key : meta */
	public static final String MODEL_KEY_META = "meta";
	/** Model key : state */
	public static final String MODEL_KEY_STATE = "state";
	/** Model key : message */
	public static final String MODEL_KEY_MESSAGE = "message";
	
	public static ObjectMapper JACKSON_MAPPER = new ObjectMapper();
	
	public static ObjectMapper JACKSON_MAPPER_NON_NULL = new ObjectMapper();
	
	static{
		JACKSON_MAPPER_NON_NULL.setSerializationInclusion(Include.NON_NULL);
	}
	private MessageSource messageSource;
	
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	/**
	 * Get the access point object for the request
	 * @param request the request from client 
	 **/
	public static AccessPoint getAccessPoint(HttpServletRequest request){
		
		return CustomWebUtils.getAccessPoint(request);
	}
	
	/**
	 * Get model view for json data, the model and view will accept multiple key-values.
	 **/
	public ModelAndView getJsonModelView(){
		
		return getJsonModelView(true); 		
	}
	
	/**
	 * Get model view for json data, the model and view will accept multiple key-values.
	 * @param includeNull true:ignore null in json; otherwize include null in json
	 **/
	public ModelAndView getJsonModelView(boolean includeNull){
		
		if(includeNull)
			return new ModelAndView(new MappingJackson2JsonView(JACKSON_MAPPER)); 	
		else
			return new ModelAndView(new MappingJackson2JsonView(JACKSON_MAPPER_NON_NULL)); 	
	}

	/**
	 * Get model view for json data, the attributeValue is the only data to be written.
	 **/
	public ModelAndView getJsonModelView(Object attributeValue){
		
		MappingSingleJsonView view = new MappingSingleJsonView();
		ModelAndView mav = new ModelAndView(view);
		mav.addObject(MappingSingleJsonView.SINGLE_KEY, attributeValue);
		
		return mav;
	}
	
	/**
	 * Get model view for jsp presentation 
	 **/
	public ModelAndView getJspModelView(String jspname){
		
		return new ModelAndView(jspname); 		
	}
	
	/**
	 * Get the request parameter, it's used for method without request arguments. 
	 **/
	protected String readRequestParam(String parameterName) {
		
		String value = null;
		
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		
		if (requestAttributes instanceof ServletRequestAttributes) {
			
			ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
			HttpServletRequest request = servletRequestAttributes.getRequest();
			value = request.getParameter(parameterName);
		}

		return value;
	}
	
	/**
	 * Read the request parameters into certain data wrapper.
	 * This method will retrieve the request from RequestContextHolder.
	 * @param datawrapper the data wrapper.
	 **/
	protected void readRequestData(Object dataWrapper){
		
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		
		if (requestAttributes instanceof ServletRequestAttributes) {
			
			ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
			HttpServletRequest request = servletRequestAttributes.getRequest();			
			ServletRequestDataBinder binder = new ServletRequestDataBinder(dataWrapper);
			binder.bind(request); 
		}
	}
	
	/**
	 * Read the request parameters into certain data wrapper.
	 * @param request the HTTP request
	 * @param datawrapper the data wrapper.
	 **/
	protected void readRequestData(HttpServletRequest request, Object dataWrapper){
		
		ServletRequestDataBinder binder = new ServletRequestDataBinder(dataWrapper);
		binder.bind(request); 
	}
	
	/**
	 * Read the request parameter into object.
	 * e.g {"var1":"val1","num":2} into bean object
	 * 
	 * @param parameterName The name of request parameter
	 * @param clazz The class of target bean 
	 **/
	protected <T> T readRequestJson(String parameterName, Class<T> clazz){
		
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		String value = null;
		if (requestAttributes instanceof ServletRequestAttributes) {
			
			ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
			HttpServletRequest request = servletRequestAttributes.getRequest();
			value = request.getParameter(parameterName);
		}
		
		if(null == value)
			return null;
		else{
			try {
				return JACKSON_MAPPER.readValue(value, clazz);
			} catch (Exception e) {
				LOGGER.error("Fail to parse the request json",e);
				return null;
			} 
		}
	}
	
	/**
	 * Read the request parameter into object.
	 * e.g [{"var1":"val1","num":2},{"var1":"val1","num":2}] into bean object list
	 * 
	 * @param parameterName The name of request parameter
	 * @param clazz The class of target bean 
	 **/
	protected <T> List<T> readRequestJsonArray(String parameterName, Class<T> clazz){
		
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		String value = null;
		if (requestAttributes instanceof ServletRequestAttributes) {
			
			ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
			HttpServletRequest request = servletRequestAttributes.getRequest();
			value = request.getParameter(parameterName);
		}
		
		if(null == value)
			return null;
		else{
			try {
				return JACKSON_MAPPER.readValue(value, TypeFactory.defaultInstance().constructCollectionType(List.class,  
						clazz));
			} catch (Exception e) {
				LOGGER.error("Fail to parse the request json",e);
				return null;
			} 
		}
	}
	
	/**
	 * weave the parameter with array values 
	 * @param parameter The parameter name
	 * @param values The array of values
	 * 
	 * @return the parameter string url encoded.
	 **/
	protected String weaveParameters(String parameter, String[] values) throws UnsupportedEncodingException{
		StringBuffer sbuf = new StringBuffer();
		if(null != values && values.length > 0){
			for(String value : values){
				sbuf.append(parameter).append("=")
					.append(UriUtils.encode(value, "UTF-8"))
					.append("&");
			}
			sbuf.setLength(sbuf.length()-1);
		}
		
		return sbuf.toString();
	}
	
	/**
	 * Get the principal from the security context 
	 **/
	public static Principal getPrincipal(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (Principal)authentication.getPrincipal();
	}
	
	/**
	 * Get the message in different locale
	 * @param code the message code
	 * @param args the arguments
	 * @param locale the locale 
	 **/
	public String getMessage(String code, Object[] args){
		
		Principal principal = getPrincipal();
		Locale locale = (null == principal) ? Locale.getDefault() : principal.getLocale();
		return messageSource.getMessage(code, args, locale);
	}
	
	/**
	 * Get the message in different locale
	 * @param code the message code
	 * @param locale the locale 
	 **/
	public String getMessage(String code){
		Principal principal = getPrincipal();
		Locale locale = (null == principal) ? Locale.getDefault() : principal.getLocale();
		return messageSource.getMessage(code, new String[0], locale);
	}
}
