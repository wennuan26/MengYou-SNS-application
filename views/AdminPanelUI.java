package views;

import Controller.AdminController;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class AdminPanelUI extends jFrame {
    public AdminPanelUI() {
        setLayout(null);

        // 🏷 Labels
        jlabel userIdLabel = new jlabel("User ID:", 16, guiCons.White, Font.PLAIN);
        jlabel roleLabel = new jlabel("New Role:", 16, guiCons.White, Font.PLAIN);

        // 🧾 Fields
        jtextfield userIdField = new jtextfield("Enter User ID");

        String[] roles = {"guest", "registered", "admin"};
        JComboBox<String> roleBox = new JComboBox<>(roles);
        roleBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        roleBox.setForeground(guiCons.Hint1);
        roleBox.setBackground(guiCons.Background);
        roleBox.setBounds(160, 90, 180, 40);

        // ✅ Button
        jbutton changeRoleBtn = new jbutton("Change Role", 30, 14);

        // Bounds
        userIdLabel.setBounds(50, 50, 100, 25);
        userIdField.setBounds(160, 50, 180, 40);

        roleLabel.setBounds(50, 90, 100, 25);
        changeRoleBtn.setBounds(100, 150, 200, 40);

        // Add to frame
        add(userIdLabel);
        add(userIdField);
        add(roleLabel);
        add(roleBox);
        add(changeRoleBtn);

        // ⚙️ Action
        changeRoleBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                try {
                    int userId = Integer.parseInt(userIdField.getText());
                    String newRole = (String) roleBox.getSelectedItem();
                    AdminController ac = new AdminController();
                    boolean result = ac.changeUserRole(userId, newRole);
                    JOptionPane.showMessageDialog(AdminPanelUI.this,
                            result ? "Role updated" : "Failed to update role");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AdminPanelUI.this, "Please enter a valid user ID.");
                }
            }
        });

        setVisible(true);
    }
}
