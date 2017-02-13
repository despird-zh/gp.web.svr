package com.gp.config;

import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

public class WebSocketSecurityConfigurer
		extends AbstractSecurityWebSocketMessageBrokerConfigurer {

	@Override
	protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
		messages
			.simpMessageDestMatchers("/queue/**", "/topic/**").denyAll()
			.simpSubscribeDestMatchers("/queue/**/*-user*", "/topic/**/*-user*").denyAll()
			.anyMessage().authenticated();
	}
}
