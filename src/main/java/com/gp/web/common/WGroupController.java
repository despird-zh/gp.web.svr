package com.gp.web.common;

import com.gp.audit.AccessPoint;
import com.gp.common.IdKey;
import com.gp.common.Principal;
import com.gp.core.WorkgroupFacade;
import com.gp.dao.info.GroupMemberInfo;
import com.gp.dao.info.WorkgroupInfo;
import com.gp.exception.CoreException;
import com.gp.info.CombineInfo;
import com.gp.info.InfoId;
import com.gp.info.KVPair;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.svc.info.WorkgroupLite;
import com.gp.util.CommonUtils;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.Account;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by garydiao on 7/26/16.
 */
@Controller
@RequestMapping("/common")
public class WGroupController extends BaseController{

    /**
     * Find members of give workgroup, result will be used in common dialog to select workgroup member
     **/
    @RequestMapping("workgroup-member-list")
    public ModelAndView doGetWorkgroupMemberList(HttpServletRequest request){

        String wgroupid = request.getParameter("wgroup_id");
        String account = super.readRequestParam("user_name");
        ActionResult ars = new ActionResult();
        List<Account> list = new ArrayList<Account>();
        Principal principal = super.getPrincipal();
        AccessPoint accesspoint = super.getAccessPoint(request);

        InfoId<Long> wkey = null;
        if(StringUtils.isNotBlank(wgroupid) && CommonUtils.isNumeric(wgroupid)){

            wkey = IdKey.WORKGROUP.getInfoId(Long.valueOf(wgroupid));
        }

        ModelAndView mav = super.getJsonModelView();
        try{
            List<GroupMemberInfo> gresult = WorkgroupFacade.findWorkgroupMembers(accesspoint, principal, wkey, account, null);
            for(GroupMemberInfo info: gresult){

                Account ui = new Account();
                ui.setSourceId(info.getSourceId());
                ui.setUserId(info.getUserId());
                ui.setAccount(info.getAccount());
                ui.setEmail(info.getEmail());
                ui.setType(info.getUserType());
                ui.setName(info.getUserName());

                list.add(ui);
            }

            ars.setState(ActionResult.SUCCESS);
            ars.setMessage(getMessage("mesg.find.wgroup.mbr"));
            ars.setData(list);
        }catch(CoreException ce){

            ars.setState(ActionResult.ERROR);
            ars.setMessage(ce.getMessage());
        }

        mav.addAllObjects(ars.asMap());
        return mav;
    }

    /**
     * Find work groups list to show in dropdown widget
     *
     **/
    @RequestMapping("workgroup-list")
    public ModelAndView doGetWorkgroupList(HttpServletRequest request){

        String wgroupname = request.getParameter("wgroup_name");
        PageQuery pq = new PageQuery(8,1);
        super.readRequestData(request, pq);// read pageNumber
        ModelAndView mav = super.getJsonModelView();
        Principal principal = super.getPrincipal();
        AccessPoint accesspoint = super.getAccessPoint(request);

        boolean hasMore = false;
        List<KVPair<String, String>> enlist = new ArrayList<KVPair<String, String>>();
        try{

            PageWrapper<WorkgroupLite> gresult = WorkgroupFacade.findLocalWorkgroups(accesspoint, principal, wgroupname, null, pq);

            for(WorkgroupLite cinfo : gresult.getRows()){
                Long id = cinfo.getInfoId().getId();
                KVPair<String, String> kv = new KVPair<String, String>(String.valueOf(id),
                        cinfo.getWorkgroupName());
                enlist.add(kv);
            }

            hasMore = enlist.size() == pq.getPageSize();

        }catch(CoreException ce){
            ce.printStackTrace();
        }

        mav.addObject("items", enlist);
        mav.addObject("more", hasMore);

        return mav;
    }

    @RequestMapping("workgroup-files")
    public ModelAndView doGetWorkgroupFiles(HttpServletRequest request){

        ModelAndView mav = getJsonModelView();

        return mav;
    }

    @RequestMapping("wgroup-profile-lite")
    public ModelAndView doGetProfileLite(HttpServletRequest request){

        ModelAndView mav = getJspModelView("dialog/wgroup-profile-lite");
        String wgroup_id = request.getParameter("wgroup_id");
        long gid = NumberUtils.toLong(wgroup_id);

        return mav;
    }
}
