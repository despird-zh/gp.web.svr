package com.gp.config;

import java.security.Principal;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurationSupport;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import com.gp.web.socket.CustSubProtocolHandler;
import com.gp.web.socket.HandshakeHandler;
import com.gp.web.socket.HandshakeInterceptor;

@EnableWebSocketMessageBroker
public class WebSocketConfigurer extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/gp-app");
        config.setUserDestinationPrefix("/gp-user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gp-ws")
        .setAllowedOrigins("*")
        .setHandshakeHandler(new HandshakeHandler())
        .addInterceptors(new HandshakeInterceptor()).withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
      registration.setInterceptors(new ChannelInterceptorAdapter() {

          @Override
          public Message<?> preSend(Message<?> message, MessageChannel channel) {

              StompHeaderAccessor accessor =
                  MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

              if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                  Principal user = null ; // access authentication header(s)
                  accessor.setUser(user);
              }

              return message;
          }
      });
    }
}
