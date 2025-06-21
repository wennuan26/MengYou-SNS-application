package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import Model.database;
import Model.user;

public class createuser {

    private final user u;
    private final database db;

    public createuser(user u, database db) {
        this.u = u;
        this.db = db;
    }

    public void create() {
        if (u == null) return;

        String insert = "INSERT INTO `Users`(`Fname`, `Lname`, `Email`, `Password`) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement stmt = db.getConnection().prepareStatement(insert);
            stmt.setString(1, u.getFname());
            stmt.setString(2, u.getLname());
            stmt.setString(3, u.getEmail());
            stmt.setString(4, u.getPassword());

            stmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isEmailUsed() {
        if (u == null) return false;

        String select = "SELECT * FROM `Users` WHERE `Email` = ?";
        boolean used = false;

        try {
            PreparedStatement stmt = db.getConnection().prepareStatement(select);
            stmt.setString(1, u.getEmail());

            ResultSet rs = stmt.executeQuery();
            used = rs.next();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        return used;
    }

    public user getUser() {
        if (u == null) return null;

        u.setComments(new ArrayList<>());
        u.setFriends(new ArrayList<>());
        u.setLikes(new ArrayList<>());
        u.setPosts(new ArrayList<>());

        String select = "SELECT `ID` FROM `Users` WHERE `Email` = ? AND `Password` = ?";

        try {
            PreparedStatement stmt = db.getConnection().prepareStatement(select);
            stmt.setString(1, u.getEmail());
            stmt.setString(2, u.getPassword());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                u.setID(rs.getInt("ID"));
            } else {
                JOptionPane.showMessageDialog(null, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return u;
    }
}
