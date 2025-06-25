package controller;

import Model.database;
import Model.post;
import Model.user;

import java.sql.*;
import java.util.ArrayList;

public class postcontroller {
    private final database db;

    public postcontroller(database db) {
        this.db = db;
    }

    public void createPost(user u, String content) {
        String sql = "INSERT INTO Posts(UserID, Content, DateTime) VALUES (?, ?, NOW())";

        try {
            PreparedStatement stmt = db.getConnection().prepareStatement(sql);
            stmt.setInt(1, u.getID());
            stmt.setString(2, content);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<post> getAllPosts() {
        ArrayList<post> posts = new ArrayList<>();

        String sql = "SELECT Posts.*, Users.Fname, Users.Lname FROM Posts JOIN Users ON Posts.UserID = Users.ID ORDER BY DateTime DESC";

        try {
            PreparedStatement stmt = db.getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                post p = new post();
                p.setID(rs.getInt("ID"));
                p.setContent(rs.getString("Content"));

                user u = new user();
                u.setID(rs.getInt("UserID"));
                u.setFname(rs.getString("Fname"));
                u.setLname(rs.getString("Lname"));
                p.setUser(u);

                p.setDateTime(rs.getTimestamp("DateTime").toLocalDateTime());

                posts.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return posts;
    }
}
