package views;

import Controller.UserController;
import Model.User;
import util.LanguageManager;
import java.awt.*;
import javax.swing.*;
import static views.guiCons.TEXT;

@SuppressWarnings("serial")
public class LoginUI extends jFrame {
    private jbutton loginButton, registerButton, langButton;
    private jlabel emailLabel, passwordLabel;
    private jtextfield emailField;
    private jpasswordfield passwordField;

    public LoginUI() {
        setLayout(new BorderLayout());
        setResizable(false);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(guiCons.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(53, 84, 76, 84));

        // Initialize langButton properly
        langButton = new jbutton(LanguageManager.t("switch"), 20, 14);

        langButton.setForeground(guiCons.BACKGROUND);
        langButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        langButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        // Use MouseListener because jbutton extends JLabel and doesn't have addActionListener
        langButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                LanguageManager.toggleLanguage();
                refreshText();
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        // Empty label to keep space where titleLabel was
        JLabel emptySpace = new JLabel();
        emptySpace.setPreferredSize(new Dimension(0, 50)); // reserve vertical space
        topPanel.add(emptySpace, BorderLayout.CENTER);

        topPanel.add(langButton, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        // Center form with grid layout
        JPanel centerPanel = new JPanel(new GridLayout(6, 1, 10, 15));
        centerPanel.setBackground(guiCons.BACKGROUND);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 150, 20, 150));

        emailLabel = new jlabel(LanguageManager.t("email"), 16, guiCons.TEXT, Font.PLAIN);
        passwordLabel = new jlabel(LanguageManager.t("password"), 16, guiCons.TEXT, Font.PLAIN);

        emailField = new jtextfield(LanguageManager.t("email"));
        passwordField = new jpasswordfield(LanguageManager.t("password"));

        loginButton = new jbutton(LanguageManager.t("login"), 30, 16);
        registerButton = new jbutton(LanguageManager.t("register"), 30, 16);

        centerPanel.add(emailLabel);
        centerPanel.add(emailField);
        centerPanel.add(passwordLabel);
        centerPanel.add(passwordField);
        centerPanel.add(new JLabel()); // empty cell
        centerPanel.add(new JLabel()); // empty cell
        centerPanel.add(loginButton);
        centerPanel.add(registerButton);

        panel.add(centerPanel, BorderLayout.CENTER);

        // Button actions
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                UserController uc = new UserController();
                User u = uc.login(emailField.getText(), new String(passwordField.getPassword()));
                if (u != null) {
                    JOptionPane.showMessageDialog(LoginUI.this, LanguageManager.t("welcome") + ", " + u.getFirstName());
                    dispose();
                    new HomeUI(u);
                } else {
                    JOptionPane.showMessageDialog(LoginUI.this, LanguageManager.t("login_failed"));
                }
            }
        });

        registerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                new RegisterUI();
            }
        });

        getContentPane().add(panel);
        setVisible(true);
    }

    private void refreshText() {
        emailLabel.setText(LanguageManager.t("email"));
        passwordLabel.setText(LanguageManager.t("password"));
        loginButton.setText(LanguageManager.t("login"));
        registerButton.setText(LanguageManager.t("register"));
        langButton.setText(LanguageManager.t("switch"));
        emailField.setText(LanguageManager.t("email"));
        passwordField.setText(LanguageManager.t("password"));
    }
}
