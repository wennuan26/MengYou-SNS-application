package Controller;

import Model.Community;
import Model.DatabaseConnection;
import Model.User;

import java.sql.*;
import java.util.ArrayList;

public class CommunityController {
    private final Connection conn;

    public CommunityController() {
        this.conn = DatabaseConnection.getConnection();
    }

    // ✅ Create a new community
    public boolean createCommunity(String name, String desc, int ownerId) {
        String sql = "INSERT INTO communities (name, description, owner_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, desc);
            stmt.setInt(3, ownerId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showError("Create Community Error", e);
            return false;
        }
    }

    // ✅ Get all communities
    public ArrayList<Community> getAllCommunities() {
        ArrayList<Community> communities = new ArrayList<>();
        String sql = "SELECT c.*, u.first_name, u.last_name, u.email FROM communities c JOIN users u ON c.owner_id = u.id";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User owner = new User(
                    rs.getInt("owner_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    "", "registered"
                );
                communities.add(new Community(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    owner
                ));
            }
        } catch (SQLException e) {
            showError("Fetch Communities Error", e);
        }
        return communities;
    }

    // ✅ Get communities the user is a member of
    public ArrayList<Community> getCommunitiesByUser(int userId) {
        ArrayList<Community> communities = new ArrayList<>();
        String sql = """
            SELECT c.*, u.first_name, u.last_name, u.email
            FROM communities c
            JOIN users u ON c.owner_id = u.id
            JOIN community_members cm ON cm.community_id = c.id
            WHERE cm.user_id = ?
        """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User owner = new User(
                    rs.getInt("owner_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    "", "registered"
                );
                communities.add(new Community(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    owner
                ));
            }
        } catch (SQLException e) {
            showError("Fetch User Communities Error", e);
        }
        return communities;
    }

    // ✅ Check if user is a member
    public boolean isMember(int userId, int communityId) {
        String sql = "SELECT * FROM community_members WHERE user_id = ? AND community_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, communityId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            showError("Check Membership Error", e);
            return false;
        }
    }

    // ✅ Check if user is an admin (NEW)
    public boolean isAdmin(int userId, int communityId) {
        String sql = "SELECT is_admin FROM community_members WHERE user_id = ? AND community_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, communityId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getBoolean("is_admin");
        } catch (SQLException e) {
            showError("Check Admin Error", e);
            return false;
        }
    }

    // ✅ Join community
    public boolean joinCommunity(int userId, int communityId) {
        String sql = "INSERT INTO community_members (user_id, community_id, is_admin) VALUES (?, ?, FALSE)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, communityId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showError("Join Community Error", e);
            return false;
        }
    }

    // ✅ Leave community
    public boolean leaveCommunity(int userId, int communityId) {
        String sql = "DELETE FROM community_members WHERE user_id = ? AND community_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, communityId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showError("Leave Community Error", e);
            return false;
        }
    }

    // ✅ Get all users in a community
    public ArrayList<User> getCommunityMembers(int communityId) {
        ArrayList<User> members = new ArrayList<>();
        String sql = """
            SELECT u.id, u.first_name, u.last_name, u.email
            FROM community_members cm
            JOIN users u ON cm.user_id = u.id
            WHERE cm.community_id = ?
        """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, communityId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                members.add(new User(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    "", "registered"
                ));
            }
        } catch (SQLException e) {
            showError("Fetch Community Members Error", e);
        }
        return members;
    }

    // ✅ Check if user is the owner of a community
    public boolean isOwner(int userId, int communityId) {
        String sql = "SELECT * FROM communities WHERE id = ? AND owner_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, communityId);
            stmt.setInt(2, userId);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            showError("Check Ownership Error", e);
            return false;
        }
    }
            // ✅ Like or Unlike a Post (toggle)
    public boolean toggleLike(int userId, int postId) {
        String checkSql = "SELECT * FROM post_likes WHERE user_id = ? AND post_id = ?";
        String insertSql = "INSERT INTO post_likes (user_id, post_id) VALUES (?, ?)";
        String deleteSql = "DELETE FROM post_likes WHERE user_id = ? AND post_id = ?";

            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, userId);
                checkStmt.setInt(2, postId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    // Already liked, so unlike
                    try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                        deleteStmt.setInt(1, userId);
                        deleteStmt.setInt(2, postId);
                        return deleteStmt.executeUpdate() > 0;
                    }
                } else {
                    // Not yet liked, so like
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setInt(1, userId);
                        insertStmt.setInt(2, postId);
                        return insertStmt.executeUpdate() > 0;
                    }
                }
            } catch (SQLException e) {
                showError("Toggle Like Error", e);
                return false;
            }
        }

        // ✅ Add a comment to a post
        public boolean addComment(int userId, int postId, String content) {
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


    // ⚠️ Common error display method
    private void showError(String title, SQLException e) {
        System.err.println("❌ " + title + ": " + e.getMessage());
        javax.swing.SwingUtilities.invokeLater(() -> {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Database Error: " + e.getMessage(),
                    title,
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        });
    }
}
