package com.gp.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gp.acl.Ace;
import com.gp.acl.Acl;
import com.gp.audit.AccessPoint;
import com.gp.common.Cabinets;
import com.gp.common.GeneralConstants;
import com.gp.common.IdKey;
import com.gp.common.Operations;
import com.gp.common.Principal;
import com.gp.common.ServiceContext;
import com.gp.exception.CoreException;
import com.gp.exception.ServiceException;
import com.gp.dao.info.CabEntryInfo;
import com.gp.dao.info.CabFileInfo;
import com.gp.dao.info.CabFolderInfo;
import com.gp.dao.info.CabVersionInfo;
import com.gp.dao.info.CabinetInfo;
import com.gp.info.InfoId;
import com.gp.dao.info.TagInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.svc.CabinetService;
import com.gp.svc.FileService;
import com.gp.svc.FolderService;
import com.gp.svc.TagService;
import com.gp.svc.CommonService;
import com.gp.svc.FavoriteService;
import com.gp.util.DateTimeUtils;
import com.gp.validate.ValidateMessage;
import com.gp.validate.ValidateUtils;

@Component
public class CabinetFacade {
	
	static Logger LOGGER = LoggerFactory.getLogger(CabinetFacade.class);
	
	private static CabinetService cabinetservice;
	
	private static FolderService folderservice;
	
	private static FileService fileservice;
	
	private static CommonService idservice;
	
	private static TagService tagservice;
	
	private static FavoriteService favservice;
	
	@Autowired
	private CabinetFacade(CabinetService cabinetservice, 
			FolderService folderservice, 
			FileService fileservice,
			CommonService idservice,
			TagService tagservice,
			FavoriteService favservice){
		
		CabinetFacade.cabinetservice = cabinetservice;
		CabinetFacade.fileservice = fileservice;
		CabinetFacade.folderservice = folderservice;
		CabinetFacade.idservice = idservice;
		CabinetFacade.tagservice = tagservice;
		CabinetFacade.favservice = favservice;
	}
	
