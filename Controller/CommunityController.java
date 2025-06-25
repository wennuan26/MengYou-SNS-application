/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Community;
import Model.DatabaseConnection;
import Model.User;

import java.sql.*;
import java.util.ArrayList;

public class CommunityController {
    private Connection conn;

    public CommunityController() {
        this.conn = DatabaseConnection.getConnection();
    }

    public boolean createCommunity(String name, String desc, int ownerId) {
        String sql = "INSERT INTO communities (name, description, owner_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, desc);
            stmt.setInt(3, ownerId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Create Community Error: " + e.getMessage());
            return false;
        }
    }

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
            System.err.println("❌ Fetch Communities Error: " + e.getMessage());
        }
        return communities;
    }
}
