package com.gp.sync;

public class SyncMeta {
	
	public static enum SyncStatus{
		
		pending,
		send,
		process,
		success,
		fail
	}
	
	/**
	 * enumeration of message type 
	 **/
	public static enum SyncType{
		
		SUMMARY,
		TEXT,
		BYTES
	}
	
	/**
	 * the attributes of message meta data 
	 **/
	public static enum MetaAttr{		
		syncid,
		syncbatch,
		syncseq,
		syncname,
		synctype,
		sourcenode,
		sourceentity,
		targetnode,
		targetentity,
		version
	}
	
	/** the message id */
	private String syncId;
	/** the sync batch */
	private String syncBatch;
	/** the sync sequence */
	private int syncSeq;
	/** the sync name */
	private String syncName;
	/** message type */
	private SyncType syncType;
	/** the source node */
	private String sourceNode;
	/** the source entity */
	private String sourceEntity;
	/** the target node */
	private String targetNode;
	/** the target entity */
	private String targetEntity;
	/** the message version */
	private String version;
	
	/**
	 * Get the id of message 
	 **/
	public String getSyncId(){
		
		return this.syncId;
	}
	public void setSyncId(String messageId){
		
		this.syncId = messageId;
	}
		
	public String getSyncBatch() {
		return syncBatch;
	}
	public void setSyncBatch(String syncBatch) {
		this.syncBatch = syncBatch;
	}
	
	public int getSyncSeq() {
		return syncSeq;
	}
	public void setSyncSeq(int syncSeq) {
		this.syncSeq = syncSeq;
	}
	
	public SyncType getSyncType() {
		return syncType;
	}
	public void setSyncType(SyncType syncType) {
		this.syncType = syncType;
	}
	
	public String getSourceNode() {
		return sourceNode;
	}
	public void setSourceNode(String sourceNode) {
		this.sourceNode = sourceNode;
	}
	
	public String getSourceEntity() {
		return sourceEntity;
	}
	public void setSourceEntity(String sourceEntity) {
		this.sourceEntity = sourceEntity;
	}
	
	public String getTargetNode() {
		return targetNode;
	}
	public void setTargetNode(String targetNode) {
		this.targetNode = targetNode;
	}
	
	public String getTargetEntity() {
		return targetEntity;
	}
	public void setTargetEntity(String targetEntity) {
		this.targetEntity = targetEntity;
	}
	
	public String getVersion(){
		
		return this.version;
	}	
	public void setVersion(String version){
		
		this.version = version;
	}
	
	public String getSyncName() {
		return syncName;
	}
	public void setSyncName(String syncName) {
		this.syncName = syncName;
	}
		
	public void copy(SyncMeta sourcemeta){
		
		this.setSourceEntity(sourcemeta.getSourceEntity());
		this.setSourceNode(sourcemeta.getSourceNode());
		this.setTargetEntity(sourcemeta.getTargetEntity());
		this.setTargetNode(sourcemeta.getTargetNode());
		this.setSyncBatch(sourcemeta.getSyncBatch());
		this.setSyncId(sourcemeta.getSyncId());
		this.setSyncName(sourcemeta.getSyncName());
		this.setSyncSeq(sourcemeta.getSyncSeq());
		this.setSyncType(sourcemeta.getSyncType());
		
	}
	
	@Override
	public String toString() {
		return "MessageMeta [messageId=" + syncId + ", synctype=" + syncType + ", sourceNode=" + sourceNode + ", sourceEntity=" + sourceEntity + ", targetNode="
				+ targetNode + ", targetEntity=" + targetEntity + ", version=" + version+ "]";
	}		
	
}
