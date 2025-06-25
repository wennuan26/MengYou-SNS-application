/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.DatabaseConnection;
import Model.Item;
import Model.User;

import java.sql.*;
import java.util.ArrayList;

public class MarketplaceController {
    private Connection conn;

    public MarketplaceController() {
        this.conn = DatabaseConnection.getConnection();
    }

    public boolean addItem(Item item) {
        String sql = "INSERT INTO items (name, description, price, seller_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setDouble(3, item.getPrice());
            stmt.setInt(4, item.getSeller().getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Add Item Error: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Item> getAllItems() {
        ArrayList<Item> items = new ArrayList<>();
        String sql = "SELECT i.*, u.first_name, u.last_name, u.email FROM items i JOIN users u ON i.seller_id = u.id";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User seller = new User(
                    rs.getInt("seller_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    "", "registered"
                );
                items.add(new Item(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    seller
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Fetch Items Error: " + e.getMessage());
        }
        return items;
    }
}
