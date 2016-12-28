package com.gp.audit;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.collections.map.TransformedMap;
import org.apache.commons.lang.mutable.MutableObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.gp.common.GeneralConstants;
import com.gp.common.IdKey;
import com.gp.info.Identifier;
import com.gp.info.InfoId;

/**
 * this class helps to convert AuditData object between json, as for different 
 * deployment environment, sometimes the auditdata need to be send to remote 
 * audit center to record so it will be converted into json to transfer.
 * 
 * @author gary diao
 * @version 0.1 2014-12-12
 * 
 **/
public class AuditConverter {
	
	static Logger LOGGER = LoggerFactory.getLogger(AuditConverter.class);
	
	static JsonFactory jsonFactory = new JsonFactory();
	
	/**
	 * convert the AuditData to json
	 * @param auditdata the AuditData object
	 **/
	public static MutableObject auditToJson(AuditEventLoad auditdata)throws IOException{
		
		ByteArrayOutputStream byteAryStream = new ByteArrayOutputStream();		
		JsonGenerator jsonGen = jsonFactory.createGenerator(byteAryStream);
		
		jsonGen.writeStartObject();
		jsonGen.writeStringField("subject", auditdata.getSubject());
		jsonGen.writeStringField("state", auditdata.getState());
		jsonGen.writeStringField("message", auditdata.getMessage());
		jsonGen.writeFieldName("accesspoint");
			jsonGen.writeStartObject();
			jsonGen.writeStringField("host", auditdata.getAccessPoint().getHost());
			jsonGen.writeStringField("client", auditdata.getAccessPoint().getClient());
			jsonGen.writeStringField("app", auditdata.getAccessPoint().getApp());
			jsonGen.writeStringField("version", auditdata.getAccessPoint().getVersion());
			jsonGen.writeEndObject();
		
		jsonGen.writeFieldName("audit");
			// write operation audit
			auditVerbToJson(jsonGen, auditdata.getAuditVerb());
			
		jsonGen.close();
		
		return new MutableObject(byteAryStream.toString());
	}
	
	/**
	 * Convert the json to AuditData
	 * @param auditjson the json string
	 *  
	 **/
	public static AuditEventLoad jsonToAudit(MutableObject auditjson) throws IOException{
		
		AuditEventLoad audit = new AuditEventLoad();
		JsonParser jsonParser = jsonFactory.createParser((String)auditjson.getValue());
		while(jsonParser.nextToken() != JsonToken.END_OBJECT){
			
			String name = jsonParser.getCurrentName();
			if("subject".equals(name)){
				jsonParser.nextToken();
				audit.setSubject(jsonParser.getValueAsString());
			}
			else if("state".equals(name)){
				jsonParser.nextToken();
				audit.setState(jsonParser.getValueAsString());
			}
			else if("message".equals(name)){
				jsonParser.nextToken();
				audit.setMessage(jsonParser.getValueAsString());
			}
			else if("accesspoint".equals(name)){
				jsonParser.nextToken();
				String host = null;
				String client = null;
				String app = null;
				String version = null;
				while(jsonParser.nextToken() != JsonToken.END_OBJECT){
					String aname = jsonParser.getCurrentName();
					if("host".equals(aname)){
						jsonParser.nextToken();
						host = jsonParser.getValueAsString();
					}
					else if("client".equals(aname)){
						jsonParser.nextToken();
						client = jsonParser.getValueAsString();
					}
					else if("app".equals(aname)){
						jsonParser.nextToken();
						app = jsonParser.getValueAsString();
					}
					else if("version".equals(aname)){
						jsonParser.nextToken();
						version = jsonParser.getValueAsString();
					}
				}
				AccessPoint accesspoint = new AccessPoint(client,host,app,version);
				audit.setAccessPoint(accesspoint);
			}

			else if("audit".equals(name)){
				jsonParser.nextToken();
				AuditVerb operaudit = jsonToAuditVerb(jsonParser);
				audit.setAuditVerb(operaudit);
			}
			
		}
		jsonParser.close();
		
		return audit;
	}
	
