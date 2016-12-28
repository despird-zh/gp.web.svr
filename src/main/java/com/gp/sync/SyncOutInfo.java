package com.gp.sync;

import java.util.Date;

public class SyncOutInfo extends SyncMeta{

	String auditId;
	
	String syncData;
	
	SyncStatus status;

	Date createDate;
	
	Date sentDate;
	
	Date confirmDate;
	

	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}

	public String getSyncData() {
		return syncData;
	}

	public void setSyncData(String syncData) {
		this.syncData = syncData;
	}

	public SyncStatus getStatus() {
		return status;
	}

	public void setStatus(SyncStatus status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

}
