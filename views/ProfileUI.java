import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import javax.swing.*;

public class ProfileUI extends JFrame {
    private int userId;
    private JTextField displayNameField, emailField;
    private JLabel avatarLabel;
    private String avatarPath = "";
    private Connection connection;

    public ProfileUI(int userId) {
        this.userId = userId;
        setTitle("Edit Profile");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.DARK_GRAY);
        setLayout(new BorderLayout());

        // Connect to DB
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mengyou_db", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed");
            return;
        }

        // Load current profile info
        loadProfileInfo();

        // Center Panel
        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        centerPanel.setBackground(Color.DARK_GRAY);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        avatarLabel = new JLabel("Click to upload picture", SwingConstants.CENTER);
        avatarLabel.setOpaque(true);
        avatarLabel.setBackground(Color.LIGHT_GRAY);
        avatarLabel.setForeground(Color.BLACK);
        avatarLabel.setPreferredSize(new Dimension(100, 100));
        avatarLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        avatarLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                uploadAvatar();
            }
        });

        displayNameField = new JTextField();
        emailField = new JTextField();

        centerPanel.add(avatarLabel);
        centerPanel.add(new LabeledField("Display Name:", displayNameField));
        centerPanel.add(new LabeledField("Email:", emailField));

        // Save button
        JButton saveBtn = new JButton("Save Changes");
        saveBtn.setBackground(new Color(0x1ABC9C));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> updateProfile());

        centerPanel.add(saveBtn);
        add(centerPanel, BorderLayout.CENTER);
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
                emailField = new JTextField(rs.getString("email"));
                displayNameField = new JTextField(rs.getString("display_name"));
                avatarPath = rs.getString("avatar_url");
                avatarLabel = new JLabel("<html><center>Click to change<br>Avatar</center></html>", SwingConstants.CENTER);
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

    // Helper class for labels + fields
    class LabeledField extends JPanel {
        public LabeledField(String label, JTextField field) {
            setLayout(new BorderLayout());
            setBackground(Color.DARK_GRAY);
            JLabel lbl = new JLabel(label);
            lbl.setForeground(Color.WHITE);
            add(lbl, BorderLayout.WEST);
            add(field, BorderLayout.CENTER);
        }
    }

    // For testing
    public static void main(String[] args) {
        new ProfileUI(1); // test user_id
    }
}
