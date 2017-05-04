package com.gp.web.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gp.common.AccessPoint;
import com.gp.common.GeneralConfig;
import com.gp.common.Images;
import com.gp.common.Principal;
import com.gp.common.SystemOptions;
import com.gp.core.ImageFacade;
import com.gp.core.MasterFacade;
import com.gp.dao.info.ImageInfo;
import com.gp.dao.info.SysOptionInfo;
import com.gp.exception.CoreException;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.Image;
import com.gp.web.servlet.ServiceFilter;

@Controller
@RequestMapping(ServiceFilter.FILTER_PREFIX)
public class ImagesController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(ImagesController.class);
	public static SimpleDateFormat TOUCH_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat MODIFY_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	@RequestMapping(
		    value = "image-query.do", 
		    method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doImageSearch(@RequestBody String payload){
		
		Map<String,String> paramap = super.readRequestJson(payload);
		ModelAndView mav = super.getJsonModelView();
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		
		String format = paramap.get("format");
		String category = paramap.get("category");
		ActionResult ars = new ActionResult();
		try{
			SysOptionInfo opt = MasterFacade.findSystemOption(accesspoint, principal, SystemOptions.PUBLIC_ACCESS);
			
			List<Image> images = new ArrayList<Image>();		
			List<ImageInfo> gresult = ImageFacade.findImages(accesspoint, principal, format, category);
			String image_cache = GeneralConfig.getString(SystemOptions.IMAGE_CACHE_PATH);
			for(ImageInfo info: gresult){
				
				Image img = new Image();
				img.setImageId(info.getInfoId().getId());
				img.setFormat(info.getFormat());
				img.setImageName(info.getImageName());
				img.setCategory(info.getCategory());
				img.setPersistType(info.getPersist());
				img.setModifier(info.getModifier());
				img.setModifyDate(MODIFY_DATE_FORMAT.format(info.getModifyDate()));
				
				String imgfilename = info.getLink();
				
				img.setImageUrl(opt.getOptionValue() + "/" + image_cache + '/' + imgfilename);
				
				images.add(img);
			}
			
			ars = ActionResult.success(getMessage("mesg.find.images"));
			ars.setData(images);

		}catch(CoreException ce){
						
			ars = super.wrapResult(ce);
		}
		
		mav.addAllObjects(ars.asMap());
		return mav;
	}
	
	@RequestMapping(
		    value = "image-new.do", 
		    method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doImageNew()throws IOException{
		
		ModelAndView mav = super.getJsonModelView();
		
		String imagePath = GeneralConfig.getString(SystemOptions.IMAGE_CACHE_PATH);

    	String cachedFileName = (String)request.getAttribute("file_name");
    	String srcFileName = (String)request.getAttribute("image_name");
    	String category = (String)request.getAttribute("category");
		String relativeUri = "/" + imagePath + '/' + cachedFileName;
		String realPath = request.getServletContext().getRealPath(relativeUri);
		LOGGER.debug(relativeUri);
		// new operation 
        ActionResult rmsg = new ActionResult();
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		try {
			ImageFacade.saveImage(accesspoint, principal,category, realPath, srcFileName);
				    	
	        rmsg = ActionResult.success("success create");
	        rmsg.setData("../" + imagePath + '/' + cachedFileName);
		} catch (CoreException ce) {
			
			rmsg = super.wrapResult(ce);
		}

        mav.addAllObjects(rmsg.asMap());
        
        return mav;
	}
	
	/**
	 * update the image with name and image file
	 *  
	 **/
	@RequestMapping("image-save.do")
	public ModelAndView doImageSave(HttpServletRequest request, HttpServletResponse response)throws IOException{
		
		ModelAndView mav = super.getJsonModelView();
		ActionResult rmsg = new ActionResult();
		
		String imagePath = GeneralConfig.getString(SystemOptions.IMAGE_CACHE_PATH);
		String realPath = null; 
    	String imgId = super.readRequestParam("image_id");// the id of original 
    	String imgName = super.readRequestParam("image_name");// the name of image
    	String imgSrc = super.readRequestParam("image_src"); // the file name of new image
    	String category = super.readRequestParam("category");
    	
    	imgSrc = StringUtils.isBlank(imgSrc)? null : FilenameUtils.getName(imgSrc);
    	if(Images.isQualifiedName(imgSrc)){
    		String relativeUri = "/" + imagePath + '/' + imgSrc;
    		realPath = request.getServletContext().getRealPath(relativeUri);
    	}
		
		LOGGER.debug("the real path : {}", realPath);
		
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		try {
			ImageFacade.updateImage(accesspoint, principal,Long.valueOf(imgId),category, imgName, realPath);
	
			rmsg = ActionResult.success("success create");
			rmsg.setData("../" + imagePath + '/' + imgSrc);
		} catch (CoreException ce) {
			
			rmsg = super.wrapResult(ce);
		}
        mav.addAllObjects(rmsg.asMap());
        
        return mav;
	}
	
	/**
	 * update the image with name and image file
	 *  
	 **/
	@RequestMapping("image-remove.do")
	public ModelAndView doImageRemove(HttpServletRequest request)throws IOException{
		
		ModelAndView mav = super.getJsonModelView();
		ActionResult rmsg = new ActionResult();
    	String imgId = super.readRequestParam("image_id");// the id of original 
		
    	Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		try{
			ImageFacade.removeImage(accesspoint, principal,Long.valueOf(imgId));
		
			rmsg = ActionResult.success("success create");
		} catch (CoreException ce) {
			
			rmsg = super.wrapResult(ce);
		}
        mav.addAllObjects(rmsg.asMap());
        
        return mav;
	}
	

}
