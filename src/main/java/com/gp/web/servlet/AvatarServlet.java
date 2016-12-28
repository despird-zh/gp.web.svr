package com.gp.web.servlet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gp.common.GeneralConfig;
import com.gp.common.IdKey;
import com.gp.common.Images;
import com.gp.common.SystemOptions;
import com.gp.core.CommonFacade;
import com.gp.info.InfoId;
import com.gp.util.ImageUtils;
import com.gp.web.ActionResult;

@MultipartConfig(
		fileSizeThreshold=1024*1024*2, // 2MB
		maxFileSize=1024*1024*10,      // 10MB
		maxRequestSize=1024*1024*50)   // 50MB
public class AvatarServlet extends HttpServlet {

	public static Logger LOGGER = LoggerFactory.getLogger(AvatarServlet.class);
	static ObjectMapper mapper = new ObjectMapper();
	SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmss-");
	private static final long serialVersionUID = 1509093116513195847L;

	public static String AVATAR_SRC = "avatar_src";
	public static String AVATAR_DATA = "avatar_data";
	public static String AVATAR_FILE = "avatar_file";
	
	public static String UPLOAD_ERR_INI_SIZE = "The uploaded file exceeds the upload_max_filesize directive in php.ini";
	public static String UPLOAD_ERR_FORM_SIZE = "The uploaded file exceeds the MAX_FILE_SIZE directive that was specified in the HTML form";
	public static String UPLOAD_ERR_PARTIAL = "The uploaded file was only partially uploaded";
	public static String UPLOAD_ERR_NO_FILE = "No file was uploaded";
	public static String UPLOAD_ERR_NO_TMP_DIR = "Missing a temporary folder";
	public static String UPLOAD_ERR_CANT_WRITE = "Failed to write file to disk";
	public static String UPLOAD_ERR_EXTENSION = "File upload stopped by extension";
	
	public static String CACHE_PATH = GeneralConfig.getString(SystemOptions.IMAGE_CACHE_PATH);
	public static String FILE_CACHE_PATH = GeneralConfig.getString(SystemOptions.FILE_CACHE_PATH);
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
 
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String operFlag = request.getParameter("oper_flag");
        
        // get the save path
        String savePath = FILE_CACHE_PATH + File.separator + CACHE_PATH;
        
        String paramdata = request.getParameter(AVATAR_DATA);
        
        CropSetting cropsetting = mapper.readValue(paramdata, CropSetting.class);        
        LOGGER.debug("Request : {} -> {}", AVATAR_DATA, paramdata);
        String cacheFileName = null;
        String extension = null;
        String srcurl = request.getParameter(AVATAR_SRC);
        String srcFileName = null;
        BufferedImage srcimg = null;
        if(StringUtils.isNotBlank(srcurl)){
        	
        	extension = ImageUtils.detect(srcurl);
        	srcimg = ImageUtils.read(new URL(srcurl));
        	srcFileName = FilenameUtils.getName(srcurl);
        }else{
        	
	        Part part = request.getPart(AVATAR_FILE);
	        if(part.getContentType() != null){
	            //content-disposition：form-data; name="file"; filename="snmp4j--api.zip"
	            String header = part.getHeader("content-disposition");
	            // get the file name
	            String fileName = getFileName(header);
	            extension = FilenameUtils.getExtension(fileName);
	            srcFileName = fileName;
	            //save file to specified path
	            //part.write(savePath + File.separator + fileName);
	            srcimg = ImageIO.read(part.getInputStream());
	        }
        }
        // now srcimg is in the memory, need to store it in database
        if(null != srcimg){
        	if(cropsetting.getRotate() != 0)
        		srcimg = ImageUtils.rotate(srcimg, cropsetting.getRotate());
        	// generate a id for image.        	
        	InfoId<Long> imgid = CommonFacade.generateId(IdKey.IMAGE, Long.class);
        	cacheFileName = Images.getImgFileName(new Date(), imgid.getId(), extension);
        	// the file name will be {yyyyMMdd-HHmmss}-{img_id}.{ext}
        	srcimg = ImageUtils.crop(srcimg, (int)cropsetting.getX(), (int)cropsetting.getY(), (int)cropsetting.getWidth(), (int)cropsetting.getHeight());
        	ImageUtils.write(srcimg, savePath + File.separator + cacheFileName , extension);
        	
        }
        // if not new operation, just crop the image to expected size.
        if( !"new".equals(operFlag)){
        	// new operation 
	        ActionResult rmsg = new ActionResult();
	        rmsg.setMessage("OK");
	        rmsg.setState("200");
	        rmsg.setData("../" + CACHE_PATH + '/' + cacheFileName);
	        
	        mapper.writeValue(response.getOutputStream(), rmsg);
        }else{
        	// forward the request to further processing
        	// current path is /avatar, forward path is base on this
        	RequestDispatcher dispatcher = request.getRequestDispatcher("./ga/image-new.do"); 
        	request.setAttribute("file_name", cacheFileName);
        	request.setAttribute("image_name", srcFileName);
        	request.setAttribute("category", request.getParameter("category"));
        	dispatcher.forward(request, response); 
        }
    }
    
    public String getFileName(String header) {
        /**
         * String[] tempArr1 = header.split(";"); tempArr1 differs on browser
         * firefox and chrome：tempArr1={form-data,name="file",filename="snmp4j--api.zip"}
         * IE：tempArr1={form-data,name="file",filename="E:\snmp4j--api.zip"}
         */
        String[] tempArr1 = header.split(";");
        /**
         * firefox and chrome：tempArr2={filename,"snmp4j--api.zip"}
         * IE ：tempArr2={filename,"E:\snmp4j--api.zip"}
         */
        String[] tempArr2 = tempArr1[2].split("=");
        //get the correct file name 
        String fileName = tempArr2[1].substring(tempArr2[1].lastIndexOf("\\")+1).replaceAll("\"", "");
        return fileName;
    }
    
    public static class CropSetting {
    	
    	private double x;
    	private double y;
    	private double height;
    	private double width;
    	private int rotate;
		public double getX() {
			return x;
		}
		public void setX(double x) {
			this.x = x;
		}
		public double getY() {
			return y;
		}
		public void setY(double y) {
			this.y = y;
		}
		public double getHeight() {
			return height;
		}
		public void setHeight(double height) {
			this.height = height;
		}
		public double getWidth() {
			return width;
		}
		public void setWidth(double width) {
			this.width = width;
		}
		public int getRotate() {
			return rotate;
		}
		public void setRotate(int rotate) {
			this.rotate = rotate;
		}    	
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException,ServletException{

    	doPost(req, res);
    }
}
