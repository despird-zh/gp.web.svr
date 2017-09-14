package com.gp.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gp.common.GPrincipal;
import com.gp.common.IdKey;
import com.gp.common.ServiceContext;
import com.gp.dao.info.SyncMsgOutInfo;
import com.gp.exception.CoreException;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.svc.CommonService;
import com.gp.svc.SyncService;
import com.gp.sync.message.SyncMessages;
import com.gp.sync.message.SyncPushMessage;

@Component
public class SyncFacade {

	static Logger LOGGER = LoggerFactory.getLogger(SourceFacade.class);
	
	private static SyncService syncservice;
	private static CommonService idService;
	
	@Autowired
	private SyncFacade(SyncService syncservice, CommonService idService){
		SyncFacade.syncservice = syncservice;
		SyncFacade.idService = idService;
	}
	
	public static void addSyncMsgOut(GPrincipal principal, SyncPushMessage pushMessage) throws CoreException{
		
		ServiceContext svcctx = new ServiceContext(principal);
		
		try{
			InfoId<Long> outId = idService.generateId(IdKey.GP_SYNC_MSG_OUT, Long.class);
			
			SyncMsgOutInfo msgOut = new SyncMsgOutInfo();
			msgOut.setInfoId(outId);
			msgOut.setEntityCode("N0101");
			
			String json = SyncMessages.wrapPayload(pushMessage.getPayload());
			
			msgOut.setTraceCode(pushMessage.getTraceCode());
			msgOut.setNodeCode(pushMessage.getNode());
			msgOut.setSyncCommand(pushMessage.getType().toString());
			msgOut.setMsgData(json);
			
			syncservice.newSyncMsgOut(svcctx, msgOut);
			
		}catch(ServiceException se) {
			
			throw new CoreException("excp.sync.msg",se);
		}
	}
}
