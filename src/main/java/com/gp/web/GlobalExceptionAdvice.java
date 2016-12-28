package com.gp.web;

import java.sql.SQLException;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.gp.exception.ServiceException;
import com.gp.exception.WebException;


/**
 * Performs the same exception handling as {@link ExceptionHandlingController}
 * but offers them globally. The exceptions below could be raised by any
 * controller and they would be handled here, if not handled in the controller
 * already.
 * 
 * @author Paul Chapman
 */
@ControllerAdvice
public class GlobalExceptionAdvice {

	protected Logger logger;

	public GlobalExceptionAdvice() {
		logger = LoggerFactory.getLogger(getClass());
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
	/* . . . . . . . . . . . . . EXCEPTION HANDLERS . . . . . . . . . . . . . . */
	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
	
	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	public ModelAndView noHandlerFound(HttpServletRequest request, NoHandlerFoundException exception) {
	    Locale locale = LocaleContextHolder.getLocale();
	    //String errorMessage = messageSource.getMessage("error.bad.url", null, locale);

	    logger.error("Request: " + request.getRequestURI() + " not found handler or resources ");
	    ModelAndView mav = getModelAndView(request);

	    mav.addObject("exception", exception);
		mav.addObject("url", request.getRequestURL());
		mav.addObject("timestamp", new Date().toString());
		mav.addObject("state", ActionResult.ERROR);

		return mav;
	}
	
	@ExceptionHandler(WebException.class)
	@ResponseStatus(value=HttpStatus.NETWORK_AUTHENTICATION_REQUIRED)
	public ModelAndView handleRelogonError(HttpServletRequest request, WebException exception) {

	    logger.error("Request: " + request.getRequestURI() + " relogon to authenticate is required ");
	    ModelAndView mav = new ModelAndView("error/511");

		return mav;
	}
	
	/**
	 * Convert a predefined exception to an HTTP Status code and specify the
	 * name of a specific view that will be used to display the error.
	 * 
	 * @return Exception view.
	 */
	@ExceptionHandler({ SQLException.class, DataAccessException.class, ServiceException.class })
	public ModelAndView handleDatabaseError(HttpServletRequest request,Exception exception) {
		// Nothing to do. Return value 'databaseError' used as logical view name
		// of an error page, passed to view-resolver(s) in usual way.
		logger.error("Request raised " + exception.getClass().getSimpleName(), exception);
		ModelAndView mav = getModelAndView(request);
		
		mav.addObject("exception", exception);
		mav.addObject("url", request.getRequestURL());
		mav.addObject("timestamp", new Date().toString());
		mav.addObject("state", ActionResult.ERROR);

		return mav;
	}

	/**
	 * Demonstrates how to take total control - setup a model, add useful
	 * information and return the "support" view name. This method explicitly
	 * creates and returns
	 * 
	 * @param req
	 *            Current HTTP request.
	 * @param exception
	 *            The exception thrown - always {@link SupportInfoException}.
	 * @return The model and view used by the DispatcherServlet to generate
	 *         output.
	 * @throws Exception
	 */
	@ExceptionHandler(Exception.class)
	public ModelAndView handleError(HttpServletRequest request, Exception exception)
			throws Exception {

		// Rethrow annotated exceptions or they will be processed here instead.
		if (AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class) != null)
			throw exception;

		logger.error("Request: " + request.getRequestURI() + " raised " + exception, exception);


	    ModelAndView mav = getModelAndView(request);

	    mav.addObject("exception", exception);
		mav.addObject("url", request.getRequestURL());
		mav.addObject("timestamp", new Date().toString());
		mav.addObject("state", ActionResult.ERROR);

		return mav;
	}
	
	
	private ModelAndView getModelAndView(HttpServletRequest request){
		// as for ajax request
		if("XMLHttpRequest".equalsIgnoreCase((request).getHeader("X-Requested-With"))){
			return new ModelAndView(new MappingJackson2JsonView()); 
		}else{
			return new ModelAndView("error/support");
		}
	}
}