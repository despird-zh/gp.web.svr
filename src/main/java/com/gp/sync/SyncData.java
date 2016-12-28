package com.gp.sync;

import java.util.HashMap;
import java.util.Map;

public class SyncData{
	
	/** the property map */
	private Map<String, Object> props = new HashMap<String, Object>();
	
	/** the meta data */
	private SyncMeta metadata = null;
	
	/**
	 * Get the meta data of message 
	 **/
	public SyncMeta getMetadata(){
		
		return this.metadata;
	}
	
	/**
	 * Set the meta data of message 
	 **/
	public void setMetadata(SyncMeta metadata){
		
		this.metadata = metadata;
	}
	
	/**
	 * Set the properties map  
	 **/
	public void setPropMap(Map<String, Object> props){
		this.props = props;
	}
	
	/**
	 * Get the property map 
	 **/
	public Map<String, Object> getPropMap(){
		
		return this.props;
	}

}
