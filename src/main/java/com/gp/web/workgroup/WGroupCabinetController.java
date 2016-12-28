package com.gp.web.workgroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.audit.AccessPoint;
import com.gp.common.Cabinets;
import com.gp.common.GeneralConstants;
import com.gp.common.IdKey;
import com.gp.common.Sources;
import com.gp.common.Principal;
import com.gp.core.CabinetFacade;
import com.gp.core.SourceFacade;
import com.gp.core.WorkgroupFacade;
import com.gp.exception.CoreException;
import com.gp.dao.info.CabEntryInfo;
import com.gp.dao.info.CabFileInfo;
import com.gp.dao.info.CabFolderInfo;
import com.gp.info.InfoId;
import com.gp.dao.info.SourceInfo;
import com.gp.dao.info.TagInfo;
import com.gp.dao.info.WorkgroupInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.util.DateTimeUtils;
import com.gp.web.BaseController;
import com.gp.web.model.CabinetItem;
import com.gp.web.model.ItemStat;
import com.gp.web.model.Tag;
/**
 * This controller wrap the operation on cabinet and repository 
 **/
@Controller
@RequestMapping("/workgroup")
public class WGroupCabinetController extends BaseController{
	
	@RequestMapping("publish")
	public ModelAndView doPublishInitial(HttpServletRequest request){
		
		ModelAndView mav = getJspModelView("workgroup/publish");
		String wgid = super.readRequestParam("wgroup_id");
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		InfoId<Long> wkey = IdKey.WORKGROUP.getInfoId(NumberUtils.toLong(wgid));
		WorkgroupInfo gresult = null;
		try{
			gresult = WorkgroupFacade.findWorkgroup(accesspoint, principal, wkey);
		}catch(CoreException ce){
			//
		}
		mav.addObject("wgroup_id",  wgid);
		mav.addObject("cabinet_id",  gresult.getNetdiskCabinet());
		return mav;
	}
	
	/**
	 * Initial the netdisk page 
	 **/
	@RequestMapping("netdisk")
	public ModelAndView doNetdiskInitial(HttpServletRequest request) throws CoreException{
		ModelAndView mav = getJspModelView("workgroup/netdisk");
		String wgid = super.readRequestParam("wgroup_id");
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		
		InfoId<Long> wkey = IdKey.WORKGROUP.getInfoId(NumberUtils.toLong(wgid));
		WorkgroupInfo gresult = WorkgroupFacade.findWorkgroup(accesspoint, principal, wkey);
		
		mav.addObject("wgroup_id",  wgid);
		mav.addObject("folder_id",  GeneralConstants.FOLDER_ROOT);
		mav.addObject("cabinet_id",  gresult.getNetdiskCabinet());
		return mav;
	}
	
