package com.gp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.session.ExpiringSession;
import org.springframework.session.SessionRepository;

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

	@SuppressWarnings("rawtypes")
	@Autowired
	private SessionRepository sessionRepository;
	
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
	
	@Bean
	public ConnectHandler<S> webSocketConnectHandler() {
		
		return new ConnectHandler<S>(messagingTemplate, sessionRepository);
	}

	@Bean
	public DisconnectHandler<S> webSocketDisconnectHandler() {
		
		return new DisconnectHandler<S>(messagingTemplate, sessionRepository);
	}
}
