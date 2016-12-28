package com.gp.core;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gp.audit.AccessPoint;
import com.gp.common.IdKey;
import com.gp.common.Operations;
import com.gp.common.Principal;
import com.gp.common.ServiceContext;
import com.gp.exception.CoreException;
import com.gp.exception.ServiceException;
import com.gp.exception.StorageException;
import com.gp.dao.info.BinaryInfo;
import com.gp.info.InfoId;
import com.gp.dao.info.StorageInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.storage.BinaryManager;
import com.gp.storage.ContentRange;
import com.gp.svc.FileService;
import com.gp.svc.CommonService;
import com.gp.svc.StorageService;
import com.gp.validate.ValidateMessage;
import com.gp.validate.ValidateUtils;

@Component
public class StorageFacade {

	static Logger LOGGER = LoggerFactory.getLogger(StorageFacade.class);

	private static CommonService idService;

	private static StorageService storageService;
	
	private static FileService fileservice;
	
	@Autowired
    private StorageFacade(CommonService idService, StorageService storageService, FileService fileservice) {

    	StorageFacade.idService = idService;
    	StorageFacade.storageService = storageService;
    	StorageFacade.fileservice = fileservice;
    }
    
	/**
	 * This is used for dropdown list paging request 
	 **/
    public static PageWrapper<StorageInfo> findStorages(AccessPoint accesspoint,
    		Principal principal, String storagename, PageQuery pagequery)throws CoreException{
    	
    	PageWrapper<StorageInfo> result = null;
    	
    	try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint, 
    			Operations.FIND_STORAGES)){
			String[][] parms = new String[][]{
				{"storagename",storagename}};				
			Map<?,?> parmap = ArrayUtils.toMap(parms);			
			svcctx.addOperationPredicates(parmap);
			
			result = storageService.getStorages(svcctx, storagename, pagequery);

    	}catch(ServiceException se){
    		ContextHelper.stampContext(se, "excp.find.storages");
    	}finally{
    		
    		ContextHelper.handleContext();
    	}
    	
    	return result;
    }
       
    /**
     * for screen search storage list
     **/
    public static List<StorageInfo> findStorages(AccessPoint accesspoint,
    		Principal principal, String storagename, String[] types, String[] states)throws CoreException{
    	
    	List<StorageInfo> result = null;
    	
    	try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint, 
    			Operations.FIND_STORAGES)){
			String[][] parms = new String[][]{
				{"storagename",storagename}};				
			Map<?,?> parmap = ArrayUtils.toMap(parms);			
			svcctx.addOperationPredicates(parmap);
			
			result = storageService.getStorages(svcctx, storagename, types, states);	
			
    	}catch(ServiceException se){
    		ContextHelper.stampContext(se,"excp.find.storages");
    	}finally{
    		
    		ContextHelper.handleContext();
    	}
    	
    	return result;
    }
    
    /**
     * For create a new storage information
     * 
     * @param accesspoint the access point
     * @param principal the logon principal
     * @param storage the information of target storage
     * @return GeneralResult<InfoId<Integer>> return the storage id
     **/
    public static InfoId<Integer> newStorage(AccessPoint accesspoint,
    		Principal principal, StorageInfo storage)throws CoreException{
		
    	InfoId<Integer> result = null;
		// check the validation of user information
		Set<ValidateMessage> vmsg = ValidateUtils.validate(principal.getLocale(), storage);
		if(!CollectionUtils.isEmpty(vmsg)){ // fail pass validation
			CoreException cexcp = new CoreException(principal.getLocale(), "excp.validate");
			cexcp.addValidateMessages(vmsg);
			throw cexcp;
		}
		try (ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.NEW_STORAGE)){

			svcctx.addOperationPredicates(storage);
			if(!InfoId.isValid(storage.getInfoId())){
				result = idService.generateId(IdKey.STORAGE, Integer.class);
				storage.setInfoId(result);				
				svcctx.setOperationObject(result);
			}else{
				result = storage.getInfoId();		
				svcctx.setOperationObject(storage.getInfoId());
			}
			
			storageService.newStorage(svcctx, storage);

		} catch (ServiceException e)  {
			ContextHelper.stampContext(e, "excp.new.storage");
		}finally{
			
			ContextHelper.handleContext();
		}
    	
    	return result;
    	
    }
    
    /**
     * Find the storage information 
     **/
    public static StorageInfo findStorage(AccessPoint accesspoint,
    		Principal principal, InfoId<Integer> storageid)throws CoreException{
    	
    	StorageInfo gresult = null;
    	
		if(!InfoId.isValid(storageid)){

			CoreException cexcp = new CoreException(principal.getLocale(), "excp.find.storages");
			cexcp.addValidateMessage("storageid", "mesg.prop.miss");
			throw cexcp;
		}
		
		try (ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_STORAGE)){
			
			svcctx.setOperationObject(storageid);
			
			gresult = storageService.getStorage(svcctx, storageid);
		} catch (ServiceException e)  {
			
			ContextHelper.stampContext(e, "excp.find.storages");
		}finally{
			
			ContextHelper.handleContext();
		}
    	return gresult;
    }
    
    public static Boolean saveStorage(AccessPoint accesspoint,
    		Principal principal, StorageInfo storage)throws CoreException{
    	
    	Boolean gresult = false;
    	
		if(!InfoId.isValid(storage.getInfoId())){
			CoreException cexcp = new CoreException(principal.getLocale(), "excp.save.storage");
			cexcp.addValidateMessage("storageid", "mesg.prop.miss");
			throw cexcp;
		}
		
		try (ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.UPDATE_STORAGE)){
			
			svcctx.setOperationObject(storage.getInfoId());
			svcctx.addOperationPredicates(storage);
			storageService.updateStorage(svcctx, storage);
			gresult = true;
		} catch (ServiceException e)  {
			
			ContextHelper.stampContext(e,"excp.save.storage");
		}finally{
			
			ContextHelper.handleContext();
		}
    	return gresult;
    }
    
    public static Boolean removeStorage(AccessPoint accesspoint,
    		Principal principal, InfoId<Integer> storageid)throws CoreException{
    	
    	Boolean gresult = false;
    	
		if(!InfoId.isValid(storageid)){
			CoreException cexcp = new CoreException(principal.getLocale(), "excp.remove.storage");
			cexcp.addValidateMessage("storageid", "mesg.prop.miss");
			throw cexcp;
		}
		
		try (ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.REMOVE_STORAGE)){
			
			svcctx.setOperationObject(storageid);
			gresult = storageService.removeStorage(svcctx, storageid);
			
		} catch (ServiceException e)  {
			
			ContextHelper.stampContext(e, "excp.remove.storage");

		}finally{
			
			ContextHelper.handleContext();
		}
    	return gresult;
    }
    
    /**
     * Fetch the chunk of file binary and write it to stream directly
     * 
     * @param binaryId the id of binary
     * @param contentRange the range of file content
     * @param outputStream the output stream
     * 
     **/
    public static Boolean fetchBinaryChunk(AccessPoint accesspoint, Principal principal,
    		InfoId<Long> binaryId, ContentRange contentRange, OutputStream outputStream) throws CoreException{
		
    	boolean gresult = false;    	
    
    	try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,Operations.FETCH_BIN_CHUNK)){
    	
    		BinaryManager.instance().dumpBinary(binaryId, contentRange, outputStream);
			
			gresult  =true;
		}catch (StorageException e)  {
			
			ContextHelper.stampContext(e, "excp.read.file.chunk");

		}finally{
			
			ContextHelper.handleContext();
		}
    	return gresult;
    	
    }
    
    /**
     * Find the binary stream of whole file and write it to stream directly
     * @param binaryId the id of binary
     * @param outputStream the output stream
     * 
     **/
    public static Boolean fetchBinary(AccessPoint accesspoint, Principal principal,
    		InfoId<Long> binaryId, OutputStream outputStream)throws CoreException{
		
    	Boolean gresult = false;    	

    	try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,Operations.FETCH_BIN)){
			
    		svcctx.setOperationObject(binaryId);
			BinaryManager.instance().dumpBinary(binaryId, outputStream);
	
		}catch (StorageException e)  {
			
			ContextHelper.stampContext(e, "excp.read.file");

		}finally{
			
			ContextHelper.handleContext();
		}
    	return gresult;    	
    }
    
    /**
     * Store the whole content of InputStream into binary content
     * 
     * @param binaryId the binary id
     * @param contentRange the range of target file content
     * @param inputStream the input stream
     * 
     **/
    public static Boolean storeBinaryChunk(AccessPoint accesspoint, Principal principal, 
    		InfoId<Long> binaryId, ContentRange contentRange, InputStream inputStream)throws CoreException{
    	
    	Boolean gresult = false;    	

    	try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,Operations.STORE_BIN_CHUNK)){
    		svcctx.setOperationObject(binaryId);			
			
    		BinaryManager.instance().fillBinary(binaryId, contentRange, inputStream);	
    		gresult = true;
		}catch (StorageException e)  {
			
			ContextHelper.stampContext(e, "excp.write.file.chunk");

		}finally{
			
			ContextHelper.handleContext();
		}
    	return gresult;
	}
    
    /**
     * Store the binary 
     * 
     * @param binaryId the file binary id
     * @param inputStream the input stream
     * 
     **/
    public static Boolean storeBinary(AccessPoint accesspoint, Principal principal, 
    		InfoId<Long> binaryId, InputStream inputStream)throws CoreException{
    	
    	boolean gresult = false;    	

    	try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,Operations.STORE_BIN)){
    		
    		BinaryManager.instance().fillBinary(binaryId, inputStream);			
    		gresult = true;
		}catch (StorageException e)  {
			
			ContextHelper.stampContext(e,"excp.write.file");

		}finally{
			
			ContextHelper.handleContext();
		}
    	return gresult;
	}
    

    public static InfoId<Long> newBinary(AccessPoint accesspoint, Principal principal, 
    		BinaryInfo binfo)throws CoreException{
    	
    	InfoId<Long> result = null;
    	
		try (ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.NEW_BIN)){

			svcctx.addOperationPredicates(binfo);
			// check the validation of user information
			Set<ValidateMessage> vmsg = ValidateUtils.validate(principal.getLocale(), binfo);
			if(!CollectionUtils.isEmpty(vmsg)){ // fail pass validation
				CoreException cexcp = new CoreException(principal.getLocale(), "excp.validate");
				cexcp.addValidateMessages(vmsg);
				throw cexcp;
			}
			
			if(!InfoId.isValid(binfo.getInfoId())){
				result = idService.generateId(IdKey.BINARY, Long.class);
				binfo.setInfoId(result);				
				svcctx.setOperationObject(result);
			}else{
				result = binfo.getInfoId();
				svcctx.setOperationObject(binfo.getInfoId());
			}
			
			storageService.newBinary(svcctx, binfo);
			
		} catch (ServiceException e)  {

			ContextHelper.stampContext(e, "excp.new.bin");

		}finally{
			
			ContextHelper.handleContext();
		}
    	
    	return result;
	}
}
