/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.DatabaseConnection;
import Model.User;

public class UserController {

    private Connection conn;
    

    public UserController() {
        this.conn = DatabaseConnection.getConnection();
    }

    // Register a new user
    public boolean registerUser(String fname, String lname, String email, String password) {
        try {
            String sql = "INSERT INTO users (first_name, last_name, email, password, role) VALUES (?, ?, ?, ?, 'registered')";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, fname);
            stmt.setString(2, lname);
            stmt.setString(3, email);
            stmt.setString(4, password);

            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("❌ Registration failed: " + e.getMessage());
            return false;
        }
    }

    // Login user
    public User login(String email, String password) {
        try {
            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Login failed: " + e.getMessage());
        }
        return null;
    }
}
