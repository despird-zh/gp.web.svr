package com.gp.web.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gp.common.AccessPoint;
import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.common.Principal;
import com.gp.core.DictionaryFacade;
import com.gp.dao.DictionaryDAO;
import com.gp.dao.info.DictionaryInfo;
import com.gp.exception.CoreException;
import com.gp.info.FlatColLocator;
import com.gp.info.FlatColumn;
import com.gp.info.InfoId;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.DictEntry;
import com.gp.web.servlet.ServiceFilter;

@Controller
@RequestMapping(ServiceFilter.FILTER_PREFIX)
public class DictionaryController extends BaseController{
	
	static SimpleDateFormat MDF_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	static Logger LOGGER = LoggerFactory.getLogger(DictionaryController.class);
	
	@RequestMapping(value = "dicts-query.do",
	method = RequestMethod.POST,
    consumes = {"text/plain", "application/*"})
	public ModelAndView doEntriesSearch(@RequestBody String payload){
		
		ModelAndView mav = super.getJsonModelView();
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		Map<String, String> paramap = super.readRequestJson(payload);
		String language = paramap.get("language");
		ActionResult ars = new ActionResult();
		List<DictEntry> list = new ArrayList<DictEntry>();

		try{
			List<DictionaryInfo> gresult = DictionaryFacade.findDictEntries(accesspoint, principal, 
					paramap.get("group"), 
					paramap.get("search"));
			
			FlatColumn lblcol = DictionaryDAO.getFlatColumn(language);
			
			for(DictionaryInfo info: gresult){
				DictEntry de = new DictEntry();
				de.setEntryId(info.getInfoId().getId());
				de.setEntryKey(info.getKey());
				de.setGroupKey(info.getGroup());
				de.setEntryValue(info.getValue());
				de.setLabel(info.getLabel(lblcol));
				de.setLanguage(language);
				de.setModifier(info.getModifier());
				de.setModifyDate(MDF_DATE_FORMAT.format(info.getModifyDate()));
				
				list.add(de);
			}
			ars = ActionResult.success(getMessage("mesg.find.dicts"));
			ars.setData(list);
		}catch(CoreException ce){
			ars = super.wrapResult(ce);
		}
		
		mav.addAllObjects(ars.asMap());
		return mav;
	}
	
	@RequestMapping(value = "dict-save.do",
	method = RequestMethod.POST,
    consumes = {"text/plain", "application/*"})
	public ModelAndView doEntrySave(@RequestBody String payload){
		
		ModelAndView mav = super.getJsonModelView();
		ActionResult result = new ActionResult();

		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);

		DictEntry dentry = super.readRequestBody(payload, DictEntry.class);
		
		DictionaryInfo dinfo = new DictionaryInfo();
		InfoId<Long> did = IdKeys.getInfoId(IdKey.DICTIONARY,dentry.getEntryId());
		
		FlatColumn lblcol = DictionaryDAO.getFlatColumn(dentry.getLanguage());
		
		dinfo.setInfoId(did);
		dinfo.setKey(dentry.getEntryKey());
		dinfo.setValue(dentry.getEntryValue());
		dinfo.setGroup(dentry.getGroupKey());

		Map<FlatColLocator, String> labels = new HashMap<FlatColLocator, String>();
		labels.put(lblcol, dentry.getLabel());
		dinfo.setLabelMap(labels);
		
		try{
			DictionaryFacade.saveDictEntry(accesspoint, principal, dinfo);
			result = ActionResult.success(getMessage("mesg.save.dict"));
		}catch(CoreException ce){
			result = super.wrapResult(ce);
		}
		
		mav.addAllObjects(result.asMap());
		return mav;
	}
}
