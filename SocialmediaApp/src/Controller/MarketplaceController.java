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

    // ✅ 1. Add Item
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

    // ✅ 2. Get All Items
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
                    rs.getString("image_path")
                );

                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("❌ Fetch Items Error: " + e.getMessage());
        }
        return items;
    }

    // ✅ 3. Get Items By Seller (Current User)
    public ArrayList<Item> getItemsBySeller(int sellerId) {
        ArrayList<Item> items = new ArrayList<>();
        String sql = "SELECT i.*, u.first_name, u.last_name, u.email " +
                     "FROM items i JOIN users u ON i.seller_id = u.id WHERE i.seller_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sellerId);
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
                    rs.getString("image_path")
                );

                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("❌ Fetch Items by Seller Error: " + e.getMessage());
        }
        return items;
    }

    // ✅ 4. Search Items by name or description
    public ArrayList<Item> searchItems(String keyword) {
        ArrayList<Item> items = new ArrayList<>();
        String sql = "SELECT i.*, u.first_name, u.last_name, u.email " +
                     "FROM items i JOIN users u ON i.seller_id = u.id " +
                     "WHERE LOWER(i.name) LIKE ? OR LOWER(i.description) LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchTerm = "%" + keyword.toLowerCase() + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);
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
                    rs.getString("image_path")
                );

                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("❌ Search Items Error: " + e.getMessage());
        }
        return items;
    }

    // ✅ 5. Get Items Sorted
    public ArrayList<Item> getItemsSorted(String sortBy, boolean ascending) {
        ArrayList<Item> items = new ArrayList<>();
        String direction = ascending ? "ASC" : "DESC";

        // Only allow safe columns
        if (!sortBy.equals("price") && !sortBy.equals("name")) {
            sortBy = "name";
        }

        String sql = "SELECT i.*, u.first_name, u.last_name, u.email " +
                     "FROM items i JOIN users u ON i.seller_id = u.id ORDER BY i." + sortBy + " " + direction;

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
                    rs.getString("image_path")
                );

                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("❌ Sort Items Error: " + e.getMessage());
        }

        return items;
    }

    // ✅ 6. Delete Item
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

    // ✅ 7. Update Item
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
