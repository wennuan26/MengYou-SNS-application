package views;

import Model.database;
import Model.user;
import controller.createuser;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

public class welcome {
    JFrame frame;
    JPanel panel;
    jtextfield firstName, lastName, email;
    jpasswordfield password, confirmPassword;
    jbutton createACC;
    JLabel login;
    JButton langSwitch;
    JLabel titleLabel;

    public welcome(database db) {
        frame = new JFrame("MengYou梦友");

        panel = new JPanel(new BorderLayout());
        panel.setBackground(guiCons.pink);
        frame.setResizable(false);
        panel.setBorder(BorderFactory.createEmptyBorder(53, 84, 76, 84));

        titleLabel = new JLabel(lang.get("name"), JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleLabel.setForeground(guiCons.White);

        langSwitch = new JButton(lang.get("lang_switch"));
        langSwitch.setFocusPainted(false);
        langSwitch.setForeground(guiCons.lightpink);
        langSwitch.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        langSwitch.setBackground(guiCons.pink);
        langSwitch.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        langSwitch.addActionListener((ActionEvent e) -> {
            lang.toggleLanguage();
            refreshText();
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(langSwitch, BorderLayout.EAST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        panel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        centerPanel.setBackground(guiCons.pink);
        centerPanel.setForeground(guiCons.Hint);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(22, 150, 17, 150));

        firstName = new jtextfield(lang.get("first_name"));
        lastName = new jtextfield(lang.get("last_name"));
        email = new jtextfield(lang.get("email"));
        password = new jpasswordfield(lang.get("password"));
        confirmPassword = new jpasswordfield(lang.get("confirm_password"));
        createACC = new jbutton(lang.get("create_account"), 50, 18);

        centerPanel.add(firstName);
        centerPanel.add(lastName);
        centerPanel.add(email);
        centerPanel.add(password);
        centerPanel.add(confirmPassword);
        centerPanel.add(createACC);

        panel.add(centerPanel, BorderLayout.CENTER);

        login = new JLabel(lang.get("already_have"), JLabel.CENTER);
        login.setForeground(guiCons.Hint1);
        login.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(login, BorderLayout.SOUTH);

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setSize(900, 625);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        createACC.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                String first = firstName.getText().trim();
                String last = lastName.getText().trim();
                String emailText = email.getText().trim();
                String pass = new String(password.getPassword()).trim();
                String confirmPass = new String(confirmPassword.getPassword()).trim();

                if (first.isEmpty() || last.isEmpty() || emailText.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, lang.get("alert_empty_fields"));
                    return;
                }
                if (!pass.equals(confirmPass)) {
                    JOptionPane.showMessageDialog(frame, lang.get("alert_password_mismatch"));
                    return;
                }
                if (pass.length() < 6) {
                    JOptionPane.showMessageDialog(frame, lang.get("password_too_short"));
                    return;
                }

                user u = new user();
                u.setFname(first);
                u.setLname(last);
                u.setEmail(emailText);
                u.setPassword(pass);

                createuser create = new createuser(u, db);
                if (!create.isEmailUsed()) {
                    create.create();
                    JOptionPane.showMessageDialog(frame, lang.get("account_created_successfully") + " ID: " + u.getID());
                } else {
                    JOptionPane.showMessageDialog(frame, lang.get("email_already_used"));
                }
            }

            @Override public void mousePressed(java.awt.event.MouseEvent e) {}
            @Override public void mouseReleased(java.awt.event.MouseEvent e) {}
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {}
            @Override public void mouseExited(java.awt.event.MouseEvent e) {}
        });
    }

    private void refreshText() {
        titleLabel.setText(lang.get("name"));
        firstName.setText(lang.get("first_name"));
        lastName.setText(lang.get("last_name"));
        email.setText(lang.get("email"));
        password.setText(lang.get("password"));
        confirmPassword.setText(lang.get("confirm_password"));
        createACC.setText(lang.get("create_account"));
        login.setText(lang.get("already_have"));
        langSwitch.setText(lang.get("lang_switch"));
    }
}
