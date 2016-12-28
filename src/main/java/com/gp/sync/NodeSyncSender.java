package com.gp.sync;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.gp.sync.SyncPacket.SyncResult;

public class NodeSyncSender {

	final static int BUFFER_SIZE = 4096; 
	
	CloseableHttpClient httpclient = HttpClients.createDefault();
	
	public void process(){
		
		SyncDataAccessor sda = new SyncDataAccessor();
		
		List<SyncOutInfo> syncoutlist = sda.querySyncOutList("bat001");
		
		for(SyncOutInfo syncout : syncoutlist){
			try{
				SyncPacket syncpacket = SyncDataUtils.getSyncDataPack(syncout);
				SyncResult result = sendSyncPacket(syncpacket);
								
			}catch(Exception e){
				
			}
		}
	}
	
	/**
	 * Send the sync packet data to remote host 
	 **/
	public SyncResult sendSyncPacket(SyncPacket syncpacket) throws ClientProtocolException, IOException{
				
		HttpEntity entity = null;
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		
		builder.addTextBody(BaseSyncServlet.SYNC_META, syncpacket.getMetaJsonData());
		builder.addTextBody(BaseSyncServlet.SYNC_PROP, syncpacket.getPropJsonData());
		ByteArrayBody bytesPart = new ByteArrayBody(syncpacket.getBytesData(), ContentType.APPLICATION_OCTET_STREAM, "sync.dat");
		builder.addPart(BaseSyncServlet.SYNC_BINARY, bytesPart);
		entity = builder.build();
		
		HttpPost httppost = new HttpPost("http://localhost/handler.do");
		httppost.setEntity(entity);
		
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity respentity = response.getEntity();
		InputStream in = respentity.getContent();
		return readSyncResult(in);
	}
	
	/**
	 * Read the synchronize result from http post response.
	 *  
	 **/
	private SyncResult readSyncResult(InputStream in) throws IOException{
		
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] data = new byte[BUFFER_SIZE];  
        int count = -1;  
        while((count = in.read(data,0,BUFFER_SIZE)) != -1)  
            outStream.write(data, 0, count);  
          
        data = null;  
        String json = new String(outStream.toByteArray());
        
        SyncResult result = SyncDataUtils.parseJsonResult(json);
        
        return result;
	}
}
