package com.gp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.session.ExpiringSession;

import com.gp.web.socket.ConnectHandler;
import com.gp.web.socket.DisconnectHandler;

/**
 * These handlers are separated from WebSocketConfig because they are specific to this
 * application and do not demonstrate a typical Spring Session setup.
 *
 * @author Rob Winch
 */
// @Configuration
public class WebSocketHandlersConfigurer<S extends ExpiringSession> {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
	
	@Bean
	public ConnectHandler<S> webSocketConnectHandler() {
		
		return new ConnectHandler<S>(messagingTemplate);
	}

	@Bean
	public DisconnectHandler<S> webSocketDisconnectHandler() {
		
		return new DisconnectHandler<S>(messagingTemplate);
	}
}
