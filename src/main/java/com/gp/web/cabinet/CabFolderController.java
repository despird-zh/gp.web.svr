package com.gp.web.cabinet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.audit.AccessPoint;
import com.gp.common.Principal;
import com.gp.core.CabinetFacade;
import com.gp.exception.CoreException;
import com.gp.dao.info.CabFolderInfo;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.util.CustomWebUtils;

/**
 * This controller wrap the operation on folder 
 **/
@Controller("cab-foler-ctrl")
@RequestMapping("/cabinet")
public class CabFolderController extends BaseController{

	@RequestMapping("new-folder")
	public ModelAndView doNewFolder(HttpServletRequest request){
		
		CustomWebUtils.dumpRequestAttributes(request);
		String cabinetid = super.readRequestParam("cabinet_id");
		String folderowner = super.readRequestParam("folder_owner");
		String foldername = super.readRequestParam("folder_name");
		String folderdescr = super.readRequestParam("folder_descr");
		String folderparent = super.readRequestParam("folder_parent_id");
		
		CabFolderInfo cabfolder = new CabFolderInfo();

		cabfolder.setCabinetId(NumberUtils.toLong(cabinetid));
		cabfolder.setParentId(NumberUtils.toLong(folderparent));
		cabfolder.setEntryName(foldername);
		cabfolder.setDescription(folderdescr);
		cabfolder.setOwner(folderowner);
		
		ActionResult aresult = new ActionResult();
		ModelAndView jmav = super.getJsonModelView();
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		
		try{
			CabinetFacade.addCabinetFolder(accesspoint, principal, cabfolder);
			
			aresult.setState(ActionResult.SUCCESS);
			aresult.setMessage(getMessage("mesg.new.folder"));
		}catch(CoreException ce){
			
			aresult.setState(ActionResult.ERROR);
			aresult.setMessage(ce.getMessage());
			aresult.setDetailmsgs(ce.getValidateMessages());
		}
		
		return jmav.addAllObjects(aresult.asMap());
	}
	
	@RequestMapping("folder-copy")
	public ModelAndView doCopyFolder(HttpServletRequest request){
		return null;
		
	}
	
	@RequestMapping("folder-move")
	public ModelAndView doMoveFolder(HttpServletRequest request){
		return null;
		
	}
	
	@RequestMapping("folder-delete")
	public ModelAndView doDeleteFolder(HttpServletRequest request){
		return null;
		
	}
	
	@RequestMapping("purge-folder")
	public ModelAndView doPurgeFolder(HttpServletRequest request){
		return null;
		
	}
}
