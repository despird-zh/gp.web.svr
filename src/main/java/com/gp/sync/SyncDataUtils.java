package com.gp.sync;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.gp.sync.SyncField.FieldType;
import com.gp.sync.SyncMeta.MetaAttr;
import com.gp.sync.SyncMeta.SyncType;
import com.gp.sync.SyncPacket.SyncResult;

public class SyncDataUtils {

	/** json Factory instance */
	static JsonFactory jsonFactory = new JsonFactory();

	/**
	 * Convert SyncInInfo into SyncData to be processed on remote target side
	 * @param syncininfo the sync in information
	 * @return SyncData the sync data to be processed
	 **/
	public static SyncData getSyncDataPack(SyncInInfo syncininfo)throws IOException{
		
		SyncMeta syncmeta = new SyncMeta();
		syncmeta.copy(syncininfo);
		
		SyncData syncdata = new SyncData();
		syncdata.setMetadata(syncmeta);
		
		JsonParser jsonParser = jsonFactory.createParser(syncininfo.getSyncData());
		Map<String, Object> props = new HashMap<String, Object>();
		jsonPropDeserialize(jsonParser,syncininfo.getSyncName(), props);
		jsonParser.close();
		
		syncdata.setPropMap(props);
		
		return syncdata;
	}
	
	/**
	 * Convert the SyncData into SyncOutInfo to store into sync-out table
	 * @param syncdata the sync data generate from audit infomation
	 * @return SyncOutInfo the sync out information  
	 **/
	public static SyncOutInfo getSyncOutInfo(SyncData syncdata) throws IOException{
		
		SyncOutInfo syncout = new SyncOutInfo();
		syncout.copy(syncdata.getMetadata());
		ByteArrayOutputStream byteAryStream = new ByteArrayOutputStream();		
		
		JsonGenerator jsonGen = jsonFactory.createGenerator(byteAryStream);		
		jsonPropSerialize(jsonGen, syncout.getSyncName(), syncdata.getPropMap());		
		jsonGen.close();
		
		syncout.setSyncData(byteAryStream.toString());
		return syncout;
	}
	
	/**
	 * Convert sync out table row into SyncData to be synchronized remotely 
	 * @param syncoutinfo the sync out information
	 * @return SyncData the sync data to be transfered.
	 **/
	public static SyncPacket getSyncDataPack(SyncOutInfo syncoutinfo) throws IOException{
		
		SyncPacket syncdata = new SyncPacket();
		ByteArrayOutputStream byteAryStream = new ByteArrayOutputStream();		
		
		JsonGenerator jsonGen = jsonFactory.createGenerator(byteAryStream);		
		jsonMetaSerialize(jsonGen, syncoutinfo);		
		jsonGen.close();	
		
		syncdata.setMetaJsonData(byteAryStream.toString());
		syncdata.setPropJsonData(syncoutinfo.getSyncData());
		
		return syncdata;
	}
	
	/**
	 * Convert SyncData into sync in information to store received data
	 * @param syncpack the data received
	 * @return SyncInInfo the sync in information 
	 **/
	public static SyncInInfo getSyncInInfo(SyncPacket syncpack) throws IOException{
		
		JsonParser jsonParser = jsonFactory.createParser(syncpack.getMetaJsonData());
		SyncInInfo syncin = new SyncInInfo();
		jsonMetaDeserialize(jsonParser, syncin);
		jsonParser.close();
				
		syncin.setSyncData(syncpack.getPropJsonData());
		
		return syncin;
	}
	
	public static String generateJsonResult(SyncResult syncresult)throws IOException{
		
		ByteArrayOutputStream byteAryStream = new ByteArrayOutputStream();		
		
		JsonGenerator jsonGenerator = jsonFactory.createGenerator(byteAryStream);		
		jsonGenerator.writeStartObject();		
		jsonGenerator.writeStringField("status", syncresult.getStatus());
		jsonGenerator.writeStringField("code", syncresult.getCode());
		jsonGenerator.writeStringField("message", syncresult.getMessage());
		jsonGenerator.writeEndObject();
		jsonGenerator.close();
		
		return byteAryStream.toString();
	}
	
