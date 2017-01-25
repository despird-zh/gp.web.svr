package com.gp.web;

import java.util.HashMap;
import java.util.Map;

/**
 * ActionResult Wrap the result of JSON data.
 * 
 * @author diaogc
 * @version 0.1 2014-12-10
 **/
public class ActionResult{
	
	private Meta meta = null;

	private Object data = null;

    public static ActionResult failure(String message) {
    	ActionResult ref = new ActionResult();
        ref.meta = new Meta(BaseController.FAIL, message);
        return ref;
    }
    
    public static ActionResult error(String message) {
    	ActionResult ref = new ActionResult();
        ref.meta = new Meta(BaseController.ERROR, message);
        return ref;
    }
    
    public static ActionResult success(String message) {
    	ActionResult ref = new ActionResult();
        ref.meta = new Meta(BaseController.SUCCESS, message);
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
		map.put(BaseController.MODEL_KEY_META, this.meta);
		map.put(BaseController.MODEL_KEY_DATA, data);
		
		return map;
	}
	
	/**
	 * Meta class keep the state, error code and message. 
	 **/
    public static class Meta {

        private String state;
        private String code;
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

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}
    }
}