	/**
	 * Find the Cabinets of an account. include public and private cabinet. 
	 **/
	public static List<CabinetInfo> findPersonalCabinets(AccessPoint accesspoint,
			Principal principal,
			String account) throws CoreException{
		
		List<CabinetInfo> gresult  = null;
	
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_CABINETS)){
			
			gresult = cabinetservice.getCabinets(svcctx, account);

		} catch (ServiceException e)  {

			ContextHelper.stampContext(e, "excp.personal.cabs");

		}finally{
			
			ContextHelper.handleContext();
		}
		
		return gresult;
	}
	
	//
	public static boolean addCabinetFolder(AccessPoint accesspoint,
			Principal principal, CabFolderInfo folderinfo) throws CoreException{

		Boolean gresult  = false;

		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.NEW_FOLDER)){
			
			InfoId<Long> fkey = idservice.generateId(IdKey.CAB_FOLDER, Long.class);
			folderinfo.setInfoId(fkey);
			folderinfo.setSourceId(GeneralConstants.LOCAL_SOURCE);
			folderinfo.setCreateDate(DateTimeUtils.now());
			folderinfo.setCreator(principal.getAccount());
			InfoId<Long> parentkey  = IdKey.CAB_FOLDER.getInfoId(folderinfo.getParentId());
			folderinfo.setState(Cabinets.FolderState.READY.name());
			// check the validation of folder information
			Set<ValidateMessage> vmsg = ValidateUtils.validate(principal.getLocale(), folderinfo);
			if(!CollectionUtils.isEmpty(vmsg)){ // fail pass validation
				CoreException coreexcp = new CoreException(svcctx.getPrincipal().getLocale(), "excp.validate");
				coreexcp.addValidateMessages(vmsg);
				throw coreexcp;
			}
			Acl acl =  Cabinets.getDefaultAcl();
			InfoId<Long> tempid = idservice.generateId(IdKey.CAB_ACL, Long.class);
			acl.setAclId(tempid);
			Collection<Ace> aces = acl.getAllAces();
			for(Ace ace : aces){
				tempid = idservice.generateId(IdKey.CAB_ACE, Long.class);
				ace.setAceId(tempid);
			}
			InfoId<Long> fid = folderservice.newFolder(svcctx, parentkey, folderinfo, acl);
			gresult = InfoId.isValid(fid);
		} catch (ServiceException e)  {

			ContextHelper.stampContext(e, "excp.new.folder");

		}finally{
			
			ContextHelper.handleContext();
		}
		return gresult;
	}
	
	public static List<CabFolderInfo> findCabinetFolders(AccessPoint accesspoint,
			Principal principal, 
			InfoId<Long> cabinetId, 
			InfoId<Long> parentId, 
			String namecond) throws CoreException{
		
		List<CabFolderInfo> gresult  =  null;
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_FOLDERS)){

			gresult = cabinetservice.getCabFolders(svcctx, cabinetId, parentId, namecond);
	
		} catch (ServiceException e)  {
			
			ContextHelper.stampContext(e, "excp.find.folders");
		
		}finally{
			
			ContextHelper.handleContext();
		}
		
		return gresult;
	}
	
	/**
	 * Find all the folders under cabinet with name fuzzy matching
	 * 
	 * @param accesspoint the access point
	 * @param principal the principal
	 * @param cabinetId the id of cabinet
	 * @param parentId the parent folder id, optional
	 * @param filename the name of file
	 * 
	 * @return GeneralResult<List<CabFileInfo>> the matched file information list
	 **/
	public static List<CabFileInfo> findCabinetFiles(AccessPoint accesspoint,
			Principal principal, 
			InfoId<Long> cabinetId, 
			InfoId<Long> parentId, 
			String filename)throws CoreException{
				
		List<CabFileInfo> gresult  =  null;
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_FILES)){
			
			gresult = cabinetservice.getCabFiles(svcctx, cabinetId, parentId, filename);
		
		} catch (ServiceException e)  {

			ContextHelper.stampContext(e, "excp.find.files");

		}finally{
			
			ContextHelper.handleContext();
		}
		
		return gresult;
	}

	/**
	 * Find all the entries under cabinet or folder with name fuzzy matching
	 * 
	 * @param accesspoint the access point
	 * @param principal the principal
	 * @param cabinetId the id of cabinet
	 * @param parentId the parent folder id, optional
	 * @param filename the name of file
	 * 
	 * @return GeneralResult<List<CabFileInfo>> the matched file information list
	 **/
	public static PageWrapper<CabEntryInfo> findCabinetEntries(AccessPoint accesspoint,
			Principal principal, 
			InfoId<Long> cabinetId, 
			InfoId<Long> parentId, 
			String filename,
			PageQuery pquery)throws CoreException{
				
		PageWrapper<CabEntryInfo> gresult  =  null;
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_FILES)){

			gresult = cabinetservice.getCabEntries(svcctx, cabinetId, parentId, filename, pquery);

		} catch (ServiceException e)  {

			ContextHelper.stampContext(e, "excp.find.files");
		}finally{
			
			ContextHelper.handleContext();
		}		
		return gresult;
	}
	
	
	public static CabinetInfo findCabinet(AccessPoint accesspoint,
			Principal principal, InfoId<Long> cabinetId)throws CoreException{
		
		CabinetInfo gresult  =  null;
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_CABINET)){
			
			svcctx.setOperationObject(cabinetId);
			gresult = cabinetservice.getCabinet(svcctx, cabinetId);

		} catch (ServiceException e)  {
		
			ContextHelper.stampContext(e, "excp.find.cab");
		}finally{
			
			ContextHelper.handleContext();
		}		
		return gresult;
	}
	
	/**
	 * Create a file entry in cabinet
	 * 
	 * @param accesspoint the access point of client request
	 * @param principal the principal of current user
	 * @param fileinfo the file to be created
	 * 
	 * @return The General Result that wrap the cabinet file id 
	 **/
	public static InfoId<Long> addCabinetFile(AccessPoint accesspoint,
			Principal principal, CabFileInfo fileinfo)throws CoreException{
		
		InfoId<Long> gresult  =  null;
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.NEW_FILE)){
			
			InfoId<Long> fileid = fileinfo.getInfoId();
			if(!InfoId.isValid(fileid)){
				fileid = idservice.generateId(IdKey.CAB_FILE, Long.class);
				LOGGER.debug("the file id : {}", fileid);
				fileinfo.setInfoId(fileid);
			}
			if(fileinfo.getSourceId() == 0){
				CabinetInfo cinfo = cabinetservice.getCabinet(svcctx, IdKey.CABINET.getInfoId(fileinfo.getCabinetId()));
				fileinfo.setSourceId(cinfo.getSourceId());
			}
			svcctx.setOperationObject(fileid);
			svcctx.addOperationPredicates(fileinfo);
			Acl acl =  Cabinets.getDefaultAcl();
			InfoId<Long> tempid = idservice.generateId(IdKey.CAB_ACL, Long.class);
			acl.setAclId(tempid);
			Collection<Ace> aces = acl.getAllAces();
			for(Ace ace : aces){
				tempid = idservice.generateId(IdKey.CAB_ACE, Long.class);
				ace.setAceId(tempid);
			}
			InfoId<Long> fid = fileservice.newFile(svcctx, fileinfo, acl);
			gresult = fid;
		} catch (ServiceException e)  {
			ContextHelper.stampContext(e, "excp.new.file");
		
		}finally{
			
			ContextHelper.handleContext();
		}		
		return gresult;
	}
	
	/**
	 * find cabinet file information by file id
	 * @param fileid the id of cabinet file 
	 **/
	public static CabFolderInfo findCabinetFolder(AccessPoint accesspoint,
			Principal principal,InfoId<Long> fileid)throws CoreException{
		
		CabFolderInfo gresult  =  null;
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_FOLDER)){
			
			svcctx.setOperationObject(fileid);
			
			gresult = folderservice.getFolder(svcctx, fileid);
		
		} catch (ServiceException e)  {
		
			ContextHelper.stampContext(e, "excp.find.files");

		}finally{
			
			ContextHelper.handleContext();
		}	
		
		return gresult;
	}
	
	/**
	 * find cabinet file information by file id
	 * @param fileid the id of cabinet file 
	 **/
	public static CabFileInfo findCabinetFile(AccessPoint accesspoint,
			Principal principal,InfoId<Long> fileid)throws CoreException{
		
		CabFileInfo gresult  =  null;
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_FILE)){
			
			svcctx.setOperationObject(fileid);
			
			gresult = fileservice.getFile(svcctx, fileid);

			
		} catch (ServiceException e)  {

			ContextHelper.stampContext(e, "excp.find.files");

		}finally{
			
			ContextHelper.handleContext();
		}	
		
		return gresult;
	}
	
	public static Map<InfoId<Long>, Integer> findCabEntriesFavSummary(AccessPoint accesspoint,
			Principal principal, List<InfoId<Long>> entryids)throws CoreException{
		
		Map<InfoId<Long>, Integer> gresult = null;
		
		if(CollectionUtils.isEmpty(entryids)){
			throw new CoreException("excp.entryids.empty");
		}
		List<InfoId<Long>> files = new ArrayList<InfoId<Long>>();
		List<InfoId<Long>> folders = new ArrayList<InfoId<Long>>();
		for(InfoId<Long> id: entryids){
			
			if(IdKey.CAB_FILE.getTable().equals(id.getIdKey())){
				files.add(id);
			}else if(IdKey.CAB_FOLDER.getTable().equals(id.getIdKey())){
				folders.add(id);
			}
		}
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_FAV_SUM)){
			
			Map<InfoId<Long>, Integer> filefavs = CollectionUtils.isEmpty(files)? 
					new HashMap<InfoId<Long>, Integer>() : favservice.getFavFileSummary(svcctx, files);
			Map<InfoId<Long>, Integer> folderfavs = CollectionUtils.isEmpty(folders)? 
					new HashMap<InfoId<Long>, Integer>() : favservice.getFavFolderSummary(svcctx, folders);
			// merge two maps together
			filefavs.putAll(folderfavs);
			gresult = filefavs;
		} catch (ServiceException e)  {

			ContextHelper.stampContext(e);

		}finally{
			
			ContextHelper.handleContext();
		}	
		return gresult;
	}
	
	/**
	 * Find the all the available tags of specified cabinet entry type
	 * @param entrytype the cabinet entry type 
	 **/
	public static List<TagInfo> findCabEntryAvailTags(AccessPoint accesspoint,
			Principal principal, String entrytype)throws CoreException{
		
		List<TagInfo> gresult = null;

		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_TAGS)){

			gresult = tagservice.getTags(svcctx, entrytype);
			
		} catch (ServiceException e)  {

			ContextHelper.stampContext(e,"excp.find.tags");

		}finally{
			
			ContextHelper.handleContext();
		}	
		return gresult;

	}
	
	/**
	 * Find a cabinet entry's tags
	 * @param entryid the id of cabinet entry
	 **/
	public static List<TagInfo> findCabEntryTags(AccessPoint accesspoint,
			Principal principal, InfoId<Long> entryid)throws CoreException{
		
		List<TagInfo> gresult = null;
		if(!InfoId.isValid(entryid)){
			throw new CoreException("excp.entryids.empty");
		}

		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_TAGS)){

			svcctx.setOperationObject(entryid);
			gresult = tagservice.getTags(svcctx, null, entryid);
		} catch (ServiceException e)  {
			
			ContextHelper.stampContext(e, "excp.find.tags");
		
		}finally{
			
			ContextHelper.handleContext();
		}	
		return gresult;

	}
	
	public static Map<InfoId<Long>, Set<TagInfo>> findCabEntriesTags(AccessPoint accesspoint,
			Principal principal, List<InfoId<Long>> entryids)throws CoreException{
		
		Map<InfoId<Long>, Set<TagInfo>> gresult = null;
		if(CollectionUtils.isEmpty(entryids)){
			throw new CoreException("excp.entryids.empty");
		}
		List<InfoId<Long>> files = new ArrayList<InfoId<Long>>();
		List<InfoId<Long>> folders = new ArrayList<InfoId<Long>>();
		for(InfoId<Long> id: entryids){
			
			if(IdKey.CAB_FILE.getTable().equals(id.getIdKey())){
				files.add(id);
			}else if(IdKey.CAB_FOLDER.getTable().equals(id.getIdKey())){
				folders.add(id);
			}
		}
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_TAGS)){
			
			Map<InfoId<Long>, Set<TagInfo>> filetags = tagservice.getTags(svcctx, files);
			Map<InfoId<Long>, Set<TagInfo>> foldertags = tagservice.getTags(svcctx, folders);
			// merge two maps together
			filetags.putAll(foldertags);
			gresult = filetags;
			
		} catch (ServiceException e)  {
			
			ContextHelper.stampContext(e,"excp.find.tags");

		}finally{
			
			ContextHelper.handleContext();
		}	
		return gresult;

	}
	
	/**
	 * Find the cabinet version by file id
	 * @param fileid the id of file
	 **/
	public static List<CabVersionInfo> findCabinetFileVersions(AccessPoint accesspoint,
			Principal principal, InfoId<Long> fileid)throws CoreException{
		
		List<CabVersionInfo> gresult = null;
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_TAGS)){
			
			gresult = fileservice.getVersions(svcctx, fileid);
	
		} catch (ServiceException e)  {
			
			ContextHelper.stampContext(e, "excp.find.versions");
	
		}finally{
			
			ContextHelper.handleContext();
		}	
		return gresult;
		
	}
	
	public static Boolean[] moveCabinetEntries(AccessPoint accesspoint,
			Principal principal, InfoId<Long> destid, InfoId<Long>[] fileids)throws CoreException{
		
		Boolean[] rtv = new Boolean[0];
		if(ArrayUtils.isEmpty(fileids)){
			
			throw new CoreException("excp.entryids.empty");
		}else{
			rtv = new Boolean[fileids.length];
		}
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.MOVE_FILE)){
			
			int cnt = 0;
			for(InfoId<Long> fid : fileids){
				
				if(IdKey.CAB_FILE.getSchema().equals(fid.getIdKey()))
					
					rtv[cnt] = fileservice.moveFile(svcctx, fid, destid);
				else if(IdKey.CAB_FOLDER.getSchema().equals(fid.getIdKey())){
					
					String tgt_path = folderservice.getFolderPath(svcctx, destid);
					String src_path = folderservice.getFolderPath(svcctx, fid);
					if(StringUtils.indexOfIgnoreCase(tgt_path, src_path) < 0)
						rtv[cnt] = folderservice.moveFolder(svcctx, fid, destid);
					else
						rtv[cnt] = false;
				}
				cnt ++ ;
			}
			
		} catch (ServiceException e)  {

			ContextHelper.stampContext(e, "excp.move.entries");
		}finally{
			
			ContextHelper.handleContext();
		}	
		return rtv;
		
	}
	
	
	public static List<InfoId<Long>> copyCabinetEntries(AccessPoint accesspoint,
			Principal principal, InfoId<Long> destid, InfoId<Long>[] fileids)throws CoreException{
		
		List<InfoId<Long>> rtv = new ArrayList<InfoId<Long>>();
		if(ArrayUtils.isEmpty(fileids)){
			throw new CoreException("excp.entryids.empty");
		}
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.COPY_FILE)){
			
			for(InfoId<Long> fid : fileids){
				if(IdKey.CAB_FILE.getSchema().equals(fid.getIdKey())){
					
					InfoId<Long> newId = fileservice.copyFile(svcctx, fid, destid);
					rtv.add(newId);
				}else if(IdKey.CAB_FOLDER.getSchema().equals(fid.getIdKey())){
					
					String tgt_path = folderservice.getFolderPath(svcctx, destid);
					String src_path = folderservice.getFolderPath(svcctx, fid);
					if(StringUtils.indexOfIgnoreCase(tgt_path, src_path) < 0){
						InfoId<Long> newId = folderservice.copyFolder(svcctx, fid, destid);
						rtv.add(newId);
					}else
						rtv.add(IdKey.CAB_FOLDER.getInfoId(-1l));
				}
			}
		
		} catch (ServiceException e)  {

			ContextHelper.stampContext(e, "excp.copy.entries");
		}finally{
			
			ContextHelper.handleContext();
		}	
		return rtv;
		
	}
	
	public static Boolean attachCabEntryTags(AccessPoint accesspoint,
			Principal principal, InfoId<Long> entryid, String ...tags)throws CoreException{
		boolean gresult = false;
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.ATTACH_TAG)){
			
			for(String tag : tags){
				tagservice.attachTag(svcctx, entryid, null, tag);
			}
			gresult = true;
		} catch (ServiceException e)  {

			ContextHelper.stampContext(e, "excp.attach.tag");

		}finally{
			
			ContextHelper.handleContext();
		}	
		return gresult;
	}
	
	public static Boolean detachCabEntryTags(AccessPoint accesspoint,
			Principal principal, InfoId<Long> entryid, String ...tags)throws CoreException{
		
		boolean gresult = false;
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.DETACH_TAG)){
			
			for(String tag : tags){
				tagservice.detachTag(svcctx, entryid, tag);
			}

		} catch (ServiceException e)  {
			
			ContextHelper.stampContext(e, "excp.detach.tag");

		}finally{
			
			ContextHelper.handleContext();
		}	
		return gresult;
	}
}