	public static SyncResult parseJsonResult(String jsonStr)throws IOException{
		
		JsonParser jsonParser = jsonFactory.createParser(jsonStr);
		SyncResult result = new SyncResult();
		while(jsonParser.nextToken() != JsonToken.END_OBJECT){
			String name = jsonParser.getCurrentName();
			
			if(null == name) continue;

			else if("status".equals(name)){
				
				jsonParser.nextToken();
				result.setStatus(jsonParser.getValueAsString());
				
			}else if("code".equals(name)){
				
				jsonParser.nextToken();
				result.setCode(jsonParser.getValueAsString());
			}else if("message".equals(name)){
				
				jsonParser.nextToken();
				result.setMessage(jsonParser.getValueAsString());
			}else{
				
				continue;
			}
		}
		jsonParser.close();
		
		return result;
	}
	
	/**
	 * Get the sync prop information
	 * 
	 * @param syncname the sync name
	 * @param propStr the property string
	 * 
	 * @return Map<?,?> the map object 
	 **/
	public static Map<String, Object> getSyncProp(String syncname, String propStr) throws IOException{
		
		JsonParser jsonParser = jsonFactory.createParser(propStr);
		Map<String, Object> props = new HashMap<String, Object>();
		jsonPropDeserialize(jsonParser,syncname, props);
		jsonParser.close();
		
		return props;
	}
		
	private static void jsonPropSerialize(JsonGenerator jsonGenerator, String syncname, Map<String, Object> props) throws IOException{
		
		List<SyncField> flds = SyncManager.getSyncFields(syncname);
		
		jsonGenerator.writeStartObject();	
		for(SyncField field : flds){
			
			Object value = props.get(field.getName());
			FieldType type = field.getType();
			
			switch(type){
			case STRING :
				jsonGenerator.writeStringField(field.getName(), (String)value);
				break;
			case INT :
				jsonGenerator.writeNumberField(field.getName(), (int)value);
				break;
			case FLOAT:
				jsonGenerator.writeNumberField(field.getName(), (float)value);
				break;
			case LONG:
				jsonGenerator.writeNumberField(field.getName(), (long)value);
				break;
			case DOUBLE:
				jsonGenerator.writeNumberField(field.getName(), (double)value);
				break;
			case DATE:
				long time = ((Date)value).getTime();				
				jsonGenerator.writeNumberField(field.getName(), time);
				break;
			case BOOLEAN:
				jsonGenerator.writeObjectField(field.getName(), (Boolean)value);
				break;
			default:
				break;
			}
		}
		
		jsonGenerator.writeEndObject();			 
	}

	private static void jsonPropDeserialize(JsonParser jsonParser, String syncname, Map<String, Object> props) throws IOException{
		
		TypeReference<HashMap<String,Object>> typeRef = 
				new TypeReference<HashMap<String,Object>>(){};
		props = jsonParser.readValueAs(typeRef);
		
		// prepare the property map 
		if(props == null)
			props = new HashMap<String, Object>();
		else
			props.clear();
		
		List<SyncField> flds = SyncManager.getSyncFields(syncname);
		SyncField _tempFld = new SyncField();
		while(jsonParser.nextToken() != JsonToken.END_OBJECT){
			String name = jsonParser.getCurrentName();
			
			if(null == name) continue;
			_tempFld.setName(name);
			int idx = flds.indexOf(_tempFld);
			
			if(idx == -1) continue;
			
			SyncField field = flds.get(idx);
			FieldType type = field.getType();
			switch(type){
			case STRING :
				jsonParser.nextToken();
				props.put(field.getName(), jsonParser.getValueAsString());
				break;
			case INT :
				jsonParser.nextToken();
				props.put(field.getName(), jsonParser.getValueAsInt());
				break;
			case FLOAT:
				jsonParser.nextToken();
				props.put(field.getName(), jsonParser.getValueAsDouble());
				break;
			case LONG:
				jsonParser.nextToken();
				props.put(field.getName(), jsonParser.getValueAsLong());
				break;
			case DOUBLE:
				jsonParser.nextToken();
				props.put(field.getName(), jsonParser.getValueAsDouble());
				break;
			case DATE:
				jsonParser.nextToken();
				long time = jsonParser.getValueAsLong();				
				props.put(field.getName(), new Date(time));
				break;
			case BOOLEAN:
				jsonParser.nextToken();
				props.put(field.getName(), jsonParser.getValueAsBoolean());
				break;
			default:
				break;			
			}
		}
	}	

