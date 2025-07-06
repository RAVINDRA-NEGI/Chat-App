package com.example.demo.client;

import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class ClientLauncher {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
			String username = 	JOptionPane.showInputDialog(null , 
						"Enter Username (Max : 16 Characters):" ,
						"Chat Application",
						JOptionPane.QUESTION_MESSAGE);
			if(username == null || username.isEmpty() || username.length() > 16 ) {
				JOptionPane.showMessageDialog(null,
						"Invalid Username" ,
						"Error" ,
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
				ClientGUI clientGUI = null;
				try {
					clientGUI = new ClientGUI(username);
				} catch (InterruptedException | ExecutionException e) {
					throw new RuntimeException(e);
				}
				clientGUI.setVisible(true);
			}
		});
	}
}
