package com.gp.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import com.gp.validate.ValidateMessage;

/**
 **/
public class ActionResult  extends BaseResult{
	
	private String message = null;

	private Object data = null;
	
	private Set<ValidateMessage> detailmsgs = null;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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
		map.put("message", this.message);
		map.put("data", data);
		map.put("state", super.getState());
		if(CollectionUtils.isNotEmpty(this.detailmsgs)){
			map.put("detailmsgs", detailmsgs);
		}
		return map;
	}

	public Set<ValidateMessage> getDetailmsgs() {
		return detailmsgs;
	}

	public void setDetailmsgs(Set<ValidateMessage> detailmsgs) {
		this.detailmsgs = detailmsgs;
	}
}
