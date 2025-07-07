package com.example.demo.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.example.demo.client.component.FancyTextField;
import com.example.demo.client.component.RoundedPanel;
import com.example.demo.client.ui.Utilities;
import com.example.demo.model.Message;
import com.example.demo.session.MessageListener;

public class ClientGUI extends JFrame implements MessageListener {
    private JPanel connectedUsersPanel, messagePanel, userListPanel;
    private MyStompClient myStompClient;
    private String username;
    private JScrollPane messagePaneScrollPane;
    

    public ClientGUI(String username) throws InterruptedException, ExecutionException {
        super("User: " + username);
        this.username = username;
        myStompClient = new MyStompClient(this, username);

        setSize(1121, 685);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(ClientGUI.this,
                        "Do you really want to leave?",
                        "Exit", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    myStompClient.disconnectedUser(username);
                    dispose();
                }
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateMessageSize();
            }
        });

        getContentPane().setBackground(Utilities.PRIMARY_COLOR);
        addGUIComponents();
    }

    private void addGUIComponents() {
        addConnectedUsersComponents();
        addChatComponents();
    }

    private void addConnectedUsersComponents() {
    	
    	JButton toggleSidebarBtn = new JButton("☰");
    	toggleSidebarBtn.setFocusPainted(false);
    	toggleSidebarBtn.setFont(new Font("Dialog", Font.BOLD, 18));
    	toggleSidebarBtn.setBackground(Utilities.TRANSPARENT_COLOR);
    	toggleSidebarBtn.setForeground(Color.WHITE);
    	toggleSidebarBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

    	toggleSidebarBtn.addActionListener(e -> {
    	    boolean isVisible = connectedUsersPanel.isVisible();
    	    connectedUsersPanel.setVisible(!isVisible);
    	});



        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.add(toggleSidebarBtn);
        getContentPane().add(topPanel, BorderLayout.NORTH);

        // === SIDEBAR PANEL ===
        connectedUsersPanel = new JPanel(new BorderLayout());
        connectedUsersPanel.setBorder(Utilities.addPadding(15, 15, 15, 15));
        connectedUsersPanel.setBackground(Utilities.SECOUNDARY_COLOR);
        connectedUsersPanel.setPreferredSize(new Dimension(200, getHeight()));

        // Header
        JLabel connectedUsersLabel = new JLabel("Connected Users");
        connectedUsersLabel.setFont(Utilities.HEADER_FONT);
        connectedUsersLabel.setForeground(Utilities.TEXT_COLOR);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setOpaque(false);
        headerPanel.add(connectedUsersLabel);

        connectedUsersPanel.add(headerPanel, BorderLayout.NORTH);

        // === USER LIST (INSIDE SCROLL PANE) ===
        userListPanel = new JPanel();
        userListPanel.setBackground(Utilities.TRANSPARENT_COLOR);
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(userListPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10); // smooth scrolling

        connectedUsersPanel.add(scrollPane, BorderLayout.CENTER);

      
        add(connectedUsersPanel, BorderLayout.WEST);
    }

    private void addChatComponents() {
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setBackground(Utilities.TRANSPARENT_COLOR);

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(Utilities.TRANSPARENT_COLOR);

        messagePaneScrollPane = new JScrollPane(messagePanel);
        messagePaneScrollPane.setBackground(Utilities.TRANSPARENT_COLOR);
        messagePaneScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        messagePaneScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        messagePaneScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        messagePaneScrollPane.getViewport().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                revalidate();
                repaint();
            }
        });

        chatPanel.add(messagePaneScrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(Utilities.addPadding(10, 10, 10, 10));
        inputPanel.setBackground(Utilities.TRANSPARENT_COLOR);

        FancyTextField inputField = new FancyTextField(30);
        inputField.addActionListener(e -> {
            String input = inputField.getText().trim();

            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(ClientGUI.this, "Please enter a message", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (input.length() > 1000) {
                JOptionPane.showMessageDialog(ClientGUI.this, "Message too long. Please keep it under 1000 characters.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!myStompClient.isConnected()) {
                JOptionPane.showMessageDialog(ClientGUI.this, "Connection lost. Please reconnect or restart the app.", "Connection Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            inputField.setText("");
            myStompClient.sendMessage(new Message(username, input, LocalDateTime.now()));
        });


        inputField.setBackground(Utilities.SECOUNDARY_COLOR);
        inputField.setBorder(Utilities.addPadding(0, 10, 0, 10));
        inputField.setForeground(Utilities.TEXT_COLOR);
        inputField.setFont(Utilities.INPUT_FONT);
        inputField.setPreferredSize(new Dimension(inputPanel.getWidth(), 40));

        inputPanel.add(inputField, BorderLayout.CENTER);
        chatPanel.add(inputPanel, BorderLayout.SOUTH);
        getContentPane().add(chatPanel, BorderLayout.CENTER);
    }

    private JPanel createChatMessageComponent(Message message) {
        boolean isCurrentUser = message.getUser().equals(username);

        // Username Label
        JLabel usernameLabel = new JLabel(message.getUser());
        usernameLabel.setFont(new Font("Inter", Font.BOLD, 14));
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.putClientProperty("rawText", message.getUser());

        // Message Text
        JTextArea messageText = new JTextArea(message.getMessage());
        messageText.setFont(new Font("Inter", Font.PLAIN, 16));
        messageText.setForeground(Color.WHITE);
        messageText.setOpaque(false);
        messageText.setEditable(false);
        messageText.setLineWrap(true);
        messageText.setWrapStyleWord(true);
        messageText.setBorder(null);

        // Timestamp Label
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        JLabel timestampLabel = new JLabel(message.getTimestap().format(formatter));
        timestampLabel.setFont(new Font("Inter", Font.ITALIC, 11));
        timestampLabel.setForeground(new Color(180, 180, 180));
        timestampLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        timestampLabel.putClientProperty("rawText", message.getTimestap().format(formatter));

        // Bubble Panel
        RoundedPanel bubblePanel = new RoundedPanel(20);
        bubblePanel.setLayout(new BoxLayout(bubblePanel, BoxLayout.Y_AXIS));
        bubblePanel.setBackground(isCurrentUser ? Utilities.FIRST_COLOR : Utilities.SECOND_COLOR);
        bubblePanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        bubblePanel.setOpaque(false); // Optional if RoundedPanel handles background

        bubblePanel.add(usernameLabel);
        bubblePanel.add(Box.createVerticalStrut(5));
        bubblePanel.add(messageText);
        bubblePanel.add(Box.createVerticalStrut(5));
        bubblePanel.add(timestampLabel);

        int vpWidth = messagePaneScrollPane.getViewport().getWidth();
        int rawWidth = (int) (vpWidth * Utilities.BUBBLE_WIDTH_RATIO);
        int bubbleWidth = Math.min(rawWidth, Utilities.MAX_BUBBLE_WIDTH);
        bubblePanel.setMaximumSize(new Dimension(bubbleWidth, Integer.MAX_VALUE));

        // Wrapper Panel for alignment
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, bubblePanel.getPreferredSize().height + 20));

        if (isCurrentUser) {
            wrapper.add(Box.createHorizontalGlue());
            wrapper.add(bubblePanel);
        } else {
            wrapper.add(bubblePanel);
            wrapper.add(Box.createHorizontalGlue());
        }

        // Add to messagePanel
        messagePanel.add(wrapper);
        messagePanel.add(Box.createVerticalStrut(10));

        return wrapper;
    }


    @Override
    public void onMessageReceive(Message message) {
        SwingUtilities.invokeLater(() -> {
            createChatMessageComponent(message);
            revalidate();
            repaint();

            JScrollBar verticalBar = messagePaneScrollPane.getVerticalScrollBar();
            int threshold = 50;

            int current = verticalBar.getValue() + verticalBar.getVisibleAmount();
            int max = verticalBar.getMaximum();

            if ((max - current) < threshold) {
                verticalBar.setValue(verticalBar.getMaximum());
            }
        });
    }


    @Override
    public void onActiveUserUpdated(ArrayList<String> users) {
        SwingUtilities.invokeLater(() -> {
            userListPanel.removeAll();

            for (String user : users) {
                JPanel userRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)); // 5px horizontal, 0px vertical gap
                userRow.setOpaque(false);

                userRow.setAlignmentX(Component.LEFT_ALIGNMENT);
                userRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20)); // Set max height

                
                userRow.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0)); // optional 2px vertical spacing

                // Green dot
                JLabel greenDot = new JLabel("\u2B24"); // Unicode for ●
                greenDot.setFont(new Font("Dialog", Font.PLAIN, 10));    // Small size
                greenDot.setForeground(Utilities.GREEN_COLOR);             // Green color

                // Username
                JLabel userLabel = new JLabel(user);
                userLabel.setForeground(Utilities.TEXT_COLOR);
                userLabel.setFont(Utilities.USERNAME_FONT);

              
                userRow.add(greenDot);
                userRow.add(userLabel);

               
                userListPanel.add(userRow);
            }

            userListPanel.revalidate();
            userListPanel.repaint();
        });
    }


    private void updateMessageSize() {
    	int vpWidth = messagePaneScrollPane.getViewport().getWidth();
    	int rawWidth = (int)(vpWidth * Utilities.BUBBLE_WIDTH_RATIO);
    	int bubbleWidth = Math.min(rawWidth,Utilities.MAX_BUBBLE_WIDTH);

        for (Component comp : messagePanel.getComponents()) {
            if (!(comp instanceof JPanel wrapper)) {
				continue;
			}

            for (Component inner : wrapper.getComponents()) {
                if (!(inner instanceof JPanel bubble)) {
					continue;
				}

                // Update label widths only if needed
                for (Component c : bubble.getComponents()) {
                    if (c instanceof JLabel lbl && lbl.getClientProperty("rawText") != null) {
                        String raw = (String) lbl.getClientProperty("rawText");

                        // Only apply HTML wrapping if the raw text is long
                        if (raw.length() > 20) {
                            lbl.setText(String.format(
                                "<html><div style='width:%dpx;'>%s</div></html>",
                                bubbleWidth, raw
                            ));
                        } else {
                            lbl.setText(raw); // Keep plain text
                        }
                    }
                }

                // Update bubble width
                bubble.setMaximumSize(new Dimension(bubbleWidth + 24, Integer.MAX_VALUE));
            }
        }

        messagePanel.revalidate();
        messagePanel.repaint();
    }

}


