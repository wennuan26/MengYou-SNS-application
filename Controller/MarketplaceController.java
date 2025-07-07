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

    // ✅ Add Item with imagePath
    public boolean addItem(Item item) {
        String sql = "INSERT INTO items (name, description, price, seller_id, image_path) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setDouble(3, item.getPrice());
            stmt.setInt(4, item.getSeller().getId());
            stmt.setString(5, item.getImagePath());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Add Item Error: " + e.getMessage());
            return false;
        }
    }

    // ✅ Get all items with imagePath and seller info
    public ArrayList<Item> getAllItems() {
        ArrayList<Item> items = new ArrayList<>();
        String sql = "SELECT i.*, u.first_name, u.last_name, u.email " +
                     "FROM items i JOIN users u ON i.seller_id = u.id";
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

                Item item = new Item(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    seller,
                    rs.getString("image_path")  // ← add image path here
                );

                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("❌ Fetch Items Error: " + e.getMessage());
        }
        return items;
    }

    // ✅ Delete Item by ID
    public boolean deleteItem(int itemId) {
        String sql = "DELETE FROM items WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Delete Item Error: " + e.getMessage());
            return false;
        }
    }

    // ✅ Update Item
    public boolean updateItem(Item item) {
        String sql = "UPDATE items SET name = ?, description = ?, price = ?, image_path = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setDouble(3, item.getPrice());
            stmt.setString(4, item.getImagePath());
            stmt.setInt(5, item.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Update Item Error: " + e.getMessage());
            return false;
        }
    }
}
