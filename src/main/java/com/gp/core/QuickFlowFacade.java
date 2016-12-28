package com.gp.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.common.ServiceContext;
import com.gp.dao.info.QuickNodeInfo;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.svc.QuickFlowService;

@Component
public class QuickFlowFacade {

	private static QuickFlowService quickflowservice;
	
	@Autowired
	public QuickFlowFacade(QuickFlowService quickflowservice){
		QuickFlowFacade.quickflowservice = quickflowservice;
	}
	
	public static void demo(){
		
		InfoId<Long> wgroupId = IdKey.WORKGROUP.getInfoId(172l);
		try {
			List<QuickNodeInfo> nodes = quickflowservice.getNodeList(ServiceContext.getPseudoServiceContext(), wgroupId);
			
			for(QuickNodeInfo node:nodes){
				System.out.println(node.getNodeName());
			}
			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
