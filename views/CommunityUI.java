package views;

import Controller.CommunityController;
import Model.Community;
import Model.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class CommunityUI extends jFrame {
    private final CommunityController controller = new CommunityController();

    public CommunityUI(User user) {
        setLayout(null);

        // 🏷 Title Label
        jlabel label = new jlabel("Communities:", 18, guiCons.White, Font.BOLD);
        label.setBounds(30, 15, 300, 25);
        add(label);

        // 🔤 Input Fields
        jtextfield nameField = new jtextfield("Name");
        jtextfield descField = new jtextfield("Description");
        jbutton addBtn = new jbutton("Create", 25, 14);

        nameField.setBounds(30, 50, 150, 40);
        descField.setBounds(190, 50, 150, 40);
        addBtn.setBounds(360, 50, 100, 40);

        add(nameField);
        add(descField);
        add(addBtn);

        // 📋 Community List
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> list = new JList<>(model);
        list.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        list.setBackground(guiCons.Background);
        list.setForeground(guiCons.Hint1);
        list.setSelectionBackground(guiCons.lightpink);

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBounds(30, 110, 430, 230);
        scroll.setBorder(BorderFactory.createLineBorder(guiCons.lightpink, 2));
        add(scroll);

        // 🔁 Load Data
        refreshList(model);

        // ➕ Create Button Action
        addBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                controller.createCommunity(nameField.getText(), descField.getText(), user.getId());
                refreshList(model);
            }
        });

        setVisible(true);
    }

    private void refreshList(DefaultListModel<String> model) {
        model.clear();
        ArrayList<Community> communities = controller.getAllCommunities();
        for (Community c : communities) {
            model.addElement(c.getName() + " by " + c.getOwner().getFirstName());
        }
    }
}
