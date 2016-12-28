package com.gp.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gp.common.GeneralConstants;
import com.gp.common.IdKey;
import com.gp.exception.ServiceException;
import com.gp.info.Identifier;
import com.gp.info.InfoId;
import com.gp.svc.CommonService;

@Component
public class CommonFacade {
	
	public static Logger LOGGER = LoggerFactory.getLogger(CommonFacade.class);
	
	public static final InfoId<Integer> LOCAL_INSTANCE = IdKey.SOURCE.getInfoId(GeneralConstants.LOCAL_SOURCE);
	
	private static CommonService idService;

    @Autowired
    private CommonFacade(CommonService idService) {
    	LOGGER.debug("set idService with {} instance.", 
    			((idService == null) ? "null" : "real") );
    	CommonFacade.idService = idService;
    }

    /**
     * Generate InfoId for DAO's insert operation.
     * 
     * @param ap access point
     * @param principal the principal
     * @param idkey the ID key
     * @param t class of ID Type 
     **/
    public static <K> InfoId<K> generateId(
			IdKey idkey,
			Class<K> type)
    {
    	InfoId<K> id = null;
    	try {
			return idService.generateId(idkey,type);
		} catch (ServiceException e) {
			LOGGER.error("fail to generate id.",e);
		}
    	return id;
    }
    
    /**
     * Generate InfoId for DAO's insert operation.
     * 
     * @param ap access point
     * @param principal the principal
     * @param idkey the ID key string
     * @param t class of ID Type 
     **/
	public static <K> InfoId<K> generateId(
			Identifier idkey,
			Class<K> type){

		InfoId<K> id = null;
		try {
			id = (InfoId<K>)idService.generateId(idkey, type);
		} catch (ServiceException e) {
			
			LOGGER.error("fail to generate id.",e);
		}
		
		return id;
	}
}
