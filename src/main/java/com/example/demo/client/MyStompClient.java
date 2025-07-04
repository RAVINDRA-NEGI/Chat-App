package com.example.demo.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.example.demo.model.Message;
import com.example.demo.session.MessageListener;
import com.example.demo.session.MyStompSessionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;





public class MyStompClient {
	private StompSession session;
	private String username;
	
	public  MyStompClient(MessageListener messageListener ,String username) throws InterruptedException, ExecutionException {
		this.username = username;
		
		List<Transport> transports = new ArrayList<>();
		transports.add(new WebSocketTransport(new StandardWebSocketClient()));
		
		SockJsClient sockJsClient = new SockJsClient(transports);
		WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setObjectMapper(objectMapper);
		stompClient.setMessageConverter(converter);

		StompSessionHandler sessionHandler = new MyStompSessionHandler(messageListener , username);
		String url = "ws://localhost:8080/ws";
		
		session = stompClient.connectAsync(url, sessionHandler).get();
	}
	
	public  void sendMessage(Message message) {
		try {
			session.send("/app/message", message);
			System.out.println("Message Sent " + message.getMessage());
		}catch(Exception e ) {
			 e.printStackTrace();
		}
	}

	public void disconnectedUser(String username) {
		session.send("/app/disconnect", username);
		System.out.println("Disconnect User : " + username);
		
	}
}
