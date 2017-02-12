package com.gp.web.socket;

import java.util.Arrays;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

public class DisconnectHandler<S>
		implements ApplicationListener<SessionDisconnectEvent> {
	
	private SimpMessageSendingOperations messagingTemplate;

	public DisconnectHandler(SimpMessageSendingOperations messagingTemplate) {
		super();
		this.messagingTemplate = messagingTemplate;
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

