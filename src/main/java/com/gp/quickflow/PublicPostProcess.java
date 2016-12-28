package com.gp.quickflow;

import java.util.Map;

import com.gp.common.IdKey;
import com.gp.exception.BaseException;
import com.gp.svc.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.gp.info.InfoId;

public class PublicPostProcess extends BaseFlowProcess {

	Logger LOGGER = LoggerFactory.getLogger(PublicPostProcess.class);

	@Autowired
	PostService postService;

	public PublicPostProcess() {

		super.setProcessName("public-post-square");
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public void processComplete(InfoId<Long> procId, InfoId<?> resourceId, String customStep, Map<String, Object> procData) throws BaseException {
		LOGGER.debug("The process flow completes");
	}

	@Override
	public String customNextStep(InfoId<Long> stepId, InfoId<?> resourceId, Map<String, Object> procData) throws BaseException {
		LOGGER.debug("enter the custom next step calculation");
		return "PASS";
	}

	@Override
	public boolean supportCheck(String resourceType) {
		
		return IdKey.POST.getTable().equalsIgnoreCase(resourceType);
	}

}
