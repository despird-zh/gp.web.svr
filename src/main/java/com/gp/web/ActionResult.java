package com.gp.web;

import java.util.HashMap;
import java.util.Map;

/**
 * ActionResult Wrap the result of JSON data.
 * 
 * @author diaogc
 * @version 0.1 2014-12-10
 * 
 **/
public class ActionResult{
	
	/** the meta information of result */
	private Meta meta = null;

	/** the data holder of result */
	private Object data = null;

	/**
	 * Build a new failure state result with message
	 * 
	 * @param message the message string
	 * @return ActionResult the action result
	 **/
    public static ActionResult failure(String message) {
    	ActionResult ref = new ActionResult();
        ref.meta = new Meta(BaseController.FAIL, message);
        return ref;
    }
    
	/**
	 * Build a new error state result with message
	 * 
	 * @param message the message string
	 * @return ActionResult the action result
	 **/
    public static ActionResult error(String message) {
    	ActionResult ref = new ActionResult();
        ref.meta = new Meta(BaseController.ERROR, message);
        return ref;
    }
    
	/**
	 * Build a new success state result with message
	 * 
	 * @param message the message string
	 * @return ActionResult the action result
	 **/
    public static ActionResult success(String message) {
    	ActionResult ref = new ActionResult();
        ref.meta = new Meta(BaseController.SUCCESS, message);
        return ref;
    }
    
    /**
     * Get meta information
     * @return Meta the meta  
     **/
	public Meta getMeta() {
		return meta;
	}

	/**
	 * Set meta information 
	 **/
	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	/**
	 * Get the data information 
	 **/
	public Object getData() {
		return data;
	}

	/**
	 * Set the data information 
	 **/
	public void setData(Object data) {
		this.data = data;
	}	
	
	/**
	 * Convert the action result into a map, it to be easy to append to ModelAndView
	 **/
	public Map<String, Object> asMap(){
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put(BaseController.MODEL_KEY_META, this.meta);
		map.put(BaseController.MODEL_KEY_DATA, data);
		
		return map;
	}
	
	/**
	 * Meta class keep the state, error code and message. 
	 **/
    public static class Meta {

    	/** the state of meta */
        private String state;
        /** the code of meta */
        private String code;
        /** the message of meta */
        private String message;

        /**
         * Create Meta with state 
         **/
        public Meta(String state) {
            this.state = state;
        }

        /**
         * Create Meata with state and message 
         **/
        public Meta(String state, String message) {
            this.state = state;
            this.message = message;
        }

        /**
         * Get the state 
         **/
        public String getState() {
            return state;
        }

        /**
         * Get the message 
         **/
        public String getMessage() {
            return message;
        }

        /**
         * Get the code 
         **/
		public String getCode() {
			return code;
		}

		/**
		 * Set the code 
		 **/
		public void setCode(String code) {
			this.code = code;
		}
    }
}
