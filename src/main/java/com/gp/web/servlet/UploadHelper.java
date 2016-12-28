package com.gp.web.servlet;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

import com.gp.audit.AccessPoint;
import com.gp.common.Cabinets;
import com.gp.common.GeneralConstants;
import com.gp.common.IdKey;
import com.gp.common.Principal;
import com.gp.common.SpringContextUtil;
import com.gp.core.CabinetFacade;
import com.gp.core.StorageFacade;
import com.gp.exception.CoreException;
import com.gp.dao.info.BinaryInfo;
import com.gp.dao.info.CabFileInfo;
import com.gp.dao.info.CabinetInfo;
import com.gp.info.InfoId;
import com.gp.storage.ContentRange;
import com.gp.web.BaseController;

/**
 * Helper class to facilitate the transfer servlet upload&download processing
 * 
 * @author gary diao
 * @version 0.1 2015-12-23
 **/
class UploadHelper {

	static Logger LOGGER = LoggerFactory.getLogger(UploadHelper.class);
	
	static Cache transferCache = null;
	
	static Lock lock = new ReentrantLock(); 
	
	static Cache getTransferCache(){
		// initialize the transfer cache
		if(transferCache == null)
			transferCache = SpringContextUtil.getSpringBean("fileTransferCache", Cache.class);
			
		return transferCache;
	}
	
