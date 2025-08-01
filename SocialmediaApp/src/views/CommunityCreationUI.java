package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import Model.User;
import static views.guiCons.BACKGROUND;
import static views.guiCons.TEXT;

public class CommunityCreationUI extends jFrame {
    private User currentUser;
    private jtextfield nameField;
    private JTextArea descriptionArea;
    private Connection conn;

    public CommunityCreationUI(User user) {
        this.currentUser = user;
        setTitle("Create Community");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(guiCons.BACKGROUND);

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mengyou_db", "root", "@wennuan_26");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database connection failed.");
            return;
        }

        jlabel titleLabel = new jlabel("Create a New Community", 20, guiCons.TEXT, Font.BOLD);
        titleLabel.setBounds(180, 30, 300, 30);
        add(titleLabel);

        jlabel nameLabel = new jlabel("Community Name:", 16, guiCons.TEXT, Font.PLAIN);
        nameLabel.setBounds(80, 90, 200, 30);
        add(nameLabel);

        nameField = new jtextfield("");
        nameField.setBounds(250, 90, 250, 35);
        add(nameField);

        jlabel descLabel = new jlabel("Description:", 16, guiCons.TEXT, Font.PLAIN);
        descLabel.setBounds(80, 150, 200, 30);
        add(descLabel);

        descriptionArea = new JTextArea();
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(descriptionArea);
        scroll.setBounds(250, 150, 250, 100);
        scroll.setBorder(BorderFactory.createLineBorder(guiCons.BACKGROUND, 2));
        add(scroll);

        jbutton createBtn = new jbutton("Create", 25, 14);
        createBtn.setBounds(230, 300, 140, 45);
        createBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                createCommunity();
            }
        });
        add(createBtn);

        jbutton backBtn = new jbutton("Back", 25, 14);
        backBtn.setBounds(20, 20, 100, 40);
        backBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new CommunityUI(currentUser);
            }
        });
        add(backBtn);

        setVisible(true);
    }

    private void createCommunity() {
        String name = nameField.getText().trim();
        String desc = descriptionArea.getText().trim();

        if (name.isEmpty() || desc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all fields.");
            return;
        }

        try {
            // Insert community
            PreparedStatement create = conn.prepareStatement(
                "INSERT INTO communities (name, description, owner_id) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            create.setString(1, name);
            create.setString(2, desc);
            create.setInt(3, currentUser.getId());
            create.executeUpdate();

            ResultSet rs = create.getGeneratedKeys();
            int newCommunityId = -1;
            if (rs.next()) {
                newCommunityId = rs.getInt(1);
            }

            // Add user as a member (no is_admin column)
            PreparedStatement addOwner = conn.prepareStatement(
                "INSERT INTO community_members (community_id, user_id) VALUES (?, ?)"
            );
            addOwner.setInt(1, newCommunityId);
            addOwner.setInt(2, currentUser.getId());
            addOwner.executeUpdate();

            JOptionPane.showMessageDialog(this, "Community created successfully!");

            dispose();
            new CommunityAdminUI(currentUser, newCommunityId); // still okay to navigate here

        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this, "Community name already exists.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating community.");
        }
    }
}
