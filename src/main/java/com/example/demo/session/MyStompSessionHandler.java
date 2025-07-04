package com.example.demo.session;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import com.example.demo.model.Message;

public class MyStompSessionHandler extends StompSessionHandlerAdapter{ 
	private String username;
	private MessageListener messsageListener;
	
	public MyStompSessionHandler ( MessageListener messageListener, String username) {
		this.username = username;
		this.messsageListener = messageListener;
	}

	@Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
	    System.out.println("Client Connected");

	    try {
	        session.subscribe("/topic/messages", new StompFrameHandler() {
	            @Override
	            public Type getPayloadType(StompHeaders headers) {
	                return Message.class;
	            }

	            @Override
	            public void handleFrame(StompHeaders headers, Object payload) {
	                System.out.println("handleFrame called");
	                if (payload instanceof Message) {
	                    Message message = (Message) payload;
	                    messsageListener.onMessageReceive( message);
	                    System.out.println("Received message: " + message.getUser() + " : " + message.getMessage());
	                } else {
	                    System.out.println("Unexpected payload: " + payload.getClass());
	                }
	                
	            }
	        });
	        System.out.println("Client Subscribed to /topic/messages");
	    } catch (Exception e) {
	        System.out.println("Subscription failed: " + e.getMessage());
	        e.printStackTrace();
	    }
	     
	    session.subscribe("/topic/users", new StompFrameHandler() {
			
	    	@Override
			public Type getPayloadType(StompHeaders headers) {
				return new ArrayList<String>().getClass();
			}
	    	
			@Override
			public void handleFrame(StompHeaders headers, Object payload) {
				try {
					if(payload instanceof ArrayList) {
						ArrayList<String> activeUsers = (ArrayList<String>) payload;
						messsageListener.onActiveUserUpdated(activeUsers);
						System.out.println("Received active users: " + activeUsers);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	    System.out.println("Subscribed to /topic/users");
	    
	    session.send("/app/connect", username);
	    session.send("/app/request-users","");
	}

	@Override
	public void handleException(StompSession session, @Nullable StompCommand command,
			StompHeaders headers, byte[] payload, Throwable exception) {
	}
}

