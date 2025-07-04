package com.example.demo.session;

import java.util.ArrayList;

import com.example.demo.model.Message;

public interface MessageListener {
	
	void onMessageReceive(Message message);
	void onActiveUserUpdated(ArrayList<String> users );
	
}
