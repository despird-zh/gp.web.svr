package com.gp.web.workspace;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.audit.AccessPoint;
import com.gp.common.GeneralConfig;
import com.gp.common.IdKey;
import com.gp.common.Images;
import com.gp.common.Principal;
import com.gp.common.SystemOptions;
import com.gp.core.ImageFacade;
import com.gp.core.MeasureFacade;
import com.gp.core.OrgHierFacade;
import com.gp.core.PersonalFacade;
import com.gp.core.SecurityFacade;
import com.gp.exception.CoreException;
import com.gp.info.CombineInfo;
import com.gp.dao.info.ImageInfo;
import com.gp.dao.info.OrgHierInfo;
import com.gp.dao.info.UserInfo;
import com.gp.dao.info.UserSumInfo;
import com.gp.svc.info.UserExtInfo;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.TreeNode;
import com.gp.web.model.UserMetaSummary;

@Controller
@RequestMapping("/workspace")
public class WSpaceMetaController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(WSpaceMetaController.class);
	
	static String ImagePath = GeneralConfig.getString(SystemOptions.IMAGE_CACHE_PATH);

	static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	@RequestMapping("meta-sum")
	public ModelAndView doMetaSummarySearch(HttpServletRequest request){
		
		ModelAndView mav = super.getJsonModelView();
		ActionResult result = new ActionResult();
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		
		try{
			UserMetaSummary meta = new UserMetaSummary();
			// find user summary information
			UserSumInfo usum = MeasureFacade.findPersonalSummary(accesspoint, principal, principal.getAccount());
			if(null != usum){
				
				meta.setFileSum(usum.getFileSummary());
				meta.setPostSum(usum.getPostSummary());
				meta.setShareSum(usum.getShareSummary());
				meta.setTaskSum(usum.getTaskSummary());
				
			}
			
			// find user information and extension data
			UserExtInfo cmbinfo = SecurityFacade.findAccount(accesspoint, principal, null, principal.getAccount(), null);
			meta.setName(cmbinfo.getFullName());
			meta.setSourceId(cmbinfo.getSourceId());
			meta.setSourceName(cmbinfo.getSourceName());
			meta.setSourceShort(cmbinfo.getSourceShortName());
			meta.setSinceDate(DATE_FORMAT.format(cmbinfo.getCreateDate()));
			
			meta.setSignature(cmbinfo.getSignature());
			ImageInfo imginfo = ImageFacade.findImage(accesspoint, principal, IdKey.IMAGE.getInfoId(cmbinfo.getAvatarId()));
			String imagePath = "../" + ImagePath + "/" + imginfo.getLink();
			
			meta.setImagePath(imagePath);
			List<CombineInfo<OrgHierInfo, Boolean>> belongs = PersonalFacade.findUserOrgHierNodes(accesspoint, principal, principal.getAccount());
			
			TreeNode[][] routes = new TreeNode[belongs.size()][];
			int cnt = 0;
			for(CombineInfo<OrgHierInfo, Boolean> belong: belongs){
				
				List<OrgHierInfo> orglist = OrgHierFacade.findRouteOrgHiers(accesspoint, principal, belong.getPrimary().getInfoId());
				TreeNode[] nodes = new TreeNode[orglist.size()];
				for(int i = 0 ; i<orglist.size(); i++){
					OrgHierInfo oinfo = orglist.get(i);
					TreeNode node = new TreeNode();
					node.setId(String.valueOf(oinfo.getInfoId().getId()));
					node.setPid(String.valueOf(oinfo.getParentOrg()));
					node.setName(oinfo.getOrgName());
					if(i == orglist.size() -1){
						node.setChildren(false);
					}
					nodes[i] = node;
				}
				routes[cnt] = nodes;
				cnt ++;
			}
			
			meta.setTreeNodes(routes);
			
			result.setData(meta);
			result.setState(ActionResult.SUCCESS);
			result.setMessage(getMessage("mesg.find.personal.meta"));
			
		}catch(CoreException ce){
			
			result.setState(ActionResult.FAIL);
			result.setMessage(ce.getMessage());
		}
		
		mav.addAllObjects(result.asMap());
		return mav;
		
	}
}
