// PostController.java
package Controller;

import Model.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.JOptionPane;

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
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Post creation failed: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Post> fetchAllPosts() {
        ArrayList<Post> posts = new ArrayList<>();
        try {
            String sql = """
                SELECT p.*, u.first_name, u.last_name, u.email, u.role, pr.avatar_url 
                FROM posts p
                JOIN users u ON p.user_id = u.id
                LEFT JOIN profiles pr ON u.id = pr.user_id
                ORDER BY p.timestamp DESC
            """;
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User author = new User(
                    rs.getInt("user_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    "",
                    rs.getString("role")
                );
                author.setAvatarPath(rs.getString("avatar_url"));

                Post post = new Post(
                    rs.getInt("id"),
                    author,
                    rs.getString("content"),
                    rs.getTimestamp("timestamp").toLocalDateTime()
                );
                posts.add(post);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Fetching posts failed: " + e.getMessage());
        }
        return posts;
    }

    public ArrayList<User> fetchLikesForPost(int postId) {
        ArrayList<User> likes = new ArrayList<>();
        try {
            String sql = """
                SELECT u.* FROM likes l
                JOIN users u ON l.user_id = u.id
                WHERE l.post_id = ?
            """;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    "",
                    rs.getString("role")
                );
                likes.add(user);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Fetching likes failed: " + e.getMessage());
        }
        return likes;
    }

    public void toggleLike(int postId, int userId) {
        try {
            String check = "SELECT * FROM likes WHERE post_id = ? AND user_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(check);
            checkStmt.setInt(1, postId);
            checkStmt.setInt(2, userId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                PreparedStatement delete = conn.prepareStatement("DELETE FROM likes WHERE post_id = ? AND user_id = ?");
                delete.setInt(1, postId);
                delete.setInt(2, userId);
                delete.executeUpdate();
            } else {
                PreparedStatement insert = conn.prepareStatement("INSERT INTO likes (post_id, user_id) VALUES (?, ?)");
                insert.setInt(1, postId);
                insert.setInt(2, userId);
                insert.executeUpdate();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Toggling like failed: " + e.getMessage());
        }
    }

    public ArrayList<Comment> fetchCommentsForPost(int postId) {
        ArrayList<Comment> comments = new ArrayList<>();
        try {
            String sql = """
                SELECT c.*, u.first_name, u.last_name, u.email, u.role 
                FROM comments c
                JOIN users u ON c.user_id = u.id
                WHERE c.post_id = ?
                ORDER BY c.timestamp ASC
            """;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User(
                    rs.getInt("user_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    "",
                    rs.getString("role")
                );
                Comment comment = new Comment(
                    rs.getInt("id"),
                    user,
                    rs.getString("content"),
                    rs.getTimestamp("timestamp").toLocalDateTime()
                );
                comments.add(comment);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Fetching comments failed: " + e.getMessage());
        }
        return comments;
    }

    public void addComment(int postId, int userId, String content) {
        try {
            String sql = "INSERT INTO comments (post_id, user_id, content, timestamp) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            stmt.setString(3, content);
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Adding comment failed: " + e.getMessage());
        }
    }
}
