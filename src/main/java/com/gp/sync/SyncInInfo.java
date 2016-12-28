package com.gp.sync;

import java.util.Date;

public class SyncInInfo extends SyncMeta{
		
	String syncData;
	
	SyncStatus status;

	Date receiveDate;
	
	Date processDate;

	Date confirmDate;
	
	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
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

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public Date getProcessDate() {
		return processDate;
	}

	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}	
	
}
