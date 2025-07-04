package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.demo.model.Message;
import com.example.demo.session.WebSocketSessionManager;

@Controller
public class WebSocketController {
		private final SimpMessagingTemplate messagingTemplate;
		private final WebSocketSessionManager sessionManager ;
		
		@Autowired
		public WebSocketController(SimpMessagingTemplate messagingTemplate, WebSocketSessionManager sessionManager) {
			this.sessionManager = sessionManager;
			this.messagingTemplate = messagingTemplate;
			
		}
		
		@MessageMapping("/message")
		public void handleMessage(Message message) {
			System.out.println("Received messsafe from user: "  + message.getUser() + " : " + message.getMessage());
			messagingTemplate.convertAndSend("/topic/messages", message);
			System.out.println("Sent message to /topic/messages: " + message.getUser() + " : " + message.getMessage());
		}
		@MessageMapping("/connect")
		public void connectUser(String username) {
			sessionManager.addUsername(username);
			sessionManager.broadcastActiveUsernames();
			System.out.println(username + " connected");
		}
		@MessageMapping("/disconnect")
		public void disconnected(String username) {
			sessionManager.removeUsername(username);
			sessionManager.broadcastActiveUsernames();
			System.out.println(username + " disconnected");
		}
		
		@MessageMapping("/request-users")
		public  void  requestUsers() {
			sessionManager.broadcastActiveUsernames();
			System.out.println("Requesting Users");
		}
}


 