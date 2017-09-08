package com.gp.web.view.master;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.common.AccessPoint;
import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.common.GPrincipal;
import com.gp.core.DictionaryFacade;
import com.gp.exception.CoreException;
import com.gp.dao.info.DictionaryInfo;
import com.gp.info.InfoId;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.DictEntry;

@Controller("ga-dict-ctlr")
@RequestMapping("/ga")
public class DictEntriesController  extends BaseController{

	static SimpleDateFormat MDF_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@RequestMapping("dict-list")
	public ModelAndView doInitial(){
		
		return getJspModelView("ga/master/dict-list");
	}
	
	@RequestMapping("dict-search")
	public ModelAndView doEntriesSearch(HttpServletRequest request){
		
		ModelAndView mav = super.getJsonModelView();
		GPrincipal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		ActionResult ars = new ActionResult();
		List<DictEntry> list = new ArrayList<DictEntry>();

		try{
			List<DictionaryInfo> gresult = DictionaryFacade.findDictEntries(accesspoint, principal, "", "");
			for(DictionaryInfo info: gresult){
				DictEntry de = new DictEntry();
				de.setEntryId(info.getInfoId().getId());
				de.setEntryKey(info.getKey());
				de.setGroupKey(info.getGroup());
				de.setEntryValue(info.getValue());
				//de.setLabel(info.getLabel());
				//de.setLanguage(info.g);
				de.setModifier(info.getModifier());
				de.setModifyDate(MDF_DATE_FORMAT.format(info.getModifyDate()));
				
				list.add(de);
			}
			ars = ActionResult.success(getMessage("mesg.find.sysopts"));
			ars.setData(list);
		}catch(CoreException ce){
			ars = super.wrapResult(ce);
		}
		
		mav.addAllObjects(ars.asMap());
		return mav;
	}
	
	@RequestMapping("dict-save")
	public ModelAndView doEntrySave(HttpServletRequest request){
		
		ModelAndView mav = super.getJsonModelView();
		ActionResult result = new ActionResult();
		DictEntry dentry = new DictEntry();
		GPrincipal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		super.readRequestParams(dentry);// read request data.
		
		DictionaryInfo dinfo = new DictionaryInfo();
		InfoId<Long> did = IdKeys.getInfoId(IdKey.GP_DICTIONARY, dentry.getEntryId());
		dinfo.setInfoId(did);
		dinfo.setKey(dentry.getEntryKey());
		dinfo.setValue(dentry.getEntryValue());
		dinfo.setGroup(dentry.getGroupKey());
		//dinfo.setLabel(dentry.getLabel());
		//dinfo.setDefaultLang(dentry.getLanguage());

		try{
			Boolean gresult = DictionaryFacade.saveDictEntry(accesspoint, principal, dinfo);
			
			result = ActionResult.success(getMessage("mesg.save.dict"));
			result.setData(gresult);
		}catch(CoreException ce){
			result = super.wrapResult(ce);
		}
		
		mav.addAllObjects(result.asMap());
		return mav;
	}
	
}
