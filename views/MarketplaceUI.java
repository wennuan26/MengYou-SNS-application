package views;

import Controller.MarketplaceController;
import Model.Item;
import Model.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import util.LanguageManager;

@SuppressWarnings("serial")
public class MarketplaceUI extends jFrame {
    private final MarketplaceController controller = new MarketplaceController();
    private jbutton backbutton;

    public MarketplaceUI(User user) {
        setLayout(null);

        // 🏷 Title Label
        jlabel label = new jlabel("Marketplace Items:", 18, guiCons.White, Font.BOLD);
        label.setBounds(30, 15, 300, 25);
        add(label);

        // 🔤 Input Fields
        jtextfield nameField = new jtextfield("Name");
        jtextfield descField = new jtextfield("Description");
        jtextfield priceField = new jtextfield("0.00");

        nameField.setBounds(30, 50, 100, 40);
        descField.setBounds(140, 50, 120, 40);
        priceField.setBounds(270, 50, 80, 40);

        // ➕ Add Button
        jbutton addBtn = new jbutton("Add Item", 25, 14);
        addBtn.setBounds(370, 50, 100, 40);

        add(nameField);
        add(descField);
        add(priceField);
        add(addBtn);

        // 📃 Item List
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> itemList = new JList<>(model);
        itemList.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        itemList.setBackground(guiCons.Background);
        itemList.setForeground(guiCons.Hint1);
        itemList.setSelectionBackground(guiCons.lightpink);

        JScrollPane scrollPane = new JScrollPane(itemList);
        scrollPane.setBounds(30, 110, 440, 230);
        scrollPane.setBorder(BorderFactory.createLineBorder(guiCons.lightpink, 2));
        add(scrollPane);

        refreshList(model);

        // ➕ Add Item Action
        addBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                try {
                    double price = Double.parseDouble(priceField.getText());
                    Item item = new Item(0, nameField.getText(), descField.getText(), price, user);
                    controller.addItem(item);
                    refreshList(model);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MarketplaceUI.this, "Please enter a valid price.");
                }
            }
        });

        setVisible(true);
        
        // ⬅️ Back Button
        backbutton = new jbutton(LanguageManager.t("back"), 30, 16);
        backbutton.setBounds(30, 360, 140, 40); // Set position and size
        add(backbutton); // Add it to the frame

        // Back button action
        backbutton.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            dispose();
            HomeUI();
          }

            private void HomeUI() {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
        });

    }

    private void refreshList(DefaultListModel<String> model) {
        model.clear();
        ArrayList<Item> items = controller.getAllItems();
        for (Item item : items) {
            model.addElement(item.getName() + " - Rs." + item.getPrice() + " (" + item.getSeller().getFirstName() + ")");
        }
    }
}
