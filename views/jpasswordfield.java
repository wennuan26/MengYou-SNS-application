/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package views;

import java.awt.Font;
import java.awt.Shape;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.JPasswordField;
import java.awt.Graphics;
import java.awt.geom.RoundRectangle2D;

@SuppressWarnings("serial")
public class jpasswordfield extends JPasswordField {

    private Shape shape;
    private String placeholder;

    public jpasswordfield(String hint) {
        super(hint);
        this.placeholder = hint;
        setFont(new Font("微软雅黑", Font.PLAIN, 16));
        setOpaque(false);
        setForeground(guiCons.Hint);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        setEchoChar((char) 0); // show placeholder

        addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (getText().equals(placeholder)) {
                    setText("");
                    setForeground(guiCons.White);
                    setEchoChar('●');
                }
            }

            public void focusLost(FocusEvent e) {
                if (getPassword().length == 0) {
                    setText(placeholder);
                    setForeground(guiCons.Hint);
                    setEchoChar((char) 0);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(guiCons.SoftGray);
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(guiCons.Blue);
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
    }

    @Override
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().contains(x, y)) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
        }
        return shape.contains(x, y);
    }
}
