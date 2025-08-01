package Controller;

import Model.*;
import java.sql.*;
import javax.swing.JOptionPane;
import java.util.ArrayList;

public class CommunityPostContoller {
    private final Connection conn = DatabaseConnection.getConnection();

    public boolean sendPost(int userId, int communityId, String content, String imagePath) {
        String sql = "INSERT INTO community_posts (user_id, community_id, content, image_path, timestamp) VALUES (?, ?, ?, ?, NOW())";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, communityId);
            stmt.setString(3, content);
            stmt.setString(4, imagePath);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showError("Send Post Error", e);
            return false;
        }
    }

    public ArrayList<Post> getPosts(int communityId) {
        ArrayList<Post> posts = new ArrayList<>();
        String sql = "SELECT p.*, u.first_name, u.last_name, u.email FROM community_posts p " +
                     "JOIN users u ON p.user_id = u.id " +
                     "WHERE p.community_id = ? ORDER BY p.timestamp DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, communityId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User author = new User(
                    rs.getInt("user_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    "", "registered"
                );
                Post post = new Post(
                    rs.getInt("id"),
                    author,
                    rs.getString("content"),
                    rs.getTimestamp("timestamp").toLocalDateTime()
                );
                post.getLikes().addAll(getLikesForPost(post.getId()));
                post.getComments().addAll(getCommentsForPost(post.getId()));
                posts.add(post);
            }
        } catch (SQLException e) {
            showError("Get Posts Error", e);
        }
        return posts;
    }

    public boolean updatePost(int postId, String newContent) {
        String sql = "UPDATE community_posts SET content = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newContent);
            stmt.setInt(2, postId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showError("Update Post Error", e);
            return false;
        }
    }

    public boolean deletePost(int postId) {
        String sql = "DELETE FROM community_posts WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showError("Delete Post Error", e);
            return false;
        }
    }

    public boolean toggleLike(int userId, int postId) {
        String checkSql = "SELECT * FROM post_likes WHERE user_id = ? AND post_id = ?";
        try (PreparedStatement check = conn.prepareStatement(checkSql)) {
            check.setInt(1, userId);
            check.setInt(2, postId);
            ResultSet rs = check.executeQuery();
            if (rs.next()) {
                String delete = "DELETE FROM post_likes WHERE user_id = ? AND post_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(delete)) {
                    stmt.setInt(1, userId);
                    stmt.setInt(2, postId);
                    stmt.executeUpdate();
                }
                return false;
            } else {
                String insert = "INSERT INTO post_likes (user_id, post_id) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(insert)) {
                    stmt.setInt(1, userId);
                    stmt.setInt(2, postId);
                    stmt.executeUpdate();
                }
                return true;
            }
        } catch (SQLException e) {
            showError("Toggle Like Error", e);
            return false;
        }
    }

    public boolean addComment(int postId, int userId, String content) {
        String sql = "INSERT INTO comments (post_id, user_id, content, timestamp) VALUES (?, ?, ?, NOW())";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            stmt.setString(3, content);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showError("Add Comment Error", e);
            return false;
        }
    }

    private ArrayList<User> getLikesForPost(int postId) {
        ArrayList<User> likes = new ArrayList<>();
        String sql = "SELECT u.id, u.first_name, u.last_name, u.email FROM post_likes pl " +
                     "JOIN users u ON pl.user_id = u.id WHERE pl.post_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                likes.add(new User(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    "", "registered"
                ));
            }
        } catch (SQLException e) {
            showError("Get Likes Error", e);
        }
        return likes;
    }

    private ArrayList<Comment> getCommentsForPost(int postId) {
        ArrayList<Comment> comments = new ArrayList<>();
        String sql = "SELECT c.id, c.user_id, c.content, c.timestamp, u.first_name, u.last_name, u.email " +
                     "FROM comments c JOIN users u ON c.user_id = u.id " +
                     "WHERE c.post_id = ? ORDER BY c.timestamp";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User(
                    rs.getInt("user_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    "", "registered"
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
            showError("Get Comments Error", e);
        }
        return comments;
        
    }
    public boolean hasUserLiked(int userId, int postId) {
        String sql = "SELECT * FROM post_likes WHERE user_id = ? AND post_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, postId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("❌ Error checking like status: " + e.getMessage());
            return false;
        }
    }


    private void showError(String title, SQLException e) {
        System.err.println("❌ " + title + ": " + e.getMessage());
        javax.swing.SwingUtilities.invokeLater(() ->
            JOptionPane.showMessageDialog(null,
                "Database Error: " + e.getMessage(),
                title,
                JOptionPane.ERROR_MESSAGE)
        );
    }
}
