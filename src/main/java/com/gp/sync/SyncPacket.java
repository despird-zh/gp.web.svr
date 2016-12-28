package com.gp.sync;

/**
 * SyncPack wrap the data to be transfered via network. 
 **/
public class SyncPacket{
	
	/** the meta json data */
	private String metaJsonData = null;
	/** the prop json data */
	private String propJsonData = null;
	/** the bytes data */
	private byte[] bytesData = new byte[0];

	public String getMetaJsonData() {
		return metaJsonData;
	}

	public void setMetaJsonData(String metaJsonData) {
		this.metaJsonData = metaJsonData;
	}

	public String getPropJsonData() {
		return propJsonData;
	}

	public void setPropJsonData(String propJsonData) {
		this.propJsonData = propJsonData;
	}

	public byte[] getBytesData() {
		return bytesData;
	}

	public void setBytesData(byte[] bytesData) {
		this.bytesData = bytesData;
	}	
	
	/**
	 * The synchronization result wrap class 
	 **/
	public static class SyncResult{
		
		String status;
		
		String message;
		
		String code;

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}	
		
	}
}
