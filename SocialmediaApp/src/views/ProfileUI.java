package views;

import Controller.UserController;
import Model.User;

import javax.swing.*;
import java.awt.*;

import static views.guiCons.*;

public class ProfileUI extends jFrame {
    private User currentUser;
    private final UserController controller = new UserController();
    private UserController userController;
    private jtextfield emailField, passwordField;

    public ProfileUI(User user, UserController controller) {
        this.currentUser = user;
        this.userController = controller;

        setTitle("Edit Profile");
        setSize(600, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND);
        setContentPane(mainPanel);

        // Top (Back)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(BACKGROUND);
        jbutton backBtn = new jbutton("Back", 24, 14);
        backBtn.setPreferredSize(new Dimension(100, 40));
        backBtn.addActionListener(e -> {
            dispose();
            new HomeUI(currentUser);
        });
        topPanel.add(backBtn);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center (User Info + Edit Fields)
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(BACKGROUND);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        jlabel infoLabel = new jlabel("Welcome, " + currentUser.getFirstName(), 20, TEXT, Font.BOLD);
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(infoLabel);
        centerPanel.add(Box.createVerticalStrut(30));

        emailField = new jtextfield("");
        passwordField = new jtextfield("");

        jlabel emailLabel = new jlabel("Email", 16, TEXT, Font.PLAIN);
        jlabel passwordLabel = new jlabel("New Password (optional)", 16, TEXT, Font.PLAIN);

        centerPanel.add(wrapField(emailLabel, emailField, new Dimension(300, 40)));
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(wrapField(passwordLabel, passwordField, new Dimension(300, 40)));
        centerPanel.add(Box.createVerticalStrut(30));

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom (Save Button)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(BACKGROUND);

        jbutton saveBtn = new jbutton("Save Changes", 22, 14);
        saveBtn.setPreferredSize(new Dimension(150, 45));
        saveBtn.addActionListener(e -> saveChanges());

        bottomPanel.add(saveBtn);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        saveBtn.setBackground(BUTTONS);
        saveBtn.setForeground(BACKGROUND);

        loadUserInfo();
        setVisible(true);
    }

    private JPanel wrapField(JLabel label, JComponent field, Dimension size) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(BACKGROUND);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setPreferredSize(size);
        field.setMaximumSize(size);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(label);
        wrapper.add(Box.createVerticalStrut(5));
        wrapper.add(field);
        return wrapper;
    }

    private void loadUserInfo() {
        String email = userController.getEmailById(currentUser.getId());
        if (email != null) {
            emailField.setText(email);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to load email.");
        }
    }

    private void saveChanges() {
        String newEmail = emailField.getText().trim();
        String newPassword = passwordField.getText().trim();

        boolean emailUpdated = userController.updateEmail(currentUser.getId(), newEmail);
        boolean passwordUpdated = true;

        if (!newPassword.isEmpty()) {
            passwordUpdated = userController.updatePassword(currentUser.getId(), newPassword);
        }

        if (emailUpdated && passwordUpdated) {
            JOptionPane.showMessageDialog(this, "Profile updated successfully!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update profile.");
        }
    }
}
