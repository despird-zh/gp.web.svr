package com.gp.core;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gp.common.FlatColumns;
import com.gp.common.GPrincipal;
import com.gp.common.GroupUsers;
import com.gp.common.IdKey;
import com.gp.common.ServiceContext;
import com.gp.common.Synchronizes.SyncState;
import com.gp.dao.info.SyncMsgOutInfo;
import com.gp.exception.CoreException;
import com.gp.exception.ServiceException;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.svc.CommonService;
import com.gp.svc.SyncService;
import com.gp.sync.SyncMessages;
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
	
	public static InfoId<Long> newSyncMsgOut(GPrincipal principal, SyncPushMessage pushMessage) throws CoreException{
		
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
			msgOut.setState(SyncState.PENDING.name());
			boolean done = syncservice.newSyncMsgOut(svcctx, msgOut);
			
			return done? outId : null;
		}catch(ServiceException se) {
			
			throw new CoreException("excp.sync.msg",se);
		}
	}
	
	public static boolean saveSyncMsgOut(GPrincipal principal, InfoId<Long> outId, SyncState state) throws CoreException{
		
		try{
			
			Map<FlatColLocator, Object> fields = new HashMap<FlatColLocator, Object>();
			fields.put(FlatColumns.STATE, state.name());
			fields.put(FlatColumns.MODIFIER, principal.getAccount());
			fields.put(FlatColumns.MODIFY_DATE, new Date());
			
			int cnt = idService.update(outId, fields);
			
			return cnt > 0;
		}catch(ServiceException se) {
			
			throw new CoreException("excp.sync.msg",se);
		}
	}
}
