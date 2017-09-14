package com.gp.web.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gp.sync.client.SyncNodeClient;
import com.gp.sync.message.SyncTriggerMessage;
import com.gp.web.BaseController;
import com.gp.web.servlet.ServiceTokenFilter;

@Controller
@RequestMapping(ServiceTokenFilter.FILTER_PREFIX)
public class DevDebugAPIController extends BaseController{

	@RequestMapping(
		    value = "sync-debug", 
		    method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doSyncDebug(@RequestBody(required = false) String payload) throws Exception {
		
		ModelAndView rtv= super.getJsonModelView();
		
		SyncNodeClient client = SyncNodeClient.getInstance();
		client.setAuthenSetting("dev1", "1", "http://localhost:8081", "web.client");
		SyncTriggerMessage pushMessage = new SyncTriggerMessage();
		pushMessage.setNode("xx");
		pushMessage.setTraceCode("xxxxx");
		pushMessage.setPayload("{}sss");
		pushMessage.setType(com.gp.sync.SyncPayloads.CMD_UPD_SOURCE);
		client.sendMessage("/gpapi/sync-push",pushMessage);
		
		return rtv;
	}
}
