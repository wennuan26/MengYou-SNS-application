/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Model.DatabaseConnection;
import Model.Message;

public class MessageController {
    private final Connection conn;

    public MessageController() {
        this.conn = DatabaseConnection.getConnection();
    }

    public List<Message> fetchChatHistory(int userId1, int userId2, int itemId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT sender_id, content, timestamp FROM messages WHERE item_id = ? AND " +
                     "((sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?)) " +
                     "ORDER BY timestamp ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            ps.setInt(2, userId1);
            ps.setInt(3, userId2);
            ps.setInt(4, userId2);
            ps.setInt(5, userId1);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                messages.add(new Message(rs.getInt("sender_id"), rs.getString("content"), rs.getTimestamp("timestamp")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public boolean sendMessage(int senderId, int receiverId, int itemId, String content) {
        String sql = "INSERT INTO messages (sender_id, receiver_id, item_id, content) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, senderId);
            ps.setInt(2, receiverId);
            ps.setInt(3, itemId);
            ps.setString(4, content);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getUserName(int userId) {
        String sql = "SELECT first_name FROM users WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("first_name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "User(" + userId + ")";
    }
}
