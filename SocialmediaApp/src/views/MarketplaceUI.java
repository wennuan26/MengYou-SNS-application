package views;

import Controller.MarketplaceController;
import Model.Item;
import Model.User;
import util.LanguageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;

import static views.guiCons.*;

public class MarketplaceUI extends jFrame {
    private final MarketplaceController controller = new MarketplaceController();
    private final User currentUser;
    private jtextfield nameField, descField, priceField;
    private JLabel imageLabel;
    private String selectedImagePath = "";
    private DefaultListModel<String> model;
    private JList<String> itemList;

    public MarketplaceUI(User user) {
        this.currentUser = user;
        setTitle("Marketplace");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND);
        setContentPane(mainPanel);

        // Top Panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.setBackground(BACKGROUND);

        nameField = new jtextfield("Name");
        descField = new jtextfield("Description");
        priceField = new jtextfield("0.00");
        jbutton chooseImageBtn = new jbutton("Choose Image", 20, 14);

        topPanel.add(wrapField(nameField, new Dimension(120, 40)));
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(wrapField(descField, new Dimension(240, 40)));
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(wrapField(priceField, new Dimension(120, 40)));
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(wrapField(chooseImageBtn, new Dimension(140, 40)));

        mainPanel.add(topPanel, BorderLayout.NORTH);

        chooseImageBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                selectedImagePath = selectedFile.getAbsolutePath();
                JOptionPane.showMessageDialog(this, "Selected: " + selectedFile.getName());
            }
        });

        // Center Panel
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(PANEL);

        model = new DefaultListModel<>();
        itemList = new JList<>(model);
        itemList.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        itemList.setBackground(BACKGROUND);
        itemList.setForeground(BACKGROUND);

        JScrollPane scrollPane = new JScrollPane(itemList);
        scrollPane.setBorder(BorderFactory.createLineBorder(BUTTONS, 2));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(200, 100));
        imageLabel.setBorder(BorderFactory.createLineBorder(TEXT));
        centerPanel.add(imageLabel, BorderLayout.EAST);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bottomPanel.setBackground(BACKGROUND);

        jbutton addBtn = new jbutton("Add", 24, 14);
        jbutton updateBtn = new jbutton("Update", 24, 14);
        jbutton deleteBtn = new jbutton("Delete", 24, 14);
        jbutton messageBtn = new jbutton("Message", 24, 14);
        jbutton inboxBtn = new jbutton("Inbox", 24, 14);
        jbutton backBtn = new jbutton(LanguageManager.t("back"), 24, 14);

        for (jbutton btn : new jbutton[]{addBtn, updateBtn, deleteBtn, messageBtn, inboxBtn, backBtn}) {
            btn.setPreferredSize(new Dimension(130, 40));
            bottomPanel.add(btn);
        }

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Logic
        addBtn.addActionListener(e -> {
            try {
                double price = Double.parseDouble(priceField.getText());
                Item item = new Item(0, nameField.getText(), descField.getText(), price, currentUser, selectedImagePath);
                boolean success = controller.addItem(item);
                if (success) {
                    System.out.println("✅ Item added successfully");
                } else {
                    System.out.println("❌ Failed to add item");
                }
                refreshList();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid price.");
            }
        });

        updateBtn.addActionListener(e -> {
            int index = itemList.getSelectedIndex();
            if (index >= 0) {
                Item item = controller.getAllItems().get(index);
                if (item.getSeller().getId() == currentUser.getId()) {
                    try {
                        item.setName(nameField.getText());
                        item.setDescription(descField.getText());
                        item.setPrice(Double.parseDouble(priceField.getText()));
                        item.setImagePath(selectedImagePath);
                        controller.updateItem(item);
                        refreshList();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Invalid update.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "❌ You can't edit this item.");
                }
            }
        });

        deleteBtn.addActionListener(e -> {
            int index = itemList.getSelectedIndex();
            if (index >= 0) {
                Item item = controller.getAllItems().get(index);
                if (item.getSeller().getId() == currentUser.getId()) {
                    controller.deleteItem(item.getId());
                    refreshList();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ You can't delete this item.");
                }
            }
        });

        messageBtn.addActionListener(e -> {
            int index = itemList.getSelectedIndex();
            if (index >= 0) {
                Item item = controller.getAllItems().get(index);
                if (item.getSeller().getId() == currentUser.getId()) {
                    JOptionPane.showMessageDialog(this, "You cannot message yourself.");
                } else {
                    new MessagingUI(currentUser, item.getSeller().getId(), item.getId());
                }
            }
        });

        inboxBtn.addActionListener(e -> {
            showSellerInbox();
        });

        backBtn.addActionListener(e -> {
            dispose();
            new HomeUI(currentUser);
        });

        itemList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = itemList.getSelectedIndex();
                if (index >= 0) {
                    Item item = controller.getAllItems().get(index);
                    nameField.setText(item.getName());
                    descField.setText(item.getDescription());
                    priceField.setText(String.valueOf(item.getPrice()));
                    try {
                        if (item.getImagePath() != null && !item.getImagePath().isEmpty()) {
                            ImageIcon icon = new ImageIcon(item.getImagePath());
                            Image scaled = icon.getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH);
                            imageLabel.setIcon(new ImageIcon(scaled));
                        } else {
                            imageLabel.setIcon(null);
                        }
                    } catch (Exception ex) {
                        System.err.println("❌ Error loading image: " + ex.getMessage());
                        imageLabel.setIcon(null);
                    }
                }
            }
        });

        refreshList();
        setVisible(true);
    }

    private void showSellerInbox() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mengyou_db", "root", "@wennuan_26")) {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT DISTINCT sender_id, item_id FROM messages WHERE receiver_id = ?"
            );
            ps.setInt(1, currentUser.getId());

            ResultSet rs = ps.executeQuery();
            ArrayList<String> choices = new ArrayList<>();
            ArrayList<Integer> buyerIds = new ArrayList<>();
            ArrayList<Integer> itemIds = new ArrayList<>();

            while (rs.next()) {
                int senderId = rs.getInt("sender_id");
                int itemId = rs.getInt("item_id");
                String buyerName = getUserNameById(senderId, conn);
                choices.add(buyerName + " (Item ID: " + itemId + ")");
                buyerIds.add(senderId);
                itemIds.add(itemId);
            }

            if (choices.isEmpty()) {
                JOptionPane.showMessageDialog(this, "📭 No messages from buyers yet.");
                return;
            }

            String selected = (String) JOptionPane.showInputDialog(this,
                "Select a buyer to reply to:",
                "Inbox",
                JOptionPane.PLAIN_MESSAGE,
                null,
                choices.toArray(),
                null);

            if (selected != null) {
                int idx = choices.indexOf(selected);
                new MessagingUI(currentUser, buyerIds.get(idx), itemIds.get(idx));
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading inbox.");
        }
    }

    private String getUserNameById(int userId, Connection conn) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT first_name FROM users WHERE id = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("first_name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "User(" + userId + ")";
    }

    private JPanel wrapField(JComponent field, Dimension size) {
        JPanel wrapper = new JPanel();
        wrapper.setPreferredSize(size);
        wrapper.setOpaque(false); // Makes the wrapper transparent
        wrapper.setBorder(null);  // Removes any default borders
        wrapper.setLayout(new BorderLayout());
        wrapper.add(field, BorderLayout.CENTER);
        return wrapper;
    }


    private void refreshList() {
        model.clear();
        ArrayList<Item> items = controller.getAllItems();
        for (Item item : items) {
            model.addElement(item.getName() + " - Rs." + item.getPrice() + " (" + item.getSeller().getFirstName() + ")");
        }
    }
}
