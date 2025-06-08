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

@SuppressWarnings("serial")
public class jtextfield extends javax.swing.JTextField {

    private Shape shape;

    public jtextfield(String hint) {
        super(hint);
        setFont(new Font("微软雅黑", Font.PLAIN, 16));
        setOpaque(false);
        setForeground(guiCons.Hint);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (getText().equals(hint)) {
                    setText("");
                    setForeground(guiCons.White);
                }
            }

            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(hint);
                    setForeground(guiCons.Hint);
                }
            }
        });
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        g.setColor(guiCons.SoftGray);
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(java.awt.Graphics g) {
        g.setColor(guiCons.Blue);
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
    }

    @Override
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().contains(x, y)) {
            shape = new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 30, 30);
        }
        return shape.contains(x, y);
    }
}
