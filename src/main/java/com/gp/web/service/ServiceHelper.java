package com.gp.web.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;

import com.gp.common.GeneralConfig;
import com.gp.common.IdKey;
import com.gp.common.Images;
import com.gp.common.SystemOptions;
import com.gp.core.CommonFacade;
import com.gp.info.InfoId;
import com.gp.util.ImageUtils;

public class ServiceHelper {
	
	public static String IMAGE_CACHE_PATH = GeneralConfig.getString(SystemOptions.IMAGE_CACHE_PATH);
	public static String FILE_CACHE_PATH = GeneralConfig.getString(SystemOptions.FILE_CACHE_PATH);
	
	public static InfoId<Long> cacheImage(String base64Img){
		
		String savePath = FILE_CACHE_PATH + File.separator + IMAGE_CACHE_PATH;
		
		int pos = base64Img.indexOf(';');
		String prefix = base64Img.substring(0, pos);
		pos = prefix.indexOf('/');
		String ext = prefix.substring(pos + 1);
		InfoId<Long> imgid = CommonFacade.generateId(IdKey.IMAGE, Long.class);
    	String cacheFileName = Images.getImgFileName(new Date(), imgid.getId(), ext);
    	
    	BufferedImage bufImg = ImageUtils.read(base64Img);
    	
    	ImageUtils.write(bufImg, savePath + File.separator + cacheFileName , ext);
    	
    	return imgid;
	}
}