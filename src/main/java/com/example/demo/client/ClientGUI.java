package com.example.demo.client;

import java.util.concurrent.ExecutionException;

import com.example.demo.model.Message;

public class ClientGUI {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		MyStompClient myStompClient = new MyStompClient("Raju");
		
		 
		myStompClient.sendMessage(new Message("Tap Tap" , "Hello World!"));
		
		
	}
	
}
