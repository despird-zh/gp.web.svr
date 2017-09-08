package com.gp.web.view.workgroup;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.common.AccessPoint;
import com.gp.common.GeneralConfig;
import com.gp.common.GeneralConstants;
import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.common.GPrincipal;
import com.gp.common.SystemOptions;
import com.gp.core.CabinetFacade;
import com.gp.core.ImageFacade;
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
import com.gp.dao.info.WorkgroupInfo;
import com.gp.svc.info.WorkgroupExtInfo;
import com.gp.util.DateTimeUtils;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.Workgroup;
import com.gp.web.util.ExWebUtils;
import com.gp.web.util.ServletUtils;

@Controller
public class WorkgroupEditController extends BaseController{
	
	static Logger LOGGER = LoggerFactory.getLogger(WorkgroupAddController.class);
	
	static String imagePath = GeneralConfig.getString(SystemOptions.IMAGE_CACHE_PATH);
	
	@RequestMapping("workgroup-edit")
	public ModelAndView doPageShow(HttpServletRequest request){
		
		ModelAndView mav = getJspModelView("config/workgroup-edit");
		String wgid = super.readRequestParam("wgroup_id");
		
		mav.addObject("wgroup_id",  wgid);
		
		return mav;
	}
	
	@RequestMapping("workgroup-info")
	public ModelAndView doFindWorkgroup(HttpServletRequest request){
		
		String wgid = super.readRequestParam("wgroup_id");
		GPrincipal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		ActionResult result = new ActionResult();
		ModelAndView mav = getJsonModelView();
		
		if(StringUtils.isBlank(wgid)){
			result = ActionResult.failure("request workgroup id not exist");
			
			mav.addAllObjects(result.asMap());
			return mav;
		}
		InfoId<Long> wgroupId = IdKeys.getInfoId(IdKey.GP_WORKGROUPS, Long.valueOf(wgid));
		
		try{
			WorkgroupExtInfo info = WorkgroupFacade.findWorkgroupExt(accesspoint, principal, wgroupId);
			Workgroup wgroup = new Workgroup();
			
			wgroup.setWorkgroupId(info.getInfoId().getId());
			wgroup.setWorkgroupName(info.getWorkgroupName());
			wgroup.setAdmin(info.getAdmin());
			wgroup.setAdminName(info.getAdminName());
			wgroup.setManager(info.getManager());
			wgroup.setManagerName(info.getManagerName());
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
			
			// sotrage and organiztion
			wgroup.setStorageId(info.getStorageId());
			StorageInfo storage = StorageFacade.findStorage(accesspoint, principal, IdKeys.getInfoId(IdKey.GP_STORAGES,info.getStorageId()));
			if(null != storage)
				wgroup.setStorageName(storage.getStorageName());
			wgroup.setOrgId(info.getOrgId());
			OrgHierInfo orghier = OrgHierFacade.findOrgHier(accesspoint, principal, IdKeys.getInfoId(IdKey.GP_ORG_HIER, info.getOrgId()));
			if(null != orghier)
				wgroup.setOrgName(orghier.getOrgName());
			// avatar icon
			Long avatarId = info.getAvatarId();
			ImageInfo avatar = ImageFacade.findImage(accesspoint, principal, IdKeys.getInfoId(IdKey.GP_IMAGES, avatarId));
			if(null != avatar){
				wgroup.setImagePath("../" + imagePath + "/" + avatar.getLink());
			}
			// cabinet capacity
			Long pubcabId = info.getPublishCabinet();
			CabinetInfo pubcab = CabinetFacade.findCabinet(accesspoint, principal, IdKeys.getInfoId(IdKey.GP_CABINETS, pubcabId));
			wgroup.setPublishCapacity((int) (pubcab.getCapacity()/ (1024 * 1024)));
			Long pricabId = info.getNetdiskCabinet();
			CabinetInfo pricab = CabinetFacade.findCabinet(accesspoint, principal, IdKeys.getInfoId(IdKey.GP_CABINETS, pricabId));
			wgroup.setNetdiskCapacity((int) (pricab.getCapacity()/ (1024 * 1024)));
			
			result = ActionResult.success(getMessage("mesg.find.wgroup"));
			result.setData(wgroup);
			
		}catch(CoreException ce){
			result = super.wrapResult(ce);
		}
		
		
		mav.addAllObjects(result.asMap());
		
		return mav;
	}
	
	@RequestMapping("workgroup-update")
	public ModelAndView doWorkgroupUpdate(HttpServletRequest request){
		
		if(LOGGER.isDebugEnabled())
			ExWebUtils.dumpRequestAttributes(request);
		
		ModelAndView mav = getJsonModelView();
		Workgroup group = new Workgroup();
		readRequestParams( group);
		
		GPrincipal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		ActionResult result = new ActionResult();
		WorkgroupInfo info = new WorkgroupInfo();
		
		InfoId<Long> wgroupId = IdKeys.getInfoId(IdKey.GP_WORKGROUPS, group.getWorkgroupId());
		info.setInfoId(wgroupId);
		info.setSourceId(GeneralConstants.LOCAL_SOURCE);// set local workgroup id
		info.setWorkgroupName(group.getWorkgroupName());
		info.setDescription(group.getDescription());
		info.setState(group.getState());
		info.setAdmin(group.getAdmin());
		info.setManager(group.getManager());
		info.setCreator(principal.getAccount());
		info.setCreateDate(new Date(System.currentTimeMillis()));
		info.setOrgId(group.getOrgId());
		info.setPublishEnable(group.getPublishOn());
		info.setNetdiskEnable(group.getNetdiskOn());
		info.setPostEnable(group.getTopicOn());
		info.setTaskEnable(group.getTaskOn());
		info.setShareEnable(group.getShareOn());
		info.setLinkEnable(group.getLinkOn());
		info.setStorageId(group.getStorageId());
		
		// convert the url into disk path, ignore [..]
		String basePath = ServletUtils.getBaseUrl(request);
		String imagePath = request.getServletContext().getRealPath(group.getImagePath().substring(basePath.length()));
		LOGGER.debug("image file path : {}", imagePath);

		try{
			WorkgroupFacade.updateWorkgroup(accesspoint, principal, 
				info, 
				(long)group.getPublishCapacity()*1024*1024, 
				(long)group.getNetdiskCapacity()*1024*1024,
				imagePath);

			result = ActionResult.success(getMessage("mesg.save.wgroup"));
					
		}catch(CoreException ce){
			result = super.wrapResult(ce);
		}
	
		mav.addAllObjects(result.asMap());

		return mav;

	}
}
