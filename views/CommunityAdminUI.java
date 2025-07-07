package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

import Model.User;

@SuppressWarnings("serial")
public class CommunityAdminUI extends jFrame {
    private User currentUser;
    private int communityId;
    private Connection conn;
    private JTable memberTable;
    private DefaultTableModel tableModel;
    private jtextfield searchField;

    public CommunityAdminUI(User user, int communityId) {
        this.currentUser = user;
        this.communityId = communityId;

        setTitle("Community Admin Panel");
        setSize(750, 600);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(guiCons.lightpink);

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mengyou_db", "root", "@wennuan_26");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database connection failed.");
            return;
        }

        // 🔍 Search Bar
        searchField = new jtextfield("");
        searchField.setBounds(30, 20, 300, 35);
        add(searchField);

        jbutton searchBtn = new jbutton("Search & Add", 20, 13);
        searchBtn.setBounds(350, 20, 150, 35);
        searchBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                searchAndAddUser();
            }
        });
        add(searchBtn);

        // 👥 Members Table
        tableModel = new DefaultTableModel(new String[]{"User ID", "Name", "Admin"}, 0);
        memberTable = new JTable(tableModel) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JScrollPane scrollPane = new JScrollPane(memberTable);
        scrollPane.setBounds(30, 80, 680, 350);
        add(scrollPane);

        // 🔧 Action Buttons
        jbutton removeBtn = new jbutton("Remove Selected", 22, 14);
        removeBtn.setBounds(100, 460, 180, 40);
        removeBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                removeSelected();
            }
        });
        add(removeBtn);

        jbutton promoteBtn = new jbutton("Promote to Admin", 22, 14);
        promoteBtn.setBounds(320, 460, 180, 40);
        promoteBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                promoteSelected();
            }
        });
        add(promoteBtn);

        jbutton backBtn = new jbutton("Back", 22, 14);
        backBtn.setBounds(540, 460, 100, 40);
        backBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose();
                new CommunityUI(currentUser);
            }
        });
        add(backBtn);

        loadMembers();

        setVisible(true);
    }

    private void loadMembers() {
        try {
            tableModel.setRowCount(0);
            PreparedStatement ps = conn.prepareStatement(
                "SELECT u.id, CONCAT(u.first_name, ' ', u.last_name) AS name, cm.is_admin " +
                "FROM community_members cm JOIN users u ON cm.user_id = u.id WHERE cm.community_id = ?"
            );
            ps.setInt(1, communityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("name"));
                row.add(rs.getBoolean("is_admin") ? "Yes" : "No");
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchAndAddUser() {
        String keyword = searchField.getText().trim();
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT id, CONCAT(first_name, ' ', last_name) AS name FROM users " +
                "WHERE (first_name LIKE ? OR last_name LIKE ?) AND id NOT IN " +
                "(SELECT user_id FROM community_members WHERE community_id = ?)"
            );
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setInt(3, communityId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int uid = rs.getInt("id");
                String name = rs.getString("name");

                int confirm = JOptionPane.showConfirmDialog(this, "Add " + name + " to this community?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO community_members (community_id, user_id, is_admin) VALUES (?, ?, FALSE)"
                    );
                    insert.setInt(1, communityId);
                    insert.setInt(2, uid);
                    insert.executeUpdate();
                    JOptionPane.showMessageDialog(this, name + " added.");
                    loadMembers();
                }
            } else {
                JOptionPane.showMessageDialog(this, "No matching user found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeSelected() {
        int row = memberTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "No member selected.");
            return;
        }

        int uid = (int) tableModel.getValueAt(row, 0);
        if (uid == currentUser.getId()) {
            JOptionPane.showMessageDialog(this, "You cannot remove yourself.");
            return;
        }

        try {
            PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM community_members WHERE community_id = ? AND user_id = ?"
            );
            ps.setInt(1, communityId);
            ps.setInt(2, uid);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Member removed.");
            loadMembers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void promoteSelected() {
        int row = memberTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "No member selected.");
            return;
        }

        int uid = (int) tableModel.getValueAt(row, 0);

        try {
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE community_members SET is_admin = TRUE WHERE community_id = ? AND user_id = ?"
            );
            ps.setInt(1, communityId);
            ps.setInt(2, uid);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "User promoted to admin.");
            loadMembers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
