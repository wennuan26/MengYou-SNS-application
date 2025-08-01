/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Model.DatabaseConnection;

public class AdminController {
    private Connection conn;

    public AdminController() {
        this.conn = DatabaseConnection.getConnection();
    }

    public boolean changeUserRole(int userId, String newRole) {
        try {
            String sql = "UPDATE users SET role = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newRole);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Failed to change role: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteUser(int userId) {
        try {
            String sql = "DELETE FROM users WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Failed to delete user: " + e.getMessage());
            return false;
        }
    }
}
