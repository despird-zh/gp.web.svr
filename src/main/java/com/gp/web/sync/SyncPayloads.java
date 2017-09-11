package com.gp.web.sync;

import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;

import com.google.common.collect.Sets;
/**
 * static methods to clean the property map which inherit from {@link CoreEventPayload}  
 * the cleaned property map will be sent as sync command pay load.
 * 
 * @author gdiao
 * @version 0.1 2017-7-6
 **/
public class SyncPayloads {
	
	public static SyncType CMD_UPD_SOURCE = new SyncType("cmd", "upd", "source");
	
	/**
	 * Include the entry with specified
	 * @author gdiao
	 * @param payload the paylod map to be clean
	 * @param includeKeys the keys to reserve
	 **/
	public static void includePayload(Map<String, Object> payload, String ... includeKeys) {
		
		cleanPayload(payload, true, includeKeys);
	}
	
	/**
	 * Exclude the entry with specified
	 * @author gdiao
	 * @param payload the paylod map to be clean
	 * @param excludeKeys the keys to remove
	 **/
	public static void excludePayload(Map<String, Object> payload, String ... excludeKeys) {
		
		cleanPayload(payload, false, excludeKeys);
	}
	
	/**
	 * Clean the payload of operation message 
	 * 
	 **/
	private static void cleanPayload(Map<String, Object> payload, boolean include, String ... keys) {
		
		if(MapUtils.isEmpty(payload)) return;
		Set<String> keylist = Sets.newHashSet(keys);
		for(Map.Entry<String, Object> entry: payload.entrySet()) {
			
			if((!keylist.contains(entry.getKey())) && include ) {
				payload.remove(entry.getKey());
			}
			else if((keylist.contains(entry.getKey())) && !include ) {
				payload.remove(entry.getKey());
			}
		}
	}
}
