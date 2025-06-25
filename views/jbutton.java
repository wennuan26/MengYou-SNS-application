/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package views;

import java.awt.Font;
import java.awt.Shape;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class jbutton extends JLabel {

    private Shape shape;
    private final int radius;

    public jbutton(String text, int radius, int textSize) {
        super(text);
        this.radius = radius;
        setFont(new Font("微软雅黑", Font.BOLD, textSize));
        setOpaque(false);
        setForeground(guiCons.White);
        setHorizontalAlignment(CENTER);
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(guiCons.lightpink);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(guiCons.White);
            }
        });
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        g.setColor(guiCons.SoftGray);
        g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(java.awt.Graphics g) {
        g.setColor(guiCons.lightpink);
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
    }

    @Override
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().contains(x, y)) {
            shape = new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        }
        return shape.contains(x, y);
    }

    void setFocusPainted(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    void addActionListener(ActionListener actionListener) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
