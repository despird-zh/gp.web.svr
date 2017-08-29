package com.gp.web.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;

import com.gp.common.AccessPoint;
import com.gp.common.GeneralConfig;
import com.gp.common.IdKey;
import com.gp.common.Images;
import com.gp.common.GPrincipal;
import com.gp.common.SystemOptions;
import com.gp.core.CommonFacade;
import com.gp.core.ImageFacade;
import com.gp.exception.CoreException;
import com.gp.info.InfoId;
import com.gp.util.ImageUtils;

public class ServiceHelper {
	
	public static String IMAGE_CACHE_PATH = GeneralConfig.getString(SystemOptions.IMAGE_CACHE_PATH);
	public static String FILE_CACHE_PATH = GeneralConfig.getString(SystemOptions.FILE_CACHE_PATH);
	
	/**
	 * cache the base64 image into the cache path, eg. {cache dir}/{img path}/123-20160201-123213.jpg
	 * 
	 * @param base64Img the base64 image string, data:image/png;base64,seYsxdYJJLddxd....
	 * @return String the file name in cache folder, which include the image id and cache time.
	 **/
	public static String cacheImage(String base64Img){
		
		String savePath = FILE_CACHE_PATH + File.separator + IMAGE_CACHE_PATH;
		
		int pos = base64Img.indexOf(';');
		String prefix = base64Img.substring(0, pos);
		pos = prefix.indexOf('/');
		String ext = prefix.substring(pos + 1);
		InfoId<Long> imgid = CommonFacade.generateId(IdKey.IMAGE, Long.class);
    	String cacheFileName = Images.getImgFileName(new Date(), imgid.getId(), ext);
    	
    	BufferedImage bufImg = ImageUtils.read(base64Img);
    	
    	ImageUtils.write(bufImg, savePath + File.separator + cacheFileName , ext);
    	
    	return cacheFileName;
	}
	
	/**
	 * Save the cached image file
	 * 
	 * @param category the category of image
	 * @param cachedFileName the name of file reside in cache folder
	 * @param srcFileName the name of file reside in browser client.
	 *  
	 **/
	public static boolean  saveCachedImage(AccessPoint ap, GPrincipal principal, 
			String category, 
			String cachedFileName, 
			String srcFileName )throws CoreException{
		
		String fullPath = FILE_CACHE_PATH + File.separator + IMAGE_CACHE_PATH + File.separator + cachedFileName;
		
		return ImageFacade.saveImage(ap, principal,category, fullPath, srcFileName);
		
	}
}