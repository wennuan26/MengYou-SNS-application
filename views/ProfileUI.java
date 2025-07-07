package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import Model.User;

public class ProfileUI extends jFrame {
    private int userId;
    private User currentUser;
    private jtextfield displayNameField, emailField;
    private jlabel avatarLabel;
    private String avatarPath = "";
    private Connection connection;

    public ProfileUI(User user) {
        this.currentUser = user;
        this.userId = user.getId();

        setTitle("Edit Profile");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setLayout(null);

        // Connect to DB
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mengyou_db", "root", "@wennuan_26");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed");
            return;
        }

        // 🔙 Back Button
        jbutton backBtn = new jbutton("Back", 25, 14);
        backBtn.setBounds(20, 20, 100, 40);
        backBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose(); // close profile window
                new HomeUI(currentUser); // go back to home
            }
        });
        add(backBtn);

        // 🖼 Avatar Label
        avatarLabel = new jlabel("Click to upload picture", 16, guiCons.lightpink, Font.PLAIN);
        avatarLabel.setBounds(200, 70, 200, 120);
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        avatarLabel.setVerticalAlignment(SwingConstants.CENTER);
        avatarLabel.setBackground(guiCons.White);
        avatarLabel.setOpaque(true);
        avatarLabel.setBorder(BorderFactory.createLineBorder(guiCons.lightpink, 2));
        avatarLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                uploadAvatar();
            }
        });
        add(avatarLabel);

        displayNameField = new jtextfield("");
        emailField = new jtextfield("");

        loadProfileInfo(); // Loads data into fields

        jlabel nameLabel = new jlabel("Display Name:", 16, guiCons.White, Font.PLAIN);
        nameLabel.setBounds(100, 220, 150, 30);
        add(nameLabel);

        displayNameField.setBounds(250, 220, 250, 40);
        add(displayNameField);

        jlabel emailLabel = new jlabel("Email:", 16, guiCons.White, Font.PLAIN);
        emailLabel.setBounds(100, 290, 150, 30);
        add(emailLabel);

        emailField.setBounds(250, 290, 250, 40);
        add(emailField);

        jbutton saveBtn = new jbutton("Save Changes", 25, 14);
        saveBtn.setBounds(225, 400, 150, 45);
        saveBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                updateProfile();
            }
        });
        add(saveBtn);

        setVisible(true);
    }

    private void loadProfileInfo() {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT u.email, p.display_name, p.avatar_url FROM users u JOIN profiles p ON u.id = p.user_id WHERE u.id = ?"
            );
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                emailField.setText(rs.getString("email"));
                displayNameField.setText(rs.getString("display_name"));
                avatarPath = rs.getString("avatar_url");
                avatarLabel.setText("<html><center>Click to<br>change Avatar</center></html>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadAvatar() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File avatarFile = fileChooser.getSelectedFile();
            avatarPath = avatarFile.getAbsolutePath();
            avatarLabel.setText("<html><center>Uploaded<br>✓</center></html>");
        }
    }

    private void updateProfile() {
        try {
            PreparedStatement updateUser = connection.prepareStatement(
                "UPDATE users SET email = ? WHERE id = ?"
            );
            updateUser.setString(1, emailField.getText());
            updateUser.setInt(2, userId);
            updateUser.executeUpdate();

            PreparedStatement updateProfile = connection.prepareStatement(
                "UPDATE profiles SET display_name = ?, avatar_url = ? WHERE user_id = ?"
            );
            updateProfile.setString(1, displayNameField.getText());
            updateProfile.setString(2, avatarPath);
            updateProfile.setInt(3, userId);
            updateProfile.executeUpdate();

            JOptionPane.showMessageDialog(this, "Profile updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Update failed.");
        }
    }
}