	@RequestMapping("netdisk-next")
	public ModelAndView doNetdiskContentNext(HttpServletRequest request) throws CoreException{
		
		ModelAndView mav = super.getJspModelView("workgroup/netdisk-next");

		PageQuery pquery = new PageQuery(4,1);
		String cabinetId = super.readRequestParam("cabinet_id");
		String folderId = super.readRequestParam("folder_id");
		super.readRequestData(pquery);
		
		InfoId<Long> cabid = IdKey.CABINET.getInfoId(NumberUtils.toLong(cabinetId));
		InfoId<Long> folderid = IdKey.CAB_FOLDER.getInfoId(NumberUtils.toLong(folderId));
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		
		PageWrapper<CabEntryInfo> fresult = CabinetFacade.findCabinetEntries(accesspoint, principal, 
				cabid, folderid, "", pquery );
		
		List<CabEntryInfo> entries = fresult.getRows();
		List<CabinetItem> items = new ArrayList<CabinetItem>();
		if(CollectionUtils.isEmpty(entries)){
			mav.addObject("entries", items);
			mav.addObject("hasMore", false);
			mav.addObject("nextPage", 1);
			mav.addObject("cabinetId", cabinetId);
			mav.addObject("folderId", folderId);
			return mav;
		}
		List<InfoId<Long>> ids = new ArrayList<InfoId<Long>>();
		List<String> accounts = new ArrayList<String>();
		for(int i = 0; i< entries.size() ; i++){
			CabinetItem item = new CabinetItem();
			CabEntryInfo entry = entries.get(i);
			
			item.setItemId(entry.getInfoId().getId());			
			item.setAccount(entry.getOwner());
			item.setDescription(entry.getDescription());
			item.setTimeElapse(DateTimeUtils.toDuration(System.currentTimeMillis() - entry.getModifyDate().getTime(), principal.getLocale()));
			
			item.setClassification(entry.getClassification());
			item.setItemName(entry.getEntryName());
			item.setItemType(entry.isFolder()? Cabinets.EntryType.FOLDER.name():Cabinets.EntryType.FILE.name());
			
			if(entry.isFolder()){
				CabFolderInfo fldr = (CabFolderInfo)entry;
				ItemStat childstat = new ItemStat();
				childstat.setStatText(String.valueOf(fldr.getFileCount() + fldr.getFolderCount()));
				childstat.setStatTooltip( fldr.getFolderCount() +" folders " + fldr.getFileCount() + " files " + fldr.getTotalSize() + " bytes");
				item.setChildStat(childstat);
				
				if(StringUtils.isNotBlank(fldr.getProfile())){
					
					item.setPropStat(new ItemStat());
				}
			}else{
				CabFileInfo file = (CabFileInfo)entry;
				ItemStat childstat = new ItemStat();
				childstat.setStatTooltip( file.getSize() + " bytes");
				item.setChildStat(childstat);
				
				ItemStat verstat = new ItemStat();
				verstat.setStatText(file.getVersion());
				verstat.setStatTooltip(file.getVersionLabel());
				item.setVersionStat(verstat);
				
				if(StringUtils.isNotBlank(file.getProfile())){
					
					item.setPropStat(new ItemStat());
				}
			}

			ids.add(entries.get(i).getInfoId());
			accounts.add(entry.getOwner());
			items.add(item);
		}
		// decorate tag information
		Map<InfoId<Long>, Set<TagInfo>> tagmap = CabinetFacade.findCabEntriesTags(accesspoint,
				principal, ids);
		// decorate favorite summary
		Map<InfoId<Long>, Integer> favmap = CabinetFacade.findCabEntriesFavSummary(accesspoint,
				principal, ids);
		Map<String, SourceInfo> srcmap = SourceFacade.findSources(accesspoint,
				principal, accounts);
		// set tags
		for(int i = 0; i< ids.size() ; i++){
			CabinetItem citem = items.get(i);
			// set entry tag
			Set<TagInfo> tinfos = tagmap.get(ids.get(i));
			if(tinfos != null){
				Set<Tag> tags = new HashSet<Tag>();
				for(TagInfo tinfo : tinfos){
					Tag tag = new Tag();
					tag.setCategory(tinfo.getCategory());
					tag.setTagColor(tinfo.getTagColor());
					tag.setTagName(tinfo.getTagName());
					tags.add(tag);
				}
				citem.setTags(tags);
			}
			// set favorite statistics
			Integer favsum = favmap.get(ids.get(i));
			if(favsum == null) favsum = 0;
			ItemStat stat = new ItemStat();
			stat.setStatText(String.valueOf(favsum));
			stat.setStatTooltip(favsum + " People favorite");
			citem.setFavoriteStat(stat);
			
			SourceInfo instinfo = srcmap.get(accounts.get(i));
			if(null != instinfo){
				citem.setExternalOwned(!Sources.LOCAL_INST_ID.equals(instinfo.getInfoId()));
				ItemStat srcstat = new ItemStat();
				srcstat.setStatText("Owned Externally");
				srcstat.setStatTooltip("owned by " + accounts.get(i) + " from " + instinfo.getSourceName());
				
				citem.setSourceStat(srcstat);
			}
		}

		mav.addObject("entries", items);
		mav.addObject("hasMore", items.size() == 4);
		mav.addObject("nextPage", pquery.getPageNumber() + 1);
		mav.addObject("cabinetId", cabinetId);
		mav.addObject("folderId", folderId);
		
		return mav;
	}

}
