package com.gp.config;

import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.ExpiringSession;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import com.gp.web.socket.HandshakeHandler;
import com.gp.web.socket.HandshakeInterceptor;

@EnableScheduling
@EnableWebSocketMessageBroker
@Import({ 
	WebSocketHandlersConfigurer.class, 
	WebSocketSecurityConfigurer.class
})
public class WebSocketConfigurer extends AbstractSessionWebSocketMessageBrokerConfigurer<ExpiringSession> {
	
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/gp-app");
        config.setUserDestinationPrefix("/gp-user");
    }

    @Override
    public void configureStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gp-ws")
        .setAllowedOrigins("*")
        .setHandshakeHandler(new HandshakeHandler())
        .addInterceptors(new HandshakeInterceptor()).withSockJS();
    }

    @Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
		registration.setSendTimeLimit(15 * 1000).setSendBufferSizeLimit(512 * 1024);
	}

}
