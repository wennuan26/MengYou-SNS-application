package views;

import java.awt.Font;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class jbutton extends JLabel {

    private Shape shape;
    private final int radius;
    private final java.util.List<ActionListener> listeners = new ArrayList<>();

    public jbutton(String text, int radius, int textSize) {
        super(text);
        this.radius = radius;

        setFont(new Font("微软雅黑", Font.BOLD, textSize));
        setOpaque(false);
        setForeground(guiCons.White);
        setHorizontalAlignment(CENTER);
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // Hover effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(guiCons.lightpink);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(guiCons.White);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, getText());
                for (ActionListener l : listeners) {
                    l.actionPerformed(evt);
                }
            }
        });
    }

    // Simulate ActionListener behavior
    public void addActionListener(ActionListener l) {
        listeners.add(l);
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        g.setColor(guiCons.SoftGray);
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
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

    // Optional: remove unsupported method
    public void setFocusPainted(boolean b) {
        // No need for this in JLabel version
    }
}
