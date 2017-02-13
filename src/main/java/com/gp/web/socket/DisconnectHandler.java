package com.gp.web.socket;

import java.util.Arrays;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.session.ExpiringSession;
import org.springframework.session.SessionRepository;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

public class DisconnectHandler<S>
		implements ApplicationListener<SessionDisconnectEvent> {
	
	private SimpMessageSendingOperations messagingTemplate;

	private SessionRepository<ExpiringSession> sessionRepository;
	
	public DisconnectHandler(SimpMessageSendingOperations messagingTemplate, SessionRepository<ExpiringSession> sessionRepository) {
		super();
		this.messagingTemplate = messagingTemplate;
		this.sessionRepository = sessionRepository;
	}

	public void onApplicationEvent(SessionDisconnectEvent event) {
		String id = event.getSessionId();
		if (id == null) {
			return;
		}
		/*ActiveWebSocketUser user = this.repository.findOne(id);
		if (user == null) {
			return;
		}

		this.repository.delete(id);
		*/
		this.messagingTemplate.convertAndSend("/topic/friends/signout",
				Arrays.asList(""));

	}
}

