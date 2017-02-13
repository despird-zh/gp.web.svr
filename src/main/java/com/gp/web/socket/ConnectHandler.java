package com.gp.web.socket;

import java.security.Principal;
import java.util.Arrays;
import java.util.Calendar;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.session.ExpiringSession;
import org.springframework.session.SessionRepository;
import org.springframework.web.socket.messaging.SessionConnectEvent;

public class ConnectHandler<S>
		implements ApplicationListener<SessionConnectEvent> {

	private SimpMessageSendingOperations messagingTemplate;
	
	private SessionRepository<ExpiringSession> sessionRepository;
	
	public ConnectHandler(SimpMessageSendingOperations messagingTemplate, SessionRepository<ExpiringSession> sessionRepository) {
		super();
		this.messagingTemplate = messagingTemplate;
		this.sessionRepository = sessionRepository;
	}

	public void onApplicationEvent(SessionConnectEvent event) {
		MessageHeaders headers = event.getMessage().getHeaders();
		Principal user = SimpMessageHeaderAccessor.getUser(headers);
		if (user == null) {
			return;
		}
		String id = SimpMessageHeaderAccessor.getSessionId(headers);
		//this.repository.save(new ActiveWebSocketUser(id, user.getName(), Calendar.getInstance()));
		this.messagingTemplate.convertAndSend("/topic/friends/signin",
				Arrays.asList(user.getName()));
	}
}

