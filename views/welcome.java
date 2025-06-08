/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package views;

import java.awt.*;
import javax.swing.*;

public class welcome {

    public welcome() {
        jFrame frame = new jFrame();

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(guiCons.Black);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));

        jlabel title = new jlabel("欢迎使用 MengYou梦友", 24, guiCons.White, Font.BOLD);
        title.setHorizontalAlignment(JLabel.CENTER);
        panel.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(6, 1, 15, 15));
        centerPanel.setBackground(guiCons.Black);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        jtextfield firstName = new jtextfield("First Name");
        jtextfield lastName = new jtextfield("Last Name");
        jtextfield email = new jtextfield("Email");

        jpasswordfield password = new jpasswordfield("Password");
        jpasswordfield confirmPassword = new jpasswordfield("Confirm Password");

        jbutton createACC = new jbutton("Create Account", 30, 16);

        centerPanel.add(firstName);
        centerPanel.add(lastName);
        centerPanel.add(email);
        centerPanel.add(password);
        centerPanel.add(confirmPassword);
        centerPanel.add(createACC);

        panel.add(centerPanel, BorderLayout.CENTER);

        jlabel login = new jlabel("Already have an account? Login", 14, guiCons.Hint, Font.PLAIN);
        login.setHorizontalAlignment(JLabel.CENTER);
        login.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(login, BorderLayout.SOUTH);
        


        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}
