/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import Model.*;

public class PostController {
    private Connection conn;

    public PostController() {
        this.conn = DatabaseConnection.getConnection();
    }

    public boolean createPost(int userId, String content) {
        try {
            String sql = "INSERT INTO posts (user_id, content, timestamp) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setString(2, content);
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("❌ Post creation failed: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Post> fetchAllPosts() {
        ArrayList<Post> posts = new ArrayList<>();
        try {
            String sql = "SELECT p.*, u.first_name, u.last_name, u.email, u.role FROM posts p INNER JOIN users u ON p.user_id = u.id ORDER BY timestamp DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User author = new User(
                    rs.getInt("user_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    "", // no password
                    rs.getString("role")
                );

                Post post = new Post(
                    rs.getInt("id"),
                    author,
                    rs.getString("content"),
                    rs.getTimestamp("timestamp").toLocalDateTime()
                );

                posts.add(post);
            }
        } catch (SQLException e) {
            System.err.println("❌ Fetching posts failed: " + e.getMessage());
        }
        return posts;
    }
}
