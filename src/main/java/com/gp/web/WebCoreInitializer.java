package com.gp.web;

import com.gp.common.GPrincipal;
import com.gp.common.GroupUsers;
import com.gp.common.Synchronizes.SyncState;
import com.gp.core.SyncFacade;
import com.gp.disruptor.EventDispatcher;
import com.gp.disruptor.EventType;
import com.gp.exception.BaseException;
import com.gp.exception.CoreException;
import com.gp.info.InfoId;
import com.gp.launcher.CoreInitializer;
import com.gp.launcher.LifecycleHooker;
import com.gp.sync.client.SyncNodeAdapter;
import com.gp.sync.client.SyncNodeClient;
import com.gp.sync.message.SyncTriggerMessage;
import com.gp.web.util.ConfigUtils;

public class WebCoreInitializer extends CoreInitializer{

	public WebCoreInitializer() throws BaseException {
		super();
	}

	@Override
	public LifecycleHooker setupLifecycleHooker() throws BaseException {
		return new LifecycleHooker("WebCoreLifecycleHooker", 10){

			@Override
			public void initial() {
				
				// Register the operation hooker to generate operation log
				CoreOperationHooker operHooker = new CoreOperationHooker(EventType.CORE);
				EventDispatcher.getInstance().regEventHooker( operHooker );
				// Register sync hooker to persist and notify sync push 
				CoreSyncHooker syncHooker = new CoreSyncHooker();
				EventDispatcher.getInstance().regEventHooker( syncHooker );
				
				sendFeedback(false, "WebCoreLifecycleHooker initial done");
				
			}

			@Override
			public void startup() {
				// prepare the SyncHttpClient
				String accessUrl = ConfigUtils.getSystemOption("sync.node.access");
				SyncNodeClient.getInstance().setAuthenSetting("dev1", "1", accessUrl, "web.svr");
				
				SyncNodeAdapter adapter = new SyncNodeDelegate();
				SyncNodeClient.getInstance().setSyncNodeAdapter(adapter);
				
				sendFeedback(false, "WebCoreLifecycleHooker startup done");
			}

			@Override
			public void shutdown() {	}

		};
	}

	// define the delegate to sync node client adapter
	class SyncNodeDelegate implements SyncNodeAdapter{

		@Override
		public InfoId<?> persistMessage(SyncTriggerMessage triggerMsg) {
			GPrincipal princ = GroupUsers.PSEUDO_USER;
			InfoId<Long> outId = null;
			try {
				outId = SyncFacade.newSyncMsgOut(princ, triggerMsg);
				triggerMsg.setInfoId(outId);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return outId;
		}
		
		@Override
		public void changeMessageState(InfoId<Long> outId, SyncState state) {
			GPrincipal princ = GroupUsers.PSEUDO_USER;
			try {
				SyncFacade.saveSyncMsgOut(princ, outId, state);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
