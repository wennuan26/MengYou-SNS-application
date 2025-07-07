package views;

import Controller.CommunityController;
import Model.Community;
import Model.User;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class CommunityUI extends jFrame {
    private final CommunityController controller = new CommunityController();
    private ArrayList<Community> communities; // All communities fetched
    private User currentUser;

    private DefaultListModel<String> model;
    private JList<String> list;
    private jtextfield searchField;

    public CommunityUI(User user) {
        this.currentUser = user;

        setTitle("Communities");
        setSize(520, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // 🏷 Title Label
        jlabel label = new jlabel("Communities:", 18, guiCons.White, Font.BOLD);
        label.setBounds(30, 15, 300, 25);
        add(label);

        // 🔍 Search Field (auto-search enabled)
        searchField = new jtextfield("Search communities...");
        searchField.setBounds(30, 45, 220, 40);
        add(searchField);

        // 🔍 Search Button (optional, triggers search manually)
        jbutton searchBtn = new jbutton("Search", 22, 14);
        searchBtn.setBounds(260, 45, 90, 40);
        add(searchBtn);

        // ↩ Reset Button clears search & shows all
        jbutton resetBtn = new jbutton("Reset", 22, 14);
        resetBtn.setBounds(360, 45, 90, 40);
        add(resetBtn);

        // 🔤 Input Fields for new community
        jbutton addBtn = new jbutton("Create", 25, 14);

        addBtn.setBounds(360, 95, 100, 40);

        add(addBtn);

        // 📋 Community List
        model = new DefaultListModel<>();
        list = new JList<>(model);
        list.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        list.setBackground(guiCons.Background);
        list.setForeground(guiCons.Hint1);
        list.setSelectionBackground(guiCons.lightpink);

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBounds(30, 150, 430, 250);
        scroll.setBorder(BorderFactory.createLineBorder(guiCons.lightpink, 2));
        add(scroll);

        // 🔁 Load all communities initially
        refreshList(null);

        // ➕ Create Button Action
        addBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new CommunityCreationUI(currentUser);

                refreshList(searchField.getText().trim());
            }
        });

        // 🔍 Search Button Action (manual search)
        searchBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                refreshList(searchField.getText().trim());
            }
        });

        // ↩ Reset Button clears search and reloads all
        resetBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                searchField.setText("");
                refreshList(null);
            }
        });

        // 🔍 Auto-search as user types
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                refreshList(searchField.getText().trim());
            }
            public void removeUpdate(DocumentEvent e) {
                refreshList(searchField.getText().trim());
            }
            public void changedUpdate(DocumentEvent e) {
                refreshList(searchField.getText().trim());
            }
        });

        // ➡️ Double-click community to open CommunityAdminUI if owner
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = list.locationToIndex(e.getPoint());
                    if (index >= 0 && index < model.size()) {
                        String selectedValue = model.getElementAt(index);
                        Community selectedCommunity = null;
                        for (Community c : communities) {
                            String display = c.getName() + " by " + c.getOwner().getFirstName();
                            if (display.equals(selectedValue)) {
                                selectedCommunity = c;
                                break;
                            }
                        }
                        if (selectedCommunity != null) {
                            if (selectedCommunity.getOwner().getId() == currentUser.getId()) {
                                new CommunityAdminUI(currentUser, selectedCommunity.getId());
                            } else {
                                JOptionPane.showMessageDialog(null, "You are not the owner of this community.");
                            }
                        }
                    }
                }
            }
        });

        // 🔙 Back Button to HomeUI
        jbutton backBtn = new jbutton("Back", 22, 14);
        backBtn.setBounds(360, 420, 100, 40);
        backBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                new HomeUI(currentUser);
            }
        });
        add(backBtn);

        setVisible(true);
    }

    /**
     * Refreshes community list by filtering communities where
     * name OR description OR owner's first name contains the keyword (case-insensitive).
     * If keyword is null or empty, shows all communities.
     */
    private void refreshList(String keyword) {
        communities = controller.getAllCommunities();
        model.clear();

        if (keyword == null || keyword.isEmpty()) {
            // Show all communities
            for (Community c : communities) {
                model.addElement(c.getName() + " by " + c.getOwner().getFirstName());
            }
        } else {
            String lowerKeyword = keyword.toLowerCase();
            for (Community c : communities) {
                boolean matches = c.getName().toLowerCase().contains(lowerKeyword)
                        || c.getDescription().toLowerCase().contains(lowerKeyword)
                        || c.getOwner().getFirstName().toLowerCase().contains(lowerKeyword);
                if (matches) {
                    model.addElement(c.getName() + " by " + c.getOwner().getFirstName());
                }
            }
        }
    }
}