	/**
	 * Save the upload file 
	 **/
	static void storeFile(String storepath, PartMeta filepart)throws CoreException{
		
		InputStream inputStream = null;

		try {
			inputStream = filepart.getContent();
			TransferCacheInfo tsfinfo = getCachedInfo(filepart.getFileId());
			tsfinfo.setCabinetId(filepart.getCabinetId());
			// create file and binary 
			createCabFile(tsfinfo, filepart);
			tsfinfo.getRanges().add(filepart.getContentRange());
			// save binary
			saveBinary(tsfinfo.getBinaryId(), filepart);
			LOGGER.debug("single file upload complete");

		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}
	
	/**
	 * Save upload file chunk 
	 **/
	static void storeFileChunk(String storepath, PartMeta filepart)throws CoreException{
		
		InputStream inputStream = null;
		RandomAccessFile file = null;
		try {
			TransferCacheInfo tsfinfo = getCachedInfo(filepart.getFileId());
			tsfinfo.setCabinetId(filepart.getCabinetId());
			inputStream = filepart.getContent();			
//			/** -- Save file to a temporary file -- */
//			try{
//				String tempfilepath = storepath + filepart.getFileId() +'.'+ filepart.getExtension();
//				file = new RandomAccessFile(tempfilepath, "rw");
//				file.seek(filepart.getContentRange().getStartPos());
//				int read = 0;
//				byte[] bytes = new byte[4096];
//				while ((read = inputStream.read(bytes)) != -1) {
//					file.write(bytes, 0, read);
//				}
//			}catch(Exception e){
//				e.printStackTrace();
//			}
			// when receive first chunk then create file in cabinet
			if(filepart.getContentRange().getStartPos() == 0){
				
				createCabFile(tsfinfo, filepart);
			}
			
			tsfinfo.getRanges().add(filepart.getContentRange());
			
			saveBinaryChunk(tsfinfo.getBinaryId(), filepart);
			
		} finally {
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(file);
		}
	}
	
	/**
	 * The transfer information is cached, in order to support multiple chunk upload, 
	 * add an ReentrantLock to make chunks cache per file is thread safe.
	 **/
	private static TransferCacheInfo getCachedInfo(String transferUid){
		lock.lock();  
		try {   
			Cache rangesCache = getTransferCache();
			TransferCacheInfo tsfinfo = rangesCache.get(transferUid, TransferCacheInfo.class);
			if(null == tsfinfo){
				tsfinfo = new TransferCacheInfo();
				tsfinfo.setClientUid(transferUid);
				rangesCache.put(transferUid, tsfinfo);
			}
			return tsfinfo;
		}  
		finally {  
		  lock.unlock();   
		}
	}

	/**
	 * Create file and binary records, the cabinet file id and binary id is saved in TransferCacheInfo
	 * 
	 * @return InfoId<Long> the id of cabinet file
	 **/
	private static InfoId<Long> createCabFile(TransferCacheInfo tsfinfo, PartMeta filepart) throws CoreException{
		
		Principal principal = BaseController.getPrincipal();
		AccessPoint accesspoint = filepart.getAccessPoint();

		long cabinetId = Long.valueOf(filepart.getCabinetId());
		InfoId<Long> cabid = IdKey.CABINET.getInfoId(cabinetId);
		CabinetInfo cabinfo = CabinetFacade.findCabinet(accesspoint, principal, cabid);

		BinaryInfo binfo = new BinaryInfo();
		binfo.setSourceId(cabinfo.getSourceId());		
		binfo.setStorageId(cabinfo.getStorageId());		
		binfo.setFormat(filepart.getExtension());
		binfo.setSize(filepart.getContentRange().getFileSize());
		binfo.setState(Cabinets.FileState.BLANK.name());
		binfo.setCreator(principal.getAccount());
		binfo.setCreateDate(new Date());
		
		InfoId<Long> binaryId = StorageFacade.newBinary(accesspoint, principal, binfo);
		tsfinfo.setBinaryId(binaryId);// save the binary id
		
		CabFileInfo fileinfo = new CabFileInfo();

		fileinfo.setCabinetId(cabinetId);
		fileinfo.setParentId(GeneralConstants.FOLDER_ROOT);
		fileinfo.setClassification("secret");
		fileinfo.setCommentOn(false);
		fileinfo.setBinaryId(binaryId.getId()); // Set the binary id
		fileinfo.setFormat(filepart.getExtension());
		fileinfo.setOwner(principal.getAccount());
		fileinfo.setState(Cabinets.FileState.BLANK.name());
		fileinfo.setEntryName(filepart.getName());
		fileinfo.setVersion("1.0");
		fileinfo.setCreator(principal.getAccount());
		fileinfo.setCreateDate(new Date());
		
		InfoId<Long> fileId = CabinetFacade.addCabinetFile(accesspoint, principal, fileinfo);
		tsfinfo.setFileId(fileId); // save the file id
		
		return fileId;
	}
	
	/**
	 * Save file binary into storage
	 * 
	 * @param binaryId the id of file binary
	 * @param inputStream the input stream of upload file
	 * @param request the http request to extract access point and principal. 
	 **/
	public static void saveBinary(InfoId<Long> binaryId, PartMeta filemeta)throws CoreException{
		
		Principal principal = BaseController.getPrincipal();
		AccessPoint accesspoint = filemeta.getAccessPoint();
		StorageFacade.storeBinary(accesspoint, principal, binaryId, filemeta.getContent());
	}
	
	/**
	 * Save file binary into storage
	 * 
	 * @param binaryId the id of file binary
	 * @param filemeta the content range of upload file
	 **/
	public static void saveBinaryChunk(InfoId<Long> binaryId, PartMeta filemeta)throws CoreException{
		
		Principal principal = BaseController.getPrincipal();

		StorageFacade.storeBinaryChunk(filemeta.getAccessPoint(),
				principal,
				binaryId,
				filemeta.getContentRange(),
				filemeta.getContent());
	}
	
	/**
	 * Class to hold the transfer chunk information
	 *  
	 **/
	public static class TransferCacheInfo{
		/** cabinet id */
		private String cabinetId;
		/** client side uid */
		private String clientUid;
		/** file id */
		private InfoId<Long> fileId;
		/** binaryId */
		private InfoId<Long> binaryId;
		/** range setting of chunks */
		Set<ContentRange> ranges = new HashSet<ContentRange>();

		public String getClientUid() {
			return clientUid;
		}

		public void setClientUid(String clientUid) {
			this.clientUid = clientUid;
		}

		public InfoId<Long> getFileId() {
			return fileId;
		}

		public void setFileId(InfoId<Long> fileId) {
			this.fileId = fileId;
		}

		public InfoId<Long> getBinaryId() {
			return binaryId;
		}

		public void setBinaryId(InfoId<Long> binaryId) {
			this.binaryId = binaryId;
		}

		public Set<ContentRange> getRanges() {
			return ranges;
		}

		public void setRanges(Set<ContentRange> ranges) {
			this.ranges = ranges;
		}

		/**
		 * Check if the all chunks are complete 
		 **/
		boolean isComplete(){
				
				long length = 0l;
				long filesize = 0l;
				for(ContentRange range : this.ranges){
					length += range.getRangeLength();
					if(filesize == 0)
						filesize = range.getFileSize();
				}
				
				return filesize == length;
		}

		public String getCabinetId() {
			return cabinetId;
		}

		public void setCabinetId(String cabinetId) {
			this.cabinetId = cabinetId;
		}
	}
}
