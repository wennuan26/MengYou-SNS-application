package views;

import Controller.MessageController;
import Model.Message;
import Model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import static views.guiCons.BACKGROUND;
import static views.guiCons.PANEL;
import static views.guiCons.TEXT;


public class MessagingUI extends jFrame {
    private final User currentUser;
    private final int otherUserId;
    private final int itemId;
    private final MessageController controller;

    private JTextArea messageArea;
    private JTextArea chatHistory;

    public MessagingUI(User currentUser, int otherUserId, int itemId) {
        this.currentUser = currentUser;
        this.otherUserId = otherUserId;
        this.itemId = itemId;
        this.controller = new MessageController();

        setTitle("Chat with " + controller.getUserName(otherUserId));
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(BACKGROUND);

        jlabel titleLabel = new jlabel("Chat with " + controller.getUserName(otherUserId), 18, TEXT, Font.BOLD);
        titleLabel.setBounds(200, 10, 250, 30);
        add(titleLabel);

        chatHistory = new JTextArea();
        chatHistory.setEditable(false);
        chatHistory.setLineWrap(true);
        chatHistory.setWrapStyleWord(true);
        chatHistory.setFont(new Font("Arial", Font.PLAIN, 14));
        chatHistory.setForeground(TEXT);
        chatHistory.setBackground(PANEL);
        JScrollPane scroll = new JScrollPane(chatHistory);
        scroll.setBounds(30, 50, 520, 300);
        add(scroll);

        messageArea = new JTextArea();
        messageArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane msgScroll = new JScrollPane(messageArea);
        msgScroll.setBounds(30, 360, 400, 60);
        add(msgScroll);

        jbutton sendBtn = new jbutton("Send", 25, 14);
        sendBtn.setBounds(450, 370, 100, 45);
        sendBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sendMessage();
            }
        });
        add(sendBtn);

        jbutton backBtn = new jbutton("Back", 25, 14);
        backBtn.setBounds(30, 435, 100, 30);
        backBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });
        add(backBtn);

        loadChatHistory();
        setVisible(true);
    }

    private void loadChatHistory() {
        List<Message> messages = controller.fetchChatHistory(currentUser.getId(), otherUserId, itemId);
        StringBuilder sb = new StringBuilder();
        for (Message msg : messages) {
            String senderName = (msg.getSenderId() == currentUser.getId())
                    ? "You"
                    : controller.getUserName(msg.getSenderId());
            sb.append(senderName).append(": ").append(msg.getContent()).append("\n");
        }
        chatHistory.setText(sb.toString());
        chatHistory.setCaretPosition(chatHistory.getDocument().getLength());
    }

    private void sendMessage() {
        String text = messageArea.getText().trim();
        if (text.isEmpty()) return;

        boolean success = controller.sendMessage(currentUser.getId(), otherUserId, itemId, text);
        if (success) {
            messageArea.setText("");
            JOptionPane.showMessageDialog(this, "✅ Message sent successfully!");
            loadChatHistory();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Failed to send message.");
        }
    }
}
