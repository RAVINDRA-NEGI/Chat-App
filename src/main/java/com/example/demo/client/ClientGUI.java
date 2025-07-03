package com.example.demo.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.example.demo.model.Message;


public class ClientGUI extends JFrame{
	private JPanel connectedUsersPanel, messagePanel;
	
	
	public ClientGUI(String username) {
		super("User: " + username);
		
		setSize(1121 , 685);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
		
		@Override
		 public void windowClosing(WindowEvent e) {
			int option = JOptionPane.showConfirmDialog(ClientGUI.this, "Do you really want to leave?",
				"Exit", JOptionPane.YES_NO_OPTION	);
			if(option == JOptionPane.YES_OPTION) {
				ClientGUI.this.dispose();
			}
		}

		});
		
		getContentPane().setBackground(Utilitities.PRIMARY_COLOR);
		addGUIComponents();
	}
	
	private void addGUIComponents() {
		addConnectedUsersComponents();
		addChatComponents();
		
	}
	
	private void addConnectedUsersComponents() {
		connectedUsersPanel = new JPanel();
		connectedUsersPanel.setBorder(Utilitities.addPadding(15,15,15,15));
		connectedUsersPanel.setLayout(new BoxLayout(connectedUsersPanel , BoxLayout.Y_AXIS));
		connectedUsersPanel.setBackground(Utilitities.SECOUNDARY_COLOR);
		connectedUsersPanel.setPreferredSize(new Dimension(200 , getHeight()));
		
		JLabel connectedUsersLabel = new JLabel("Connected Users");
		connectedUsersLabel.setFont(new Font("Inter" , Font.BOLD, 18));
		connectedUsersLabel.setForeground(Utilitities.Text_Color);
		connectedUsersPanel.add(connectedUsersLabel);
		
		add(connectedUsersPanel , BorderLayout.WEST);
	}
	private void addChatComponents() {
		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(new BorderLayout());
		chatPanel.setBackground(Utilitities.TRANSPARENT_COLOR);
		
		messagePanel = new JPanel();
		messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
		messagePanel.setBackground(Utilitities.TRANSPARENT_COLOR);
		chatPanel.add(messagePanel,BorderLayout.CENTER);
		
		messagePanel.add(createChatMessageComponent(new Message("raz" , "hey")));
		
		JPanel inputPanel = new JPanel();
		inputPanel.setBorder(Utilitities.addPadding(15,15,15,15));
		inputPanel.setLayout(new BorderLayout());
		inputPanel.setBackground(Utilitities.TRANSPARENT_COLOR);

		JTextField inputField  = new JTextField();
		inputField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() == KeyEvent.VK_ENTER) {
					String input = inputField.getText();
					
					inputField.setText("");
					messagePanel.add(createChatMessageComponent(new Message("raz" , input)));
					repaint();
					revalidate();
				}
			}
		});
		inputField.setBackground(Utilitities.SECOUNDARY_COLOR);
		inputField.setForeground(Utilitities.Text_Color);
		inputField.setFont(new Font("Inter" , Font.PLAIN , 16));
		inputField.setPreferredSize(new Dimension(inputPanel.getWidth() , 50));
		inputPanel.add(inputField , BorderLayout.CENTER);
		chatPanel.add(inputPanel, BorderLayout.SOUTH);
		
		add(chatPanel,BorderLayout.CENTER);
	}
	
	private JPanel createChatMessageComponent(Message message) {
		JPanel chatMessage = new JPanel();
		chatMessage.setBackground(Utilitities.TRANSPARENT_COLOR);
		chatMessage.setLayout(new BoxLayout(chatMessage, BoxLayout.Y_AXIS));
		chatMessage.setBorder(Utilitities.addPadding(20, 20, 20, 20));
		
		JLabel usernameLabel = new JLabel(message.getUser());
		usernameLabel.setFont(new Font("Inter" , Font.BOLD , 18));
		usernameLabel.setForeground(Utilitities.Text_Color);
		chatMessage.add(usernameLabel);
		
		JLabel messageLabel = new JLabel(message.getMessage());
		messageLabel.setFont(new Font("Inter" , Font.PLAIN , 18));
		messageLabel.setForeground(Utilitities.Text_Color);
		chatMessage.add(messageLabel);
		
		return chatMessage;
		
	}
}
