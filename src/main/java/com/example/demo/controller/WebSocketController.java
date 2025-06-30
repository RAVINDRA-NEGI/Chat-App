package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.demo.model.Message;

@Controller
public class WebSocketController {
		private final SimpMessagingTemplate messagingTemplate;
		
		@Autowired
		public WebSocketController(SimpMessagingTemplate messagingTemplate) {
			this.messagingTemplate = messagingTemplate;
		}
		
		@MessageMapping("/message")
		public void handleMessage(Message message) {
			System.out.println("Received messsafe from user: "  + message.getUser() + " : " + message.getMessage());
			messagingTemplate.convertAndSend("/topic/messages", message);
			System.out.println("Sent message to /topic/messages: " + message.getUser() + " : " + message.getMessage());
		}
}
 