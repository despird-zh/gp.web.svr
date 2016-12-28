package com.gp.web.workgroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
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
import com.gp.core.OrgHierFacade;
import com.gp.core.StorageFacade;
import com.gp.core.WorkgroupFacade;
import com.gp.exception.CoreException;
import com.gp.dao.info.OperLogInfo;
import com.gp.dao.info.CabinetInfo;
import com.gp.info.CombineInfo;
import com.gp.dao.info.GroupMemberInfo;
import com.gp.dao.info.ImageInfo;
import com.gp.info.InfoId;
import com.gp.dao.info.OrgHierInfo;
import com.gp.dao.info.StorageInfo;
import com.gp.dao.info.WorkgroupInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.svc.info.WorkgroupExtInfo;
import com.gp.util.CommonUtils;
import com.gp.util.DateTimeUtils;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.ActivityLog;
import com.gp.web.model.WGroupMember;
import com.gp.web.model.Workgroup;

@Controller
@RequestMapping("/workgroup")
public class WGroupProfileController extends BaseController{
	
	static Logger LOGGER = LoggerFactory.getLogger(WGroupProfileController.class);
	
	static String imagePath = GeneralConfig.getString(SystemOptions.IMAGE_CACHE_PATH);
	
	@RequestMapping("profile")
	public ModelAndView doInitial(HttpServletRequest request){
		
		ModelAndView mav = getJspModelView("workgroup/profile");
		// initial the work group id
		String wgroupid = super.readRequestParam("wgroup_id");
		mav.addObject("wgroup_id",  wgroupid);
		
		return mav;
		
	}

	@RequestMapping("workgroup-info")
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
		InfoId<Long> wgroupId = IdKey.WORKGROUP.getInfoId(Long.valueOf(wgid));
		
		try{
			WorkgroupExtInfo info = WorkgroupFacade.findWorkgroupExt(accesspoint, principal, wgroupId);
			Workgroup wgroup = new Workgroup();
			
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
				wgroup.setImagePath("../" + imagePath + "/" + avatar.getLink());
			}
			// cabinet capacity
			Long pubcabId = info.getPublishCabinet();
			CabinetInfo pubcab = CabinetFacade.findCabinet(accesspoint, principal, IdKey.CABINET.getInfoId(pubcabId));
			wgroup.setPublishCapacity((int) (pubcab.getCapacity()/ (1024 * 1024)));
			Long pricabId = info.getNetdiskCabinet();
			CabinetInfo pricab = CabinetFacade.findCabinet(accesspoint, principal, IdKey.CABINET.getInfoId(pricabId));
			wgroup.setNetdiskCapacity((int) (pricab.getCapacity()/ (1024 * 1024)));
			
