package views;

import Controller.MarketplaceController;
import Model.Item;
import Model.User;
import util.LanguageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class MarketplaceUI extends jFrame {
    private final MarketplaceController controller = new MarketplaceController();
    private User currentUser;
    private jbutton backbutton, addBtn, chooseImageBtn, messageBtn, deleteBtn, updateBtn;
    private jtextfield nameField, descField, priceField;
    private DefaultListModel<String> model;
    private JList<String> itemList;
    private JLabel imageLabel;
    private String selectedImagePath = "";

    public MarketplaceUI(User user) {
        this.currentUser = user;

        setTitle("Marketplace");
        setSize(550, 550);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(guiCons.lightpink);

        // 🏷 Title Label
        jlabel label = new jlabel("Marketplace Items:", 18, guiCons.White, Font.BOLD);
        label.setBounds(30, 15, 300, 25);
        add(label);

        // 🔤 Input Fields
        nameField = new jtextfield("Name");
        descField = new jtextfield("Description");
        priceField = new jtextfield("0.00");

        nameField.setBounds(30, 50, 100, 40);
        descField.setBounds(140, 50, 120, 40);
        priceField.setBounds(270, 50, 80, 40);

        add(nameField);
        add(descField);
        add(priceField);

        // 🖼️ Choose Image Button
        chooseImageBtn = new jbutton("Choose Image", 20, 14);
        chooseImageBtn.setBounds(370, 50, 120, 40);
        chooseImageBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(MarketplaceUI.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    selectedImagePath = selectedFile.getAbsolutePath(); // store image path
                    JOptionPane.showMessageDialog(MarketplaceUI.this, "Image selected: " + selectedFile.getName());
                }
            }
        });
        add(chooseImageBtn);

        // ➕ Add Button
        addBtn = new jbutton("Add Item", 25, 14);
        addBtn.setBounds(30, 95, 140, 40);
        addBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    double price = Double.parseDouble(priceField.getText());
                    Item item = new Item(0, nameField.getText(), descField.getText(), price, currentUser, selectedImagePath);
                    controller.addItem(item);
                    refreshList();
                    JOptionPane.showMessageDialog(MarketplaceUI.this, "Item added!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MarketplaceUI.this, "Please enter a valid price.");
                }
            }
        });
        add(addBtn);

        // 📃 Item List
        model = new DefaultListModel<>();
        itemList = new JList<>(model);
        itemList.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        itemList.setBackground(guiCons.Background);
        itemList.setForeground(guiCons.Hint1);
        itemList.setSelectionBackground(guiCons.lightpink);

        JScrollPane scrollPane = new JScrollPane(itemList);
        scrollPane.setBounds(30, 150, 460, 180);
        scrollPane.setBorder(BorderFactory.createLineBorder(guiCons.lightpink, 2));
        add(scrollPane);

        // 🖼 Image Preview Label
        imageLabel = new JLabel();
        imageLabel.setBounds(320, 340, 170, 100);
        imageLabel.setBorder(BorderFactory.createLineBorder(guiCons.White));
        add(imageLabel);

        // 🖱️ Show image on item select
        itemList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = itemList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Item selectedItem = controller.getAllItems().get(selectedIndex);
                    String path = selectedItem.getImagePath();
                    if (path != null && !path.isEmpty()) {
                        ImageIcon icon = new ImageIcon(path);
                        Image scaled = icon.getImage().getScaledInstance(170, 100, Image.SCALE_SMOOTH);
                        imageLabel.setIcon(new ImageIcon(scaled));
                    } else {
                        imageLabel.setIcon(null);
                    }
                }
            }
        });

        // 📩 Message Seller Button
        messageBtn = new jbutton("Message Seller", 25, 14);
        messageBtn.setBounds(30, 360, 160, 40);
        messageBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedIndex = itemList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Item selectedItem = controller.getAllItems().get(selectedIndex);
                    int sellerId = selectedItem.getSeller().getId();
                    int itemId = selectedItem.getId();
                    new MessagingUI(currentUser, sellerId, itemId);
                } else {
                    JOptionPane.showMessageDialog(MarketplaceUI.this, "Please select an item first.");
                }
            }
        });
        add(messageBtn);

        // ❌ Delete Button
        deleteBtn = new jbutton("Delete Item", 25, 14);
        deleteBtn.setBounds(30, 410, 140, 40);
        deleteBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedIndex = itemList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Item selectedItem = controller.getAllItems().get(selectedIndex);
                    if (selectedItem.getSeller().getId() == currentUser.getId()) {
                        controller.deleteItem(selectedItem.getId());
                        refreshList();
                        imageLabel.setIcon(null);
                        JOptionPane.showMessageDialog(MarketplaceUI.this, "Item deleted.");
                    } else {
                        JOptionPane.showMessageDialog(MarketplaceUI.this, "You can only delete your own items.");
                    }
                }
            }
        });
        add(deleteBtn);

        // 🔄 Update Button
        updateBtn = new jbutton("Update Item", 25, 14);
        updateBtn.setBounds(190, 410, 140, 40);
        updateBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedIndex = itemList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Item selectedItem = controller.getAllItems().get(selectedIndex);
                    if (selectedItem.getSeller().getId() == currentUser.getId()) {
                        try {
                            selectedItem.setName(nameField.getText());
                            selectedItem.setDescription(descField.getText());
                            selectedItem.setPrice(Double.parseDouble(priceField.getText()));
                            selectedItem.setImagePath(selectedImagePath);
                            controller.updateItem(selectedItem);
                            refreshList();
                            JOptionPane.showMessageDialog(MarketplaceUI.this, "Item updated.");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(MarketplaceUI.this, "Failed to update. Make sure all fields are valid.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(MarketplaceUI.this, "You can only update your own items.");
                    }
                }
            }
        });
        add(updateBtn);

        // 🔙 Back Button
        backbutton = new jbutton(LanguageManager.t("back"), 30, 16);
        backbutton.setBounds(370, 460, 120, 40);
        backbutton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new HomeUI(currentUser);
            }
        });
        add(backbutton);

        refreshList();
        setVisible(true);
    }

    private void refreshList() {
        model.clear();
        ArrayList<Item> items = controller.getAllItems();
        for (Item item : items) {
            model.addElement(item.getName() + " - Rs." + item.getPrice() + " (" + item.getSeller().getFirstName() + ")");
        }
    }
}
