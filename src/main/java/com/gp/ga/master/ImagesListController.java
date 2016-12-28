package com.gp.ga.master;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.audit.AccessPoint;
import com.gp.common.GeneralConfig;
import com.gp.common.Images;
import com.gp.common.Principal;
import com.gp.common.SystemOptions;
import com.gp.core.ImageFacade;
import com.gp.exception.CoreException;
import com.gp.dao.info.ImageInfo;
import com.gp.web.ActionResult;
import com.gp.web.BaseController;
import com.gp.web.model.Image;

@Controller("ga-images-ctlr")
@RequestMapping("/ga")
public class ImagesListController  extends BaseController{
	
	public static SimpleDateFormat TOUCH_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat MODIFY_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	Logger LOGGER = LoggerFactory.getLogger(ImagesListController.class);
	
	@RequestMapping("image-list")
	public ModelAndView doInitialView(){
		
		return getJspModelView("ga/master/image-list");
	}
	
	@RequestMapping("image-search")
	public ModelAndView doImageSearch(HttpServletRequest request){
		
		String format = super.readRequestParam("format");
		ModelAndView mav = super.getJsonModelView();
		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		ActionResult ars = new ActionResult();
		try{
			List<Image> images = new ArrayList<Image>();		
			List<ImageInfo> gresult = ImageFacade.findImages(accesspoint, principal, format);
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
				
				img.setImageUrl("../" + image_cache + '/' + imgfilename);
				
				images.add(img);
			}
			
			ars.setState(ActionResult.SUCCESS);
			ars.setMessage(getMessage("mesg.find.images"));
			ars.setData(images);

		}catch(CoreException ce){
						
			ars.setState(ActionResult.ERROR);
			ars.setMessage(ce.getMessage());
		}
		
		mav.addAllObjects(ars.asMap());
		return mav;
	}
	
	@RequestMapping("image-new")
	public ModelAndView doImageNew(HttpServletRequest request, HttpServletResponse response)throws IOException{
		
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
				    	
	        rmsg.setMessage("OK");
	        rmsg.setState("200");
	        rmsg.setData("../" + imagePath + '/' + cachedFileName);
		} catch (CoreException e) {
			
			e.printStackTrace();
		}

        mav.addAllObjects(rmsg.asMap());
        
        return mav;
	}
	
	/**
	 * update the image with name and image file
	 *  
	 **/
	@RequestMapping("image-save")
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
	
			rmsg.setMessage("Success in update the image");
			rmsg.setState(ActionResult.SUCCESS);
			rmsg.setData("../" + imagePath + '/' + imgSrc);
		} catch (CoreException e) {
			
			e.printStackTrace();
		}
        mav.addAllObjects(rmsg.asMap());
        
        return mav;
	}
	
	/**
	 * update the image with name and image file
	 *  
	 **/
	@RequestMapping("image-remove")
	public ModelAndView doImageRemove(HttpServletRequest request)throws IOException{
		
		ModelAndView mav = super.getJsonModelView();
		ActionResult rmsg = new ActionResult();
    	String imgId = super.readRequestParam("image_id");// the id of original 
		
    	Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);
		try{
			ImageFacade.removeImage(accesspoint, principal,Long.valueOf(imgId));
		
			rmsg.setMessage("Success remove the image");
			rmsg.setState(ActionResult.SUCCESS);
		} catch (CoreException e) {
			
			e.printStackTrace();
		}
        mav.addAllObjects(rmsg.asMap());
        
        return mav;
	}
}