	private static void auditVerbToJson(JsonGenerator jsonGen, AuditVerb auditverb) throws IOException{
					
		jsonGen.writeStartObject();	
		jsonGen.writeStringField("verb", auditverb.getVerb());
		jsonGen.writeStringField("object", auditverb.getObjectId().toString());
		jsonGen.writeNumberField("start", auditverb.getTimestamp());	
		jsonGen.writeNumberField("elapse", auditverb.getElapsedTime());
		// write predicates map
		jsonGen.writeFieldName("predicates"); 
			jsonGen.writeStartObject();
			for(Map.Entry<String, String> entry:auditverb.getPredicates().entrySet())
				jsonGen.writeStringField(entry.getKey(), entry.getValue());
			
			jsonGen.writeEndObject();
			
		jsonGen.writeEndObject();

	}
	
	private static AuditVerb jsonToAuditVerb(JsonParser jsonParser) throws IOException{

		AuditVerb auditverb = new AuditVerb();
		while(jsonParser.nextToken() != JsonToken.END_OBJECT){
			String name = jsonParser.getCurrentName();
			if("verb".equals(name)){
				jsonParser.nextToken();
				auditverb.setVerb(jsonParser.getValueAsString());
			}				
			else if("object".equals(name)){
				jsonParser.nextToken();
				
				String idstr = jsonParser.getValueAsString();
				int separatepos = idstr.indexOf(GeneralConstants.KEYS_SEPARATOR);
				String type = idstr.substring(0, separatepos);
				String id = idstr.substring(separatepos + 1);
				InfoId<?> infoid = null;
				if(StringUtils.isNumeric(id)){					
					Identifier idfier = IdKey.valueOfIgnoreCase(type);
					infoid = new InfoId<Long>(idfier, Long.valueOf(id));
				}else{
					Identifier idfier = IdKey.valueOfIgnoreCase(type);
					infoid = new InfoId<String>(idfier, id);
				}
				auditverb.setObjectId(infoid);
			}
			else if("predicates".equals(name)){
				jsonParser.nextToken();
				String key = null;
				String value = null;				
				while(jsonParser.nextToken() != JsonToken.END_OBJECT){
					key = jsonParser.getCurrentName();
					jsonParser.nextToken();
					value = jsonParser.getValueAsString();
					auditverb.addPredicate(key, value);
				}
			}
			else if("start".equals(name)){
				jsonParser.nextToken();
				auditverb.setTimestamp(jsonParser.getValueAsLong());
			}
			else if("elapse".equals(name)){
				jsonParser.nextToken();
				auditverb.setElapsedTime(jsonParser.getValueAsLong());
			}
			
		}
		
		return auditverb;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String,String> beanToMap(Object beanObj){
		
		if(beanObj == null){  
            return null;  
        }          
        Map<String, String> map = null;
        if(beanObj instanceof Map){
        	map = new HashMap<String, String>();
        	map = TransformedMap.decorate(map, TransformerUtils.stringValueTransformer(), TransformerUtils.stringValueTransformer());
        	map.putAll((Map)beanObj);
        	return map;
        }
        
        try {  
        	map = new HashMap<String, String>();  
            BeanInfo beanInfo = Introspector.getBeanInfo(beanObj.getClass());  
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  
            for (PropertyDescriptor property : propertyDescriptors) {  
                String key = property.getName();  
  
                if (!key.equals("class")) {  
                    // get the property
                    Method getter = property.getReadMethod();  
                    Object value = getter.invoke(beanObj);  
  
                    map.put(key, String.valueOf(value));  
                }  
  
            }  
        } catch (Exception e) {  
            LOGGER.error("fail convert bean to map",e);  
        }  
  
        return map;  
	}
	
	/**
	 * convert the predicates map to json string 
	 **/
	public static MutableObject mapToJson(Map<String, String> predicates)throws IOException{
		
		if(predicates == null)
			return new MutableObject("{}");
		
		ByteArrayOutputStream byteAryStream = new ByteArrayOutputStream();		
		JsonGenerator jsonGen = jsonFactory.createGenerator(byteAryStream);
		jsonGen.writeStartObject();
		
		for(Map.Entry<String, String> entry:predicates.entrySet())
			jsonGen.writeStringField(entry.getKey(), entry.getValue());
		
		jsonGen.writeEndObject();
		jsonGen.close();
		
		return new MutableObject(byteAryStream.toString());
	}
}
