package com.gp.web.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gp.sync.client.SyncHttpClient;
import com.gp.sync.message.SyncPushMessage;
import com.gp.web.BaseController;
import com.gp.web.servlet.ServiceTokenFilter;
import com.gp.web.sync.SyncPayloads;

@Controller
@RequestMapping(ServiceTokenFilter.FILTER_PREFIX)
public class DevDebugAPIController extends BaseController{

	@RequestMapping(
		    value = "sync-debug", 
		    method = RequestMethod.POST,
		    consumes = {"text/plain", "application/*"})
	public ModelAndView doSyncDebug(@RequestBody(required = false) String payload) throws Exception {
		
		ModelAndView rtv= super.getJsonModelView();
		
		SyncHttpClient client = SyncHttpClient.getInstance();
		client.setAuthenSetting("dev1", "1", "http://localhost:8081/gpapi/authenticate", "web.client");
		SyncPushMessage pushMessage = new SyncPushMessage();
		pushMessage.setNode("xx");
		pushMessage.setTraceCode("xxxxx");
		pushMessage.setPayload("{}sss");
		pushMessage.setType(SyncPayloads.CMD_UPD_SOURCE);
		client.pushMessage("http://localhost:8081/gpapi/sync-push",pushMessage);
		
		return rtv;
	}
}
