package com.gp.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.gp.audit.AccessPoint;
import com.gp.web.BaseController;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gp.common.GeneralConfig;
import com.gp.common.SystemOptions;
import com.gp.exception.CoreException;
import com.gp.storage.ContentRange;

/**
 * Servlet implementation class TransferServlet
 */
@MultipartConfig
public class TransferServlet extends HttpServlet {
 
	static Logger LOGGER = LoggerFactory.getLogger(TransferServlet.class);
    private static final long serialVersionUID = 1L;

    static String upload_cache = GeneralConfig.getString(SystemOptions.FILE_CACHE_PATH);
    // unknown file extension
    public static final String UNKNOWN_EXT = "ukw";
    // the form element dom name
	public static final String CONTENT_PART = "files[]";
	
    /**
     * process upload request
     **/
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
    	
        // parse the request
    	PartMeta partmeta = processRequest(request);
    	try {
	    	if(partmeta.isChunkPart()){
	    		UploadHelper.storeFileChunk(upload_cache, partmeta);
	    	}else{
	    		UploadHelper.storeFile(upload_cache, partmeta);
	    	}
	        // prepare write back json string
	        response.setContentType("application/json");
	 
	        // Convert FilePart into JSON format
	        ObjectMapper mapper = new ObjectMapper();
	        // Send result to client
	        mapper.writeValue(response.getOutputStream(), partmeta);
    	} catch (CoreException e) {
			
			e.printStackTrace();
		}
    }
    
    /**
     * Process HEAD request. This returns the same headers as GET request, but without content.
     * @see HttpServlet#doHead(HttpServletRequest, HttpServletResponse).
     */
    protected void doHead(
      HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException
    {
    	// Process request without content.
    	try {
			DownloadHelper.processRequest(request, response, false);
		} catch (CoreException e) {
			
			e.printStackTrace();
		}
    }

    /**
     * Process GET request.
     * @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse).
     */
    protected void doGet(
      HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException
    {
    	// Process request with content.
    	try {
			DownloadHelper.processRequest(request, response, true);
		} catch (CoreException e) {
			
			e.printStackTrace();
		}
    }

    /**
     * Process the request
     *  
     * @param request the upload request
     * @return PartMeta the meta information extracted from request.
     * 
     **/
    public PartMeta processRequest(HttpServletRequest request) throws IOException, ServletException{

        Part part = request.getPart(CONTENT_PART);
        // Iterate each part
        PartMeta fmeta = new PartMeta();
        fmeta.setFileId(request.getParameter("file-id"));
        fmeta.setCabinetId(request.getParameter("cabinet-id"));
        String filename = getFilename(part);
        fmeta.setName(filename);
        String ext = FilenameUtils.getExtension(filename);
        fmeta.setExtension(StringUtils.isBlank(ext)? UNKNOWN_EXT : ext);
        // parse the input stream
        fmeta.setContent(part.getInputStream());
        fmeta.setContentType(part.getContentType());

        // parse the access point from request
        AccessPoint accesspoint = BaseController.getAccessPoint(request);
        fmeta.setAccessPoint(accesspoint);

        String contentRange = request.getHeader("Content-Range");
        
        if(StringUtils.isNotBlank(contentRange)){
        	LOGGER.debug("Content-Range : {}", contentRange);
        	ContentRange range = ContentRange.parse(contentRange);
        	fmeta.setContentRange(range);
        }else{
        	ContentRange range = new ContentRange();
        	range.setFileSize(part.getSize());
        	range.setEndPos(part.getSize() - 1); // startPos is 0
        	fmeta.setContentRange(range);
        }
        return fmeta;
    }
 
    /** 
     * this method is used to get file name out of request headers
     * @param part the file part of submitted form
     */ 
    private String getFilename(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return "unknown." + UNKNOWN_EXT;
    }
}