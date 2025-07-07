package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import Model.User;

public class MessagingUI extends jFrame {
    private User currentUser;
    private int sellerId;
    private int itemId;
    private Connection conn;

    private JTextArea messageArea;
    private JTextArea chatHistory;

    public MessagingUI(User currentUser, int sellerId, int itemId) {
        this.currentUser = currentUser;
        this.sellerId = sellerId;
        this.itemId = itemId;

        setTitle("Chat with Seller");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(guiCons.lightpink);

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mengyou_db", "root", "@wennuan_26");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database connection failed.");
            return;
        }

        jlabel titleLabel = new jlabel("Chat with Seller", 18, guiCons.White, Font.BOLD);
        titleLabel.setBounds(200, 10, 250, 30);
        add(titleLabel);

        chatHistory = new JTextArea();
        chatHistory.setEditable(false);
        chatHistory.setLineWrap(true);
        chatHistory.setWrapStyleWord(true);
        chatHistory.setFont(new Font("Arial", Font.PLAIN, 14));
        chatHistory.setForeground(guiCons.White);
        chatHistory.setBackground(guiCons.lightpink);
        JScrollPane scroll = new JScrollPane(chatHistory);
        scroll.setBounds(30, 50, 520, 300);
        scroll.setBorder(BorderFactory.createLineBorder(guiCons.lightpink, 2));
        add(scroll);

        messageArea = new JTextArea();
        messageArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane msgScroll = new JScrollPane(messageArea);
        msgScroll.setBounds(30, 360, 400, 60);
        msgScroll.setBorder(BorderFactory.createLineBorder(guiCons.lightpink, 2));
        add(msgScroll);

        jbutton sendBtn = new jbutton("Send", 25, 14);
        sendBtn.setBounds(450, 370, 100, 45);
        sendBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                sendMessage();
            }
        });
        add(sendBtn);

        jbutton backBtn = new jbutton("Back", 25, 14);
        backBtn.setBounds(30, 435, 100, 30);
        backBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });
        add(backBtn);

        loadChatHistory();

        setVisible(true);
    }

    private void loadChatHistory() {
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT sender_id, content, timestamp FROM messages WHERE item_id = ? AND " +
                "((sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?)) ORDER BY timestamp ASC"
            );
            ps.setInt(1, itemId);
            ps.setInt(2, currentUser.getId());
            ps.setInt(3, sellerId);
            ps.setInt(4, sellerId);
            ps.setInt(5, currentUser.getId());

            ResultSet rs = ps.executeQuery();
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                int sender = rs.getInt("sender_id");
                String name = sender == currentUser.getId() ? "You" : "Seller";
                String text = rs.getString("content");
                sb.append(name).append(": ").append(text).append("\n");
            }
            chatHistory.setText(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String text = messageArea.getText().trim();
        if (text.isEmpty()) return;

        try {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO messages (sender_id, receiver_id, item_id, content) VALUES (?, ?, ?, ?)"
            );
            ps.setInt(1, currentUser.getId());
            ps.setInt(2, sellerId);
            ps.setInt(3, itemId);
            ps.setString(4, text);
            ps.executeUpdate();

            messageArea.setText("");
            loadChatHistory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