			result.setState(ActionResult.SUCCESS);
			result.setData(wgroup);
			
		}catch(CoreException ce){
			result.setState(ActionResult.FAIL);
			result.setMessage(ce.getMessage());
		}
		
		
		mav.addAllObjects(result.asMap());
		
		return mav;
	}
	
	@RequestMapping("members-next")
	public ModelAndView doMembersNext(HttpServletRequest request){
		
		ModelAndView mav = getJspModelView("workgroup/profile-members-next");
		// initial the work group id
		String wgroupid = super.readRequestParam("wgroup_id");
		mav.addObject("wgroup_id",  wgroupid);
		// initial group members, prepare the inifinite setting		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		PageQuery pquery = new PageQuery(12,1);
		this.readRequestData(request, pquery);
		InfoId<Long> wkey = null;
		if(StringUtils.isNotBlank(wgroupid) && CommonUtils.isNumeric(wgroupid)){
			
			wkey = IdKey.WORKGROUP.getInfoId(Long.valueOf(wgroupid));
		}
		List<WGroupMember> list = new ArrayList<WGroupMember>();
		
		Boolean hasMore = false;
		Integer nextPage = -1;
		try{
			PageWrapper<GroupMemberInfo> gresult = WorkgroupFacade.findWorkgroupMembers(accesspoint, principal, wkey, null, null,pquery);
			List<GroupMemberInfo> ulist = gresult.getRows();
			for(GroupMemberInfo info: ulist){
				
				WGroupMember wmember = new WGroupMember();
				wmember.setAccount(info.getAccount());
				wmember.setEmail(info.getEmail());
				wmember.setSourceName(info.getSourceName());
				wmember.setRole(info.getRole());
				wmember.setType(info.getUserType());
				wmember.setUname(info.getUserName());
				
				list.add(wmember);
			}
			//PaginationInfo pginfo = gresult.getPagination();
			hasMore = list.size() == pquery.getPageSize();//pginfo.getNext();
			nextPage = pquery.getPageNumber() + 1;//pginfo.getNextPage();
		}catch(CoreException ce){
			//
		}
		mav.addObject("members", list);
		mav.addObject("hasMore", hasMore);
		mav.addObject("nextPage", nextPage);
		return mav;		
	}
	
	static SimpleDateFormat SDF_DATE = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat SDF_TIME = new SimpleDateFormat("HH:mm:ss");
	
	@RequestMapping("actlogs-next")
	public ModelAndView doActivityLogsNext(HttpServletRequest request){
		
		ModelAndView mav = getJspModelView("workgroup/profile-actlogs-next");
		// initial the work group id
		String wgroupid = super.readRequestParam("wgroup_id");
		mav.addObject("wgroup_id",  wgroupid);
		String tailDateStr = request.getParameter("tailDate");
		Date taildt = null;
		// initial activity logs, prepare the infinite setting		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		PageQuery pquery = new PageQuery(5,1);
		this.readRequestData(request, pquery);
		InfoId<Long> wkey = null;
		if(StringUtils.isNotBlank(wgroupid) && CommonUtils.isNumeric(wgroupid)){
			
			wkey = IdKey.WORKGROUP.getInfoId(Long.valueOf(wgroupid));
		}
		List<ActivityLog> list = new ArrayList<ActivityLog>();
		
		Boolean hasMore = false;
		Integer nextPage = -1;
		try{
			PageWrapper<OperLogInfo> gresult = WorkgroupFacade.findWorkgroupOperLogs(accesspoint, principal, wkey, pquery);
			try {
				taildt = StringUtils.isBlank(tailDateStr) ? SDF_DATE.parse("9999-12-31"):
					SDF_DATE.parse(tailDateStr);
			} catch (ParseException e) {
				LOGGER.error("Fail parse the date",e);
			}
			
			List<OperLogInfo> ulist = gresult.getRows();
			for(OperLogInfo info: ulist){
				
				ActivityLog log = new ActivityLog();
				if(!DateUtils.isSameDay(taildt, info.getOperationTime())){
				// current row log at different day.
					if(DateUtils.isSameDay(info.getOperationTime(), new Date())){
						log.setTimeLabel("Today");
					}
					else{
						log.setTimeLabel(SDF_DATE.format(info.getOperationTime()));
					}
				}
				log.setAccount(info.getAccount());
				log.setActivity(info.getOperation());
				log.setActivityTime(SDF_TIME.format(info.getOperationTime()));
				log.setAuditId(info.getAuditId());
				log.setObjectId(info.getObjectId());
				log.setObjectExcerpt(info.getObjectExcerpt());
				log.setPredicateId(info.getPredicateId());
				log.setPredicateExcerpt(info.getPredicateExcerpt());
				log.setUserName(info.getUserName());
				log.setWorkgroupId(info.getWorkgroupId());
				taildt = info.getOperationTime();
				list.add(log);
			}
			//PaginationInfo pginfo = gresult.getPagination();
			hasMore = list.size() == pquery.getPageSize();
			nextPage = pquery.getPageNumber() +1;
		}catch(CoreException ce){
			//
		}
		mav.addObject("actlogs", list);
		mav.addObject("hasMore", hasMore);
		mav.addObject("nextPage", nextPage);
		mav.addObject("tailDate", SDF_DATE.format(taildt));
		return mav;		
	}
}
