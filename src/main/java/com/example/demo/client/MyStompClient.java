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
    private static final int MAX_MESSAGE_LENGTH = 1000;
    private StompSession session;
    private final String username;

    private WebSocketStompClient stompClient;
    private MyStompSessionHandler sessionHandler;
    private final String url = "ws://localhost:8080/ws";

    public MyStompClient(MessageListener messageListener, String username) throws InterruptedException, ExecutionException {
        this.username = username;

        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));

        SockJsClient sockJsClient = new SockJsClient(transports);
        stompClient = new WebSocketStompClient(sockJsClient);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        stompClient.setMessageConverter(converter);

        sessionHandler = new MyStompSessionHandler(messageListener, username);

        connect();
    }

    private void connect() throws InterruptedException, ExecutionException {
        System.out.println("Connecting to WebSocket...");
        session = stompClient.connectAsync(url, sessionHandler).get();
        System.out.println("Connected to WebSocket.");
    }

    public void reconnect() {
        try {
            System.out.println("Reconnecting...");
            session = stompClient.connectAsync(url, sessionHandler).get();
            System.out.println("Reconnected successfully.");
        } catch (Exception e) {
            System.err.println("Reconnect failed: " + e.getMessage());
        }
    }

    public void sendMessage(Message message) {
        if (message == null || message.getMessage() == null) return;

        String text = message.getMessage();

        if (text.length() > MAX_MESSAGE_LENGTH) {
            System.out.println("Message too long. Not sending.");
            return;
        }

        if (session == null || !session.isConnected()) {
            System.out.println("STOMP session not connected. Cannot send message.");
            return;
        }

        try {
            session.send("/app/message", message);
            System.out.println("Message Sent: " + text);
        } catch (Exception e) {
            System.err.println("Failed to send message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void disconnectedUser(String username) {
        if (session == null || !session.isConnected()) {
            System.out.println("STOMP session not connected. Cannot send disconnect.");
            return;
        }

        try {
            session.send("/app/disconnect", username);
            System.out.println("Disconnect User: " + username);
        } catch (Exception e) {
            System.err.println("Failed to send disconnect: " + e.getMessage());
        }
    }

    public boolean isConnected() {
        return session != null && session.isConnected();
    }
}
