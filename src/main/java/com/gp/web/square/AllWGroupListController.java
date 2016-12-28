package com.gp.web.square;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriUtils;

import com.gp.audit.AccessPoint;
import com.gp.common.GeneralConfig;
import com.gp.common.Principal;
import com.gp.common.SystemOptions;
import com.gp.core.WorkgroupFacade;
import com.gp.exception.CoreException;
import com.gp.info.CombineInfo;
import com.gp.dao.info.WorkgroupInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.pagination.PaginationInfo;
import com.gp.svc.info.WorkgroupLite;
import com.gp.util.DateTimeUtils;
import com.gp.web.BaseController;
import com.gp.web.model.Workgroup;

@Controller
@RequestMapping("/square")
@Deprecated
public class AllWGroupListController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(AllWGroupListController.class);
	
	static String ImagePath = GeneralConfig.getString(SystemOptions.IMAGE_CACHE_PATH);
	
	@RequestMapping("all-list")
	public ModelAndView doGridInitial(HttpServletRequest request) throws UnsupportedEncodingException{

		ModelAndView mav = getJspModelView("square/all-list");
		
		PageQuery pquery = new PageQuery(12,1);
		this.readRequestData(request, pquery);
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		
		
		List<Workgroup> wgroups = new ArrayList<Workgroup>();
		Boolean hasMore = false;
		Integer nextPage = -1;
		try{
			PageWrapper<WorkgroupLite> gresult = WorkgroupFacade.findLocalWorkgroups(accesspoint, principal, "", null, pquery);
			for(WorkgroupLite winfo : gresult.getRows()){
				
				Workgroup wgroup = new Workgroup();
				wgroup.setWorkgroupId(winfo.getInfoId().getId());
				wgroup.setWorkgroupName(winfo.getWorkgroupName());
				wgroup.setAdmin(winfo.getAdmin());
				wgroup.setAdminName(winfo.getAdminName());
				wgroup.setDescription(winfo.getDescription());
				String imagePath = "../" + ImagePath + "/" + winfo.getImageLink();
				
				wgroup.setImagePath(imagePath);
				wgroup.setState(winfo.getState());
				wgroup.setDescription(winfo.getDescription());
				wgroup.setCreateDate(DateTimeUtils.toYearMonthDay(winfo.getCreateDate()));
				wgroups.add(wgroup);
			}
			
			//PaginationInfo pginfo = gresult.getPagination();
			hasMore = wgroups.size() == pquery.getPageSize();
			nextPage = pquery.getPageNumber() + 1;
		}catch(CoreException ce){
			//
		}
		
		mav.addObject("wgroups", wgroups);
		mav.addObject("hasMore", hasMore);
		mav.addObject("nextPage", nextPage);
		mav.addObject("wgroup_name", "");
		
		mav.addObject("tags", "");
		
		return mav;
	}
	
	
	@RequestMapping("all-dmeo-next")
	public ModelAndView doGridNextLoad(HttpServletRequest request) throws UnsupportedEncodingException{
		
		String pidxstr = this.readRequestParam("pageNumber");
		int pidx = Integer.valueOf(pidxstr);
		PageQuery pquery = new PageQuery(12,1);
		pquery.setPageNumber(pidx);
		ModelAndView mav = getJspModelView("square/all-list-next");
		String wgroup_name = super.readRequestParam("wgroup_name");
		wgroup_name = StringUtils.isBlank(wgroup_name)?"":UriUtils.decode(wgroup_name, "UTF-8");
		
		String[] tags = request.getParameterValues("tags");
		List<String> taglist = (null == tags) ? null : Arrays.asList(tags);
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		

		List<Workgroup> wgroups = new ArrayList<Workgroup>();
		Boolean hasMore = false;
		Integer nextPage = -1;
		try{
			PageWrapper<WorkgroupLite> gresult = WorkgroupFacade.findLocalWorkgroups(accesspoint, principal, wgroup_name, taglist, pquery);
			for(WorkgroupLite winfo : gresult.getRows()){
				
				Workgroup wgroup = new Workgroup();
				wgroup.setWorkgroupId(winfo.getInfoId().getId());
				wgroup.setWorkgroupName(winfo.getWorkgroupName());
				wgroup.setAdmin(winfo.getAdmin());
				wgroup.setAdminName(winfo.getAdminName());
				wgroup.setDescription(winfo.getDescription());
				String imagePath = "../" + ImagePath + "/" + winfo.getImageLink();
				
				wgroup.setImagePath(imagePath);
				wgroup.setState(winfo.getState());
				wgroup.setDescription(winfo.getDescription());
				wgroup.setCreateDate(DateTimeUtils.toYearMonthDay(winfo.getCreateDate()));
				wgroups.add(wgroup);
			}
			
			PaginationInfo pginfo = gresult.getPagination();
			hasMore = pginfo.getNext();
			nextPage = pginfo.getNextPage();
		}catch(CoreException ce){
			//
		}
		
		mav.addObject("wgroups", wgroups);
		mav.addObject("hasMore", hasMore);
		mav.addObject("nextPage", nextPage);
		mav.addObject("wgroup_name", UriUtils.encode(UriUtils.encode(wgroup_name, "UTF-8"), "UTF-8"));
		
		mav.addObject("tags", weaveParameters("tags", tags));

		return mav;
	}

}
