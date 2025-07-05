package com.example.demo.client;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.example.demo.model.Message;
import com.example.demo.session.MessageListener;
import com.example.demo.util.Utilities;

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
        connectedUsersPanel = new JPanel();
        connectedUsersPanel.setBorder(Utilities.addPadding(15, 15, 15, 15));
        connectedUsersPanel.setLayout(new BoxLayout(connectedUsersPanel, BoxLayout.Y_AXIS));
        connectedUsersPanel.setBackground(Utilities.SECOUNDARY_COLOR);
        connectedUsersPanel.setPreferredSize(new Dimension(200, getHeight()));

        JLabel connectedUsersLabel = new JLabel("Connected Users");
        connectedUsersLabel.setFont(Utilities.HEADER_FONT);
        connectedUsersLabel.setForeground(Utilities.TEXT_COLOR);
        connectedUsersPanel.add(connectedUsersLabel);

        userListPanel = new JPanel();
        userListPanel.setBackground(Utilities.TRANSPARENT_COLOR);
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
        connectedUsersPanel.add(userListPanel);

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
        messagePaneScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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
        add(chatPanel, BorderLayout.CENTER);
    }

    private JPanel createChatMessageComponent(Message message) {
        boolean isCurrentUser = message.getUser().equals(username);
        int maxWidth = (int) (getWidth() * 0.5);

        JLabel usernameLabel = new JLabel(message.getUser());
        usernameLabel.setFont(new Font("Inter", Font.BOLD, 14));
        usernameLabel.setForeground(Color.WHITE);

        JLabel messageLabel = new JLabel();
        messageLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        messageLabel.setForeground(Color.WHITE);

        String rawText = message.getMessage();
        messageLabel.putClientProperty("rawText", rawText);
        messageLabel.setText("<html><body style='width:" + (maxWidth - 40) + "px'>" + rawText + "</body></html>");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        JLabel timestamp = new JLabel(message.getTimestap().format(formatter));
        timestamp.setFont(new Font("Inter", Font.PLAIN, 11));
        timestamp.setForeground(new Color(200, 200, 200));
        timestamp.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JPanel chatMessage = new JPanel();
        chatMessage.setLayout(new BoxLayout(chatMessage, BoxLayout.Y_AXIS));
        chatMessage.setBackground(isCurrentUser ? new Color(100, 149, 237) : new Color(60, 63, 65));
        chatMessage.setOpaque(true);
        chatMessage.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        chatMessage.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));

        chatMessage.add(usernameLabel);
        chatMessage.add(messageLabel);
        chatMessage.add(timestamp);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        if (isCurrentUser) {
            wrapper.add(chatMessage, BorderLayout.EAST);
        } else {
            wrapper.add(chatMessage, BorderLayout.WEST);
        }

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));

        return wrapper;
    }

    @Override
    public void onMessageReceive(Message message) {
        SwingUtilities.invokeLater(() -> {
            createChatMessageComponent(message);
            revalidate();
            repaint();
            messagePaneScrollPane.getVerticalScrollBar().setValue(Integer.MAX_VALUE);
        });
    }

    @Override
    public void onActiveUserUpdated(ArrayList<String> users) {
        SwingUtilities.invokeLater(() -> {
            userListPanel.removeAll();
            for (String user : users) {
                JLabel userLabel = new JLabel(user);
                userLabel.setForeground(Utilities.TEXT_COLOR);
                userLabel.setFont(Utilities.USERNAME_FONT);
                userListPanel.add(userLabel);
            }
            userListPanel.revalidate();
            userListPanel.repaint();
        });
    }

    private void updateMessageSize() {
        int bubbleWidth = getWidth() / 2;
        for (Component wrapper : messagePanel.getComponents()) {
            if (wrapper instanceof JPanel) {
                JPanel contentWrapper = (JPanel) wrapper;
                if (contentWrapper.getComponentCount() > 0 && contentWrapper.getComponent(0) instanceof JPanel chatMessage) {
                    for (Component c : chatMessage.getComponents()) {
                        if (c instanceof JLabel label && label.getClientProperty("rawText") != null) {
                            String rawText = (String) label.getClientProperty("rawText");
                            label.setText("<html><div style='width: " + bubbleWidth + "px;'>" + rawText + "</div></html>");
                        }
                    }
                }
            }
        }
        messagePanel.revalidate();
        messagePanel.repaint();
    }
}
