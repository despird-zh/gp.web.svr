package com.gp.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import com.gp.validate.ValidateMessage;

/**
 * 
 * @author diaogc
 * @version 0.1 2014-12-10
 **/
public class ActionResult{
	
	/** the action operation state : success */ 
	public static final String SUCCESS = "success";
	/** the action operation state : error */ 
	public static final String ERROR = "error";
	/** the action operation state : error */ 
	public static final String FAIL = "fail";
	
	private Meta meta = null;

	private Object data = null;

    public static ActionResult failure(String message) {
    	ActionResult ref = new ActionResult();
        ref.meta = new Meta(FAIL, message);
        return ref;
    }
    
    public static ActionResult error(String message) {
    	ActionResult ref = new ActionResult();
        ref.meta = new Meta(ERROR, message);
        return ref;
    }
    
    public static ActionResult success(String message) {
    	ActionResult ref = new ActionResult();
        ref.meta = new Meta(SUCCESS, message);
        return ref;
    }
    
	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}	
	
	/**
	 * convert the action result into map 
	 **/
	public Map<String, Object> asMap(){
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("meta", this.meta);
		map.put("data", data);
		
		return map;
	}
	
    public static class Meta {

        private String state;
        private String message;

        public Meta(String state) {
            this.state = state;
        }

        public Meta(String state, String message) {
            this.state = state;
            this.message = message;
        }

        public String getState() {
            return state;
        }

        public String getMessage() {
            return message;
        }
    }
}
