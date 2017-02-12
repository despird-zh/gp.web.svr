package com.gp.config;

import java.security.Principal;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import com.gp.web.socket.HandshakeHandler;
import com.gp.web.socket.HandshakeInterceptor;

@EnableScheduling
@EnableWebSocketMessageBroker
public class WebSocketConfigurer extends AbstractSecurityWebSocketMessageBrokerConfigurer {
	
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
	public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
		registration.setSendTimeLimit(15 * 1000).setSendBufferSizeLimit(512 * 1024);
	}
    
    @Override
    public void customizeClientInboundChannel(ChannelRegistration registration) {
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

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .nullDestMatcher().authenticated() 
                .simpSubscribeDestMatchers("/user/queue/errors").permitAll() 
                .simpDestMatchers("/app/**").hasRole("USER") 
                .simpSubscribeDestMatchers("/user/**", "/topic/friends/*").hasRole("USER") 
                //.simpTypeMatchers(MESSAGE, SUBSCRIBE).denyAll() 
                .anyMessage().denyAll(); 

    }
}