	/**
	 * Read the SyncMeta via string metadata
	 * @param metadata the meta data
	 * @return SyncMeta 
	 **/
	public static SyncMeta getSyncMeta(String metadata) throws IOException{
		
		SyncMeta meta = new SyncMeta();
		JsonParser jsonParser = jsonFactory.createParser(metadata);
		
		jsonMetaDeserialize(jsonParser, meta);
		jsonParser.close();
		
		return meta;
	}
	
	/**
	 * Get the serialized byte buffer via generator, the encoding is decided by jsonGenerator
	 * @param jsonGenerator the generator should be initialized outside with a outputstream
	 **/
	private static void jsonMetaSerialize(JsonGenerator jsonGenerator, SyncMeta msgmeta)throws IOException{

		jsonGenerator.writeStartObject();		
		jsonGenerator.writeStringField(MetaAttr.syncid.toString(), msgmeta.getSyncId());
		jsonGenerator.writeStringField(MetaAttr.syncbatch.toString(), msgmeta.getSyncBatch());
		jsonGenerator.writeNumberField(MetaAttr.syncseq.toString(), msgmeta.getSyncSeq());
		jsonGenerator.writeStringField(MetaAttr.syncname.toString(), msgmeta.getSyncName());
		jsonGenerator.writeStringField(MetaAttr.synctype.toString(), msgmeta.getSyncType().toString());
		jsonGenerator.writeStringField(MetaAttr.sourceentity.toString(), msgmeta.getSourceEntity());
		jsonGenerator.writeStringField(MetaAttr.sourcenode.toString(), msgmeta.getSourceNode());
		jsonGenerator.writeStringField(MetaAttr.targetentity.toString(), msgmeta.getTargetEntity());
		jsonGenerator.writeStringField(MetaAttr.targetnode.toString(), msgmeta.getTargetNode());
		jsonGenerator.writeStringField(MetaAttr.version.toString(), msgmeta.getVersion());
		jsonGenerator.writeEndObject();
		
	}
	
	/**
	 * Deserialize the byte buffer embedded in parser
	 * @param jsonParser the parser should be initialized outside with content.
	 **/
	private static void jsonMetaDeserialize(JsonParser jsonParser,SyncMeta msgmeta) throws IOException{
		
		while(jsonParser.nextToken() != JsonToken.END_OBJECT){
			String name = jsonParser.getCurrentName();
			
			if(null == name) continue;
			
			MetaAttr attrname = MetaAttr.valueOf(name);
			switch(attrname){
				case syncid:
					jsonParser.nextToken();
					msgmeta.setSyncId(jsonParser.getValueAsString());
					break;

				case syncbatch:
					jsonParser.nextToken();
					msgmeta.setSyncBatch(jsonParser.getValueAsString());
					break;

				case syncseq:
					jsonParser.nextToken();
					msgmeta.setSyncSeq(jsonParser.getValueAsInt());
					break;
					
				case syncname:
					jsonParser.nextToken();
					msgmeta.setSyncName(jsonParser.getValueAsString());
					break;
					
				case synctype:
					jsonParser.nextToken();
					msgmeta.setSyncType(SyncType.valueOf(jsonParser.getValueAsString()));
					break;
					
				case sourceentity:
					jsonParser.nextToken();
					msgmeta.setSourceEntity(jsonParser.getValueAsString());
					break;
					
				case sourcenode:
					jsonParser.nextToken();
					msgmeta.setSourceNode(jsonParser.getValueAsString());
					break;
										
				case targetentity:
					jsonParser.nextToken();
					msgmeta.setTargetEntity(jsonParser.getValueAsString());
					break;
					
				case targetnode:
					jsonParser.nextToken();
					msgmeta.setTargetNode(jsonParser.getValueAsString());
					break;
					
				case version:
					jsonParser.nextToken();
					msgmeta.setVersion(jsonParser.getValueAsString());
					break;
					
				default:
					break;
			}
		}
	}
}
