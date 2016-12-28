package com.gp.web.cabinet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.gp.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gp.audit.AccessPoint;
import com.gp.common.Cabinets;
import com.gp.common.IdKey;
import com.gp.common.Principal;
import com.gp.core.CabinetFacade;
import com.gp.exception.CoreException;
import com.gp.dao.info.CabFileInfo;
import com.gp.dao.info.CabFolderInfo;
import com.gp.dao.info.CabVersionInfo;
import com.gp.info.InfoId;
import com.gp.dao.info.TagInfo;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.Tag;
import com.gp.web.model.Version;

@Controller("cab-entry-ext-ctrl")
@RequestMapping("/cabinet")
public class CabEntryExtController extends BaseController{
	
	public static String TAG_OP_ATTACH = "attach";
	public static String TAG_OP_DETACH = "detach";
	
	@RequestMapping("entry-tags")
	public ModelAndView doFileTagSearch(HttpServletRequest request){
		
		ModelAndView  mav = super.getJsonModelView();
		Long entryid = NumberUtils.toLong(readRequestParam("entry_id"));
		String entryType = readRequestParam("entry_type");
		
		ActionResult actrst = new ActionResult();
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		List<TagInfo> alltags = null;
		List<TagInfo> usedtags = null;
		InfoId<Long> fid = null;
		if(Cabinets.EntryType.FOLDER.name().equals(entryType)){
			
			fid = IdKey.CAB_FOLDER.getInfoId(entryid);

		}else if(Cabinets.EntryType.FILE.name().equals(entryType)){
			
			fid = IdKey.CAB_FILE.getInfoId(entryid);

		}
		
		// reorganize the tag data
		Map<String, List<Tag>> tagmap = new HashMap<String, List<Tag>>();
		try{
			alltags = CabinetFacade.findCabEntryAvailTags(accesspoint, principal, fid.getIdKey());
			usedtags = CabinetFacade.findCabEntryTags(accesspoint, principal, fid);
			Set<String> attatchTag = new HashSet<String>();
			for(TagInfo t : usedtags){
				attatchTag.add(t.getTagName());
			}
			for(TagInfo t : alltags){
				
				Tag tag = new Tag();
				tag.setCategory(t.getCategory());
				tag.setTagName(t.getTagName());
				tag.setTagColor(t.getTagColor());
				tag.setChecked(attatchTag.contains(t.getTagName()));
				
				if(null == tagmap.get(t.getCategory()))
					tagmap.put(t.getCategory(), new LinkedList<Tag>());
				
				tagmap.get(t.getCategory()).add(tag);
			}
		}catch(CoreException ce){
			//
		}
		List<DefaultKeyValue> dkvs = new ArrayList<DefaultKeyValue>();
		for(Map.Entry<String, List<Tag>> entry : tagmap.entrySet()){
			dkvs.add(new DefaultKeyValue(entry));
		}
		
		actrst.setState(ActionResult.SUCCESS);
		actrst.setMessage("Success find tags");
		actrst.setData(dkvs);
		mav.addAllObjects(actrst.asMap());
		
		return mav;
	}
	
	@RequestMapping("tag-update")
	public ModelAndView doTagAttach(HttpServletRequest request){
		
		ModelAndView  mav = super.getJsonModelView();
				
		Long entryid = NumberUtils.toLong(readRequestParam("entry_id"));
		String entryType = readRequestParam("entry_type");
		String tagOps = readRequestParam("tag_ops");
		
		ActionResult actrst = new ActionResult();
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		InfoId<Long> fid = null;
		if(Cabinets.EntryType.FOLDER.name().equals(entryType)){
			
			fid = IdKey.CAB_FOLDER.getInfoId(entryid);

		}else if(Cabinets.EntryType.FILE.name().equals(entryType)){
			
			fid = IdKey.CAB_FILE.getInfoId(entryid);

		}
		Map<String, String> ops  = null;
		try {
			ops = CommonUtils.JSON_MAPPER.readValue(tagOps, new TypeReference<Map<String, String>>(){});
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
		List<String> attachTag = new ArrayList<String>();
		List<String> detachTag = new ArrayList<String>();
		for(Map.Entry<String, String> entry: ops.entrySet()){
			if(TAG_OP_ATTACH.equals(entry.getValue())){
				
				attachTag.add(entry.getKey());
			}else if(TAG_OP_DETACH.equals(entry.getValue())){
				
				detachTag.add(entry.getKey());
			}
		}
		try{
			CabinetFacade.attachCabEntryTags(accesspoint, principal, fid, attachTag.toArray(new String[0]));
			CabinetFacade.detachCabEntryTags(accesspoint, principal, fid, detachTag.toArray(new String[0]));
			actrst.setState(ActionResult.SUCCESS);
			actrst.setMessage(getMessage("mesg.save.tags"));
		}catch(CoreException ce){
			actrst.setState(ActionResult.ERROR);
			actrst.setMessage(ce.getMessage());
		}

		mav.addAllObjects(actrst.asMap());
		return mav;
		
	}
	
	@RequestMapping("entry-properties")
	public ModelAndView doPropertySearch(HttpServletRequest request){
		
		ModelAndView  mav = super.getJspModelView("dialog/entry-properties");
		Long entryid = NumberUtils.toLong(readRequestParam("entry_id"));
		String entryType = readRequestParam("entry_type");
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		Map<String, Object> propmap = null;
		String propstr = null;
		try{
			if(Cabinets.EntryType.FOLDER.name().equals(entryType)){
				InfoId<Long> folderid = IdKey.CAB_FOLDER.getInfoId(entryid);
				CabFolderInfo finfo =  CabinetFacade.findCabinetFolder(accesspoint, principal, folderid);
				propstr = finfo.getProperties();
			}else if(Cabinets.EntryType.FILE.name().equals(entryType)){
				InfoId<Long> fileid = IdKey.CAB_FILE.getInfoId(entryid);
				CabFileInfo finfo = CabinetFacade.findCabinetFile(accesspoint, principal, fileid);
				propstr = finfo.getProperties();
			}
		}catch(CoreException ce){
			//
		}
		propmap = CommonUtils.toMap(propstr, Object.class);
		mav.addObject("propmap", propmap);
		
		return mav;
		
	}
	
	@RequestMapping("file-versions")
	public ModelAndView doVersionSearch(HttpServletRequest request){
		
		ModelAndView  mav = super.getJspModelView("dialog/file-versions");
		
		ActionResult aresult = new ActionResult();
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		Long fid = NumberUtils.toLong(readRequestParam("file_id"));
		InfoId<Long> fileid = IdKey.CAB_FILE.getInfoId(fid);
		
		List<Version> nlist = new ArrayList<Version>();
		try{
			
			List<CabVersionInfo> olist = CabinetFacade.findCabinetFileVersions(accesspoint, 
					principal, 
					fileid);
			
			if(CollectionUtils.isNotEmpty(olist)){
				for(CabVersionInfo vinfo : olist){
					
					Version v = new Version();
					v.setVersion(vinfo.getVersion());
					v.setAuthor(vinfo.getCreator());
					v.setVersionLabel(vinfo.getVersionLabel());
					v.setDescription(vinfo.getDescription());
					nlist.add(v);
				}
			}
			aresult.setData(nlist);
			aresult.setState(ActionResult.SUCCESS);
			aresult.setMessage(getMessage("mesg.find.versions"));
		}catch(CoreException ce){
			
			aresult.setState(ActionResult.FAIL);
			aresult.setMessage(ce.getMessage());
		}
		
		return mav.addAllObjects(aresult.asMap());
		
	}
}
