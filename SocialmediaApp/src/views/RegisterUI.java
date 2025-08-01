package views;

import Controller.UserController;
import util.LanguageManager;
import javax.swing.*;
import java.awt.*;


@SuppressWarnings("serial")
public class RegisterUI extends jFrame {
    private jtextfield fnameField, lnameField, emailField;
    private jpasswordfield passwordField;
    private jbutton registerButton, backButton, langButton;
    private jlabel fnameLabel, lnameLabel, emailLabel, passwordLabel;

    public RegisterUI() {
        setTitle(LanguageManager.t("register"));
        setSize(500, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(guiCons.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));  // less wide padding

        // Language toggle button
        langButton = new jbutton(LanguageManager.t("switch"), 20, 14);
        langButton.setForeground(guiCons.BACKGROUND);
        langButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        langButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        langButton.setPreferredSize(new Dimension(90, 30));  // fixed size for better alignment

        langButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                LanguageManager.toggleLanguage();
                refreshText();
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        // Reserve title space
        JLabel emptySpace = new JLabel();
        emptySpace.setPreferredSize(new Dimension(0, 50));
        topPanel.add(emptySpace, BorderLayout.CENTER);
        topPanel.add(langButton, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        // Center form with GridLayout - adjust rows and gaps
        JPanel centerPanel = new JPanel(new GridLayout(9, 1, 0, 12)); // less vertical gap, fewer rows for better spacing
        centerPanel.setBackground(guiCons.BACKGROUND);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));  // less wide padding

        fnameLabel = new jlabel(LanguageManager.t("first_name"), 16, guiCons.TEXT, Font.PLAIN);
        lnameLabel = new jlabel(LanguageManager.t("last_name"), 16, guiCons.TEXT, Font.PLAIN);
        emailLabel = new jlabel(LanguageManager.t("email"), 16, guiCons.TEXT, Font.PLAIN);
        passwordLabel = new jlabel(LanguageManager.t("password"), 16, guiCons.TEXT, Font.PLAIN);

        fnameField = new jtextfield(LanguageManager.t("first_name"));
        lnameField = new jtextfield(LanguageManager.t("last_name"));
        emailField = new jtextfield(LanguageManager.t("email"));
        passwordField = new jpasswordfield(LanguageManager.t("password"));

        // Set preferred size for text fields
        Dimension fieldSize = new Dimension(300, 35);
        fnameField.setPreferredSize(fieldSize);
        lnameField.setPreferredSize(fieldSize);
        emailField.setPreferredSize(fieldSize);
        passwordField.setPreferredSize(fieldSize);

        registerButton = new jbutton(LanguageManager.t("register"), 30, 16);
        backButton = new jbutton(LanguageManager.t("back"), 30, 16);

        // Make buttons uniform size
        Dimension buttonSize = new Dimension(140, 40);
        registerButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);

        centerPanel.add(fnameLabel);
        centerPanel.add(fnameField);
        centerPanel.add(lnameLabel);
        centerPanel.add(lnameField);
        centerPanel.add(emailLabel);
        centerPanel.add(emailField);
        centerPanel.add(passwordLabel);
        centerPanel.add(passwordField);

        // Buttons panel with FlowLayout to align nicely
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        buttonsPanel.setBackground(guiCons.BACKGROUND);
        buttonsPanel.add(registerButton);
        buttonsPanel.add(backButton);
        centerPanel.add(buttonsPanel);

        panel.add(centerPanel, BorderLayout.CENTER);

        // Register button action
        registerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                UserController uc = new UserController();
                boolean success = uc.registerUser(
                        fnameField.getText(),
                        lnameField.getText(),
                        emailField.getText(),
                        new String(passwordField.getPassword())
                );
                if (success) {
                    JOptionPane.showMessageDialog(RegisterUI.this, LanguageManager.t("registration_success"));
                    dispose();
                    new LoginUI();
                } else {
                    JOptionPane.showMessageDialog(RegisterUI.this, LanguageManager.t("registration_fail"));
                }
            }
        });

        // Back button action
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                new LoginUI();
            }
        });

        getContentPane().add(panel);
        pack();
        setVisible(true);
    }

    private void refreshText() {
        fnameLabel.setText(LanguageManager.t("first_name"));
        lnameLabel.setText(LanguageManager.t("last_name"));
        emailLabel.setText(LanguageManager.t("email"));
        passwordLabel.setText(LanguageManager.t("password"));
        registerButton.setText(LanguageManager.t("register"));
        backButton.setText(LanguageManager.t("back"));
        langButton.setText(LanguageManager.t("switch"));
        fnameField.setText(LanguageManager.t("first_name"));
        lnameField.setText(LanguageManager.t("last_name"));
        emailField.setText(LanguageManager.t("email"));
        passwordField.setText(LanguageManager.t("password"));
    }
}
