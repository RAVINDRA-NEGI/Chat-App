package com.example.demo.client.controller;

import java.util.concurrent.ExecutionException;

import com.example.demo.client.MyStompClient;
import com.example.demo.model.Message;
import com.example.demo.session.MessageListener;

public class ClientController {
    private final MyStompClient stompClient;

    public ClientController(MessageListener listener, String username) throws InterruptedException, ExecutionException {
        this.stompClient = new MyStompClient(listener, username);
    }

    public void sendMessage(Message message) {
        stompClient.sendMessage(message);
    }

    public void disconnect(String username) {
        stompClient.disconnectedUser(username);
    }

    public boolean isConnected() {
        return stompClient.isConnected();
    }

    public void reconnect() {
        stompClient.reconnect();
    }
}
