package com.example.demo.client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.example.demo.model.Message;
import com.example.demo.session.MessageListener;


public class ClientGUI extends JFrame implements MessageListener{
	private JPanel connectedUsersPanel, messagePanel;
	private MyStompClient myStompClient;
	private String username;
	private JScrollPane messagePaneScrollPane;
	
	public ClientGUI(String username ) throws InterruptedException, ExecutionException {
		super("User: " + username);
		this.username = username;
		myStompClient =  new MyStompClient( this,username);
		
		setSize(1121 , 685);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
		
		@Override
		 public void windowClosing(WindowEvent e) {
			int option = JOptionPane.showConfirmDialog(ClientGUI.this, "Do you really want to leave?",
				"Exit", JOptionPane.YES_NO_OPTION	);
			if(option == JOptionPane.YES_OPTION) {
				myStompClient.disconnectedUser(username);
				ClientGUI.this.dispose();
			}
		}

		});
		
		addComponentListener(new  ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				updateMessageSize();
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
		
		messagePaneScrollPane = new JScrollPane(messagePanel);
		messagePaneScrollPane.setBackground(Utilitities.TRANSPARENT_COLOR);
		messagePaneScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		messagePaneScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		messagePaneScrollPane.getViewport().addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				revalidate();
				repaint();
			}
		});
		chatPanel.add(messagePaneScrollPane,BorderLayout.CENTER);
	
		
		JPanel inputPanel = new JPanel();
		inputPanel.setBorder(Utilitities.addPadding(10,10,10,10));
		inputPanel.setLayout(new BorderLayout());
		inputPanel.setBackground(Utilitities.TRANSPARENT_COLOR);


		FancyTextField inputField = new FancyTextField(30);
		
		inputField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() == KeyEvent.VK_ENTER) {
					String input = inputField.getText();
					
					if(input.isEmpty()) {
						JOptionPane.showMessageDialog(ClientGUI.this, "Please enter a message", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					inputField.setText("");
					
					
					myStompClient.sendMessage(new Message(username , input));
				}
			}
		});
		
		
		inputField.setBackground(Utilitities.SECOUNDARY_COLOR);
		inputField.setBorder(Utilitities.addPadding(0, 10, 0, 10));
		inputField.setForeground(Utilitities.Text_Color);
		inputField.setFont(new Font("Inter" , Font.ROMAN_BASELINE ,18 ));
		inputField.setPreferredSize(new Dimension(inputPanel.getWidth() , 40));
		
		
		inputPanel.add(inputField , BorderLayout.CENTER);
		chatPanel.add(inputPanel, BorderLayout.SOUTH);
		add(chatPanel,BorderLayout.CENTER);
	}
	
	private JPanel createChatMessageComponent(Message message) {
		JPanel chatMessage = new JPanel();
		chatMessage.setBackground(Utilitities.TRANSPARENT_COLOR);
		chatMessage.setLayout(new BoxLayout(chatMessage, BoxLayout.Y_AXIS));
		chatMessage.setBorder(Utilitities.addPadding(10, 10, 10, 10));
		
		JLabel usernameLabel = new JLabel(message.getUser());
		usernameLabel.setFont(new Font("Inter" , Font.BOLD , 18));
		usernameLabel.setForeground(Utilitities.Text_Color);
		chatMessage.add(usernameLabel);
		
		JLabel messageLabel = new JLabel();
		messageLabel.setText("<html><body style='width:" + 
			     (0.60 * getWidth()) + "px'>" + message.getMessage() + 
			     "</body></html>");

		messageLabel.setFont(new Font("Inter" , Font.PLAIN , 18));
		messageLabel.setForeground(Utilitities.Text_Color);
		chatMessage.add(messageLabel);
		
		return chatMessage;
		
	}

	@Override
	public void onMessageReceive(Message message) {
		messagePanel.add(createChatMessageComponent(message));
		revalidate();
		repaint();
		
		messagePaneScrollPane.getVerticalScrollBar().setValue(Integer.MAX_VALUE);
	}

	@Override
	public void onActiveUserUpdated(ArrayList<String> users) {
		//remove the current user list panel 
		if(connectedUsersPanel.getComponents().length >=2) {
			connectedUsersPanel.remove(1);
		}
		
		JPanel userListPanel = new JPanel();
		userListPanel.setBackground(Utilitities.TRANSPARENT_COLOR);
		userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
		
		for(String user : users) {
			JLabel username = new JLabel();
			username.setText(user);
			username.setForeground(Utilitities.Text_Color);
			username.setFont(new Font("Inter" , Font.BOLD , 16));
			userListPanel.add(username);
		}
		
		connectedUsersPanel.add(userListPanel);
		revalidate();
		repaint();
	}
	
	 private void updateMessageSize(){
	        for(int i = 0; i < messagePanel.getComponents().length; i++){
	            Component component = messagePanel.getComponent(i);
	            if(component instanceof JPanel){
	                JPanel chatMessage = (JPanel) component;
	                if(chatMessage.getComponent(1) instanceof JLabel){
	                    JLabel messageLabel = (JLabel) chatMessage.getComponent(1);
	                    messageLabel.setText("<html>" +
	                            "<body style='width:" + (0.60 * getWidth()) + "'px>" +
	                                messageLabel.getText() +
	                            "</body>"+
	                    "</html>");
	                }
	            }
	        }
	    }
}
