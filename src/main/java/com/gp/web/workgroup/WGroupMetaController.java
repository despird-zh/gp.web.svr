package com.gp.web.workgroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.audit.AccessPoint;
import com.gp.common.GeneralConfig;
import com.gp.common.IdKey;
import com.gp.common.Principal;
import com.gp.common.SystemOptions;
import com.gp.core.CabinetFacade;
import com.gp.core.ImageFacade;
import com.gp.core.MeasureFacade;
import com.gp.core.OrgHierFacade;
import com.gp.core.StorageFacade;
import com.gp.core.WorkgroupFacade;
import com.gp.exception.CoreException;
import com.gp.dao.info.CabinetInfo;
import com.gp.info.CombineInfo;
import com.gp.dao.info.ImageInfo;
import com.gp.info.InfoId;
import com.gp.dao.info.OrgHierInfo;
import com.gp.dao.info.StorageInfo;
import com.gp.dao.info.TagInfo;
import com.gp.dao.info.WorkgroupInfo;
import com.gp.dao.info.WorkgroupSumInfo;
import com.gp.svc.info.WorkgroupExtInfo;
import com.gp.util.DateTimeUtils;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.Tag;
import com.gp.web.model.WGroupMetaSummary;
import com.gp.web.model.Workgroup;

@Controller
@RequestMapping("/workgroup")
public class WGroupMetaController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(WGroupMetaController.class);
	
	static String ImagePath = GeneralConfig.getString(SystemOptions.IMAGE_CACHE_PATH);
	
	static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * Retrieve the work group summary information. these information will be the latest measure record
	 * @param wgroup_id the id of work group
	 **/
	@RequestMapping("meta-sum")
	public ModelAndView doMetaSummarySearch(HttpServletRequest request){
		
		ModelAndView mav = getJsonModelView();
		ActionResult result = new ActionResult();
		String widstr = super.readRequestParam("wgroup_id");
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
	
		WGroupMetaSummary wsum = new WGroupMetaSummary();
		try{
			
			if(StringUtils.isBlank(widstr)){
				result.setState(ActionResult.FAIL);
				result.setMessage(getMessage("mesg.prop.miss"));
				
				mav.addAllObjects(result.asMap());
				return mav;
			}
			InfoId<Long> wid = IdKey.WORKGROUP.getInfoId(NumberUtils.toLong(widstr));
			WorkgroupExtInfo cmbinfo = WorkgroupFacade.findWorkgroupExt(accesspoint, principal, wid);
			wsum.setWorkgroupId(cmbinfo.getInfoId().getId());
			wsum.setWorkgroupName(cmbinfo.getWorkgroupName());
			wsum.setAdmin(cmbinfo.getAdmin());
			wsum.setAdminName(cmbinfo.getAdminName());
			wsum.setManager(cmbinfo.getManager());
			wsum.setManagerName(cmbinfo.getManagerName());
			wsum.setDescription(cmbinfo.getDescription());
			// find image path
			Long avatarId = cmbinfo.getAvatarId();
			ImageInfo avatar = ImageFacade.findImage(accesspoint, principal, IdKey.IMAGE.getInfoId(avatarId));
			if(null != avatar){
				wsum.setImagePath("../" + ImagePath + "/" + avatar.getLink());
			}
			wsum.setState(cmbinfo.getState());
			Date since = cmbinfo.getCreateDate();
			if(null != since){
				wsum.setSinceDate(DATE_FORMAT.format(since));
			}
			// find the work group summary information
			WorkgroupSumInfo minfo = MeasureFacade.findWorkgroupSummary(accesspoint, principal, wid);
			if(null != minfo){
				wsum.setMemberSum(minfo.getMemberSummary());
				wsum.setFileSum(minfo.getFileSummary());
				wsum.setPostSum(minfo.getPostSummary());
				wsum.setTaskSum(minfo.getTaskSummary());
			}
			// find the work group tag list
			List<Tag> tags = new ArrayList<Tag>();
			List<TagInfo> tinfos = WorkgroupFacade.findWorkgroupTags(accesspoint, principal, wid);
			for(TagInfo tinfo: tinfos){
				Tag t = new Tag();
				t.setTagColor(tinfo.getTagColor());
				t.setTagName(tinfo.getTagName());
				t.setCategory(tinfo.getCategory());
				
				tags.add(t);
			}
			wsum.setTags(tags);
			result.setData(wsum);
			
			result.setState(ActionResult.SUCCESS);
			result.setMessage(getMessage("mesg.find.wgroup.sum"));
		}catch(CoreException ce){
			
			result.setState(ActionResult.FAIL);
			result.setMessage(ce.getMessage());
		}
		
		result.setData(wsum);
		mav.addAllObjects(result.asMap());
		return mav;
	}

	@RequestMapping("meta-info")
	@Deprecated
	public ModelAndView doFindWorkgroup(HttpServletRequest request){
		
		String wgid = super.readRequestParam("wgroup_id");
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		ActionResult result = new ActionResult();
		ModelAndView mav = getJsonModelView();
		
		if(StringUtils.isBlank(wgid)){
			result.setState(ActionResult.FAIL);
			result.setMessage("request workgroup id not exist");
			
			mav.addAllObjects(result.asMap());
			return mav;
		}
		InfoId<Long> wgroupId = IdKey.WORKGROUP.getInfoId(NumberUtils.toLong(wgid));
		Workgroup wgroup = new Workgroup();
		
		try{
			WorkgroupExtInfo info = WorkgroupFacade.findWorkgroupExt(accesspoint, principal, wgroupId);
			
			wgroup.setWorkgroupId(info.getInfoId().getId());
			wgroup.setWorkgroupName(info.getWorkgroupName());
			wgroup.setAdmin(info.getAdmin());
			wgroup.setDescription(info.getDescription());
			wgroup.setSourceName(info.getSourceName());
			wgroup.setState(info.getState());
			wgroup.setDescription(info.getDescription());
			wgroup.setCreateDate(DateTimeUtils.toYearMonthDay(info.getCreateDate()));
			wgroup.setEntityCode(info.getEntityCode());
			wgroup.setNodeCode(info.getNodeCode());
			
			wgroup.setPublishOn(info.getPublishEnable());
			wgroup.setNetdiskOn(info.getNetdiskEnable());
			wgroup.setTopicOn(info.getPostEnable());
			wgroup.setTaskOn(info.getTaskEnable());
			wgroup.setShareOn(info.getShareEnable());
			wgroup.setLinkOn(info.getLinkEnable());
		
			// storage and organization
			wgroup.setStorageId(info.getStorageId());
			StorageInfo storage = StorageFacade.findStorage(accesspoint, principal, IdKey.STORAGE.getInfoId(info.getStorageId()));
			if(null != storage)
				wgroup.setStorageName(storage.getStorageName());
			wgroup.setOrgId(info.getOrgId());
			OrgHierInfo orghier = OrgHierFacade.findOrgHier(accesspoint, principal, IdKey.ORG_HIER.getInfoId(info.getOrgId()));
			if(null != orghier)
				wgroup.setOrgName(orghier.getOrgName());
			// avatar icon
			Long avatarId = info.getAvatarId();
			ImageInfo avatar = ImageFacade.findImage(accesspoint, principal, IdKey.IMAGE.getInfoId(avatarId));
			if(null != avatar){
				wgroup.setImagePath("../" + ImagePath + "/" + avatar.getLink());
			}
			// cabinet capacity
			Long pubcabId = info.getPublishCabinet();
			CabinetInfo pubcab = CabinetFacade.findCabinet(accesspoint, principal, IdKey.CABINET.getInfoId(pubcabId));
			wgroup.setPublishCapacity((int) (pubcab.getCapacity()/ (1024 * 1024)));
			Long pricabId = info.getNetdiskCabinet();
			CabinetInfo pricab = CabinetFacade.findCabinet(accesspoint, principal, IdKey.CABINET.getInfoId(pricabId));
			wgroup.setNetdiskCapacity((int) (pricab.getCapacity()/ (1024 * 1024)));
			
			List<Tag> tags = new ArrayList<Tag>();
			List<TagInfo> tinfos = WorkgroupFacade.findWorkgroupTags(accesspoint, principal, wgroupId);
			for(TagInfo tinfo: tinfos){
				Tag t = new Tag();
				t.setTagColor(tinfo.getTagColor());
				t.setTagName(tinfo.getTagName());
				t.setCategory(tinfo.getCategory());
				
				tags.add(t);
			}
			
			wgroup.setTags(tags);
			result.setState(ActionResult.SUCCESS);
			result.setData(wgroup);
			
		}catch(CoreException ce){
			result.setState(ActionResult.FAIL);
			result.setMessage(ce.getMessage());
		}

		
		mav.addAllObjects(result.asMap());
		
		return mav;
	}
}
