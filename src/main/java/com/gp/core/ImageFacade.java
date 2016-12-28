package com.gp.core;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gp.audit.AccessPoint;
import com.gp.common.IdKey;
import com.gp.common.Images;
import com.gp.common.Operations;
import com.gp.common.Principal;
import com.gp.common.ServiceContext;
import com.gp.exception.CoreException;
import com.gp.exception.ServiceException;
import com.gp.dao.info.ImageInfo;
import com.gp.info.InfoId;
import com.gp.svc.ImageService;

@Component
public class ImageFacade {
	
	static Logger LOGGER = LoggerFactory.getLogger(ImageFacade.class);
	
	private static ImageService imageservice;
	
	@Autowired
	private ImageFacade(ImageService imageservice){
		ImageFacade.imageservice = imageservice;
		
	}
	
	public static List<ImageInfo> findImages(AccessPoint accesspoint,
			Principal principal, String format) throws CoreException{
		
		List<ImageInfo> gresult = null;
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_IMAGES)){
			
			gresult = imageservice.getImages(svcctx, format);

		} catch (ServiceException e)  {
			ContextHelper.stampContext(e, "excp.find.images");

		}finally{
			
			ContextHelper.handleContext();
		}
				
		return gresult;
	}
	
	public static Boolean saveImage(AccessPoint accesspoint,
			Principal principal,String category, String imagePath , String imageName)throws CoreException{
		
		Boolean gresult = false;
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.NEW_IMAGE)){
			
			String filename = FilenameUtils.getName(imagePath);
			Long imgid = Images.parseImageId(filename);
			Date touchdate = Images.parseTouchDate(filename);
			
			ImageInfo image = new ImageInfo();
			image.setDataFile(new File(imagePath));
			image.setFormat(FilenameUtils.getExtension(filename));
			image.setModifyDate(touchdate);
			image.setImageName(imageName);
			image.setCategory(category);
			image.setPersist(Images.Persist.DATABASE.name());
			image.setLink(filename);
			
			image.setInfoId(IdKey.IMAGE.getInfoId(imgid));
			
			gresult = imageservice.newImage(svcctx, image);

		} catch (ServiceException e)  {
			ContextHelper.stampContext(e, "excp.new.image");
		}finally{
			
			ContextHelper.handleContext();
		}
				
		return gresult;
	}
	
	/**
	 * Find image information without retrieve the image binary data. 
	 **/
	public static ImageInfo findImage(AccessPoint accesspoint,
			Principal principal, InfoId<Long> imageId)throws CoreException{
		
		ImageInfo gresult = null;
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_IMAGE)){

			svcctx.setOperationObject(imageId);
			// this will ignore the image data reading
			gresult = imageservice.getImage(svcctx, imageId, "");
			
		} catch (ServiceException e)  {
			ContextHelper.stampContext(e, "excp.find.images");
		}finally{
			
			ContextHelper.handleContext();
		}
				
		return gresult;
	}
	
	/**
	 * Find image information without retrieve the image binary data. 
	 **/
	public static ImageInfo findImage(AccessPoint accesspoint,
			Principal principal, String parentPath, String fileName)throws CoreException{
		
		ImageInfo gresult = null;
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.FIND_IMAGE)){

			Long imgid = Images.parseImageId(fileName);
			InfoId<Long> infoid = IdKey.IMAGE.getInfoId(imgid);
			
			gresult = imageservice.getImage(svcctx, infoid, parentPath);
		
		} catch (ServiceException e)  {
			ContextHelper.stampContext(e, "excp.find.images");
		}finally{
			ContextHelper.handleContext();
		}
				
		return gresult;
	}
	
	/**
	 * Update the image with a image file and name
	 * @param imageId The if of original file
	 * @param imageName the name of new image
	 * @param imagePath the image file to save
	 **/
	public static Boolean updateImage(AccessPoint accesspoint,
			Principal principal,Long imageId,String category, String imageName, String imagePath)throws CoreException{
		
		Boolean gresult = false;
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.UPDATE_IMAGE)){

			ImageInfo image = new ImageInfo();
			image.setImageName(imageName);
			
			if(StringUtils.isNotBlank(imagePath)){
				String filename = FilenameUtils.getName(imagePath);
									
				image.setDataFile(new File(imagePath));
				image.setLink(filename);// get extension
				image.setPersist(Images.Persist.DATABASE.name());
				image.setCategory(category);
				image.setFormat(FilenameUtils.getExtension(filename)); // get format
				image.setModifyDate(Images.parseTouchDate(filename));
			}
			image.setInfoId(IdKey.IMAGE.getInfoId(imageId));
			svcctx.setOperationObject(image.getInfoId());// set audit data
			gresult = imageservice.updateImage(svcctx, image);

		} catch (ServiceException e)  {

			ContextHelper.stampContext(e, "excp.update.image");
		}finally{
			
			ContextHelper.handleContext();
		}
				
		return gresult;
	}
	
	public static Boolean removeImage(AccessPoint accesspoint,
			Principal principal,Long imageId)throws CoreException{
		
		Boolean gresult = false;
		
		try(ServiceContext svcctx = ContextHelper.beginServiceContext(principal, accesspoint,
				Operations.REMOVE_IMAGE)){
			
			InfoId<Long> imgid = IdKey.IMAGE.getInfoId(imageId);
			svcctx.setOperationObject(imgid);
			gresult = imageservice.removeImage(svcctx, imgid);			
			
		} catch (ServiceException e)  {

			ContextHelper.stampContext(e, "excp.remove.image");
		
		}finally{
			
			ContextHelper.handleContext();
		}
				
		return gresult;
	}
}
