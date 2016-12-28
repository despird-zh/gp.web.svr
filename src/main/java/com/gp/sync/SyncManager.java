package com.gp.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gp.sync.SyncField.FieldType;

public class SyncManager {

	static Logger LOGGER = LoggerFactory.getLogger(SyncManager.class);
	
	private Map<String, List<SyncField>> fieldsmap = new HashMap<String, List<SyncField>>();

	static private SyncManager singleton = new SyncManager();
	
	private SyncManager(){
		
		try {
			load();
		} catch (ConfigurationException e) {
			LOGGER.error("fail to load sync-data fields.", e);
		}
	}
		
	public void load() throws ConfigurationException{
		
		XMLConfiguration config = new XMLConfiguration("sync_fields.xml");  
		
		List<Object> l = config.getList("sync-data[@name]");
		for(int i = 0; i< l.size(); i++){
			
			Object dataname = config.getProperty("sync-data("+i+")[@name]");
			LOGGER.debug("start load sync-data = {}", dataname);
			List<SyncField> fieldlist = new ArrayList<SyncField>();
			
			List<Object> lf = config.getList("sync-data("+i+").sync-field[@name]");
			for(int j = 0; j< lf.size(); j++){
				
				SyncField field = new SyncField();
				Object val = config.getProperty("sync-data("+i+").sync-field("+j+")[@name]");
				LOGGER.debug("read sync-data field.name = {}", val);
				field.setName(val.toString());
				
				val = config.getProperty("sync-data("+i+").sync-field("+j+")[@db-field]");
				LOGGER.debug("read sync-data field.dbfield = {}", val);
				field.setDbfield(val.toString());
				
				val = config.getProperty("sync-data("+i+").sync-field("+j+")[@type]");
				LOGGER.debug("read sync-data field.type = {}", val);
				FieldType type = FieldType.valueOf(val.toString().toUpperCase());
				field.setType(type);
				
				fieldlist.add(field);
			}
			
			LOGGER.debug("end load sync-data = {}", dataname);
			fieldsmap.put(dataname.toString(), fieldlist);
		}
	}
	
	
	public static List<SyncField> getSyncFields(String syncdataname){
		
		return singleton.fieldsmap.get(syncdataname);
	}

}
