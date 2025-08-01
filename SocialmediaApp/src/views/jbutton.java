package views;

import javax.swing.*;
import java.awt.*;
import java.awt.Shape;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import static views.guiCons.BACKGROUND;

@SuppressWarnings("serial")
public class jbutton extends JLabel {

    private Shape shape;
    private final int radius;
    private final List<ActionListener> listeners = new ArrayList<>();
    private boolean hovered = false;
    private boolean pressed = false;

    // Constructor with text, corner radius, font size
    public jbutton(String text, int radius, int fontSize) {
        this(text, radius, fontSize, null);
    }

    // Constructor with optional icon
    public jbutton(String text, int radius, int fontSize, Icon icon) {
        super(text);
        this.radius = radius;

        setFont(new Font("微软雅黑", Font.BOLD, fontSize));
        setOpaque(false);
        setForeground(guiCons.BACKGROUND);
        setHorizontalAlignment(CENTER);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (icon != null) {
            setIcon(icon);
            setIconTextGap(10);
            setHorizontalTextPosition(SwingConstants.RIGHT);
            setVerticalTextPosition(SwingConstants.CENTER);
        }

        // Add hover/click effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                setForeground(guiCons.BACKGROUND);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                pressed = false;
                setForeground(guiCons.BACKGROUND);
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                pressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (pressed && contains(e.getPoint())) {
                    fireActionPerformed();
                }
                pressed = false;
                repaint();
            }
        });
    }

    // Fire all attached listeners
    private void fireActionPerformed() {
        ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, getText());
        for (ActionListener l : listeners) {
            l.actionPerformed(evt);
        }
    }

    public void addActionListener(ActionListener l) {
        listeners.add(l);
    }

    public void removeActionListener(ActionListener l) {
        listeners.remove(l);
    }

    public void setTooltip(String text) {
        setToolTipText(text);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        Color base = guiCons.BUTTONS;
        if (pressed) {
            base = base.darker();
        } else if (hovered) {
            base = base.brighter();
        }

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(base);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(guiCons.BUTTONS);
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
    }

    @Override
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        }
        return shape.contains(x, y);
    }

    // Ignored but included for compatibility
    public void setFocusPainted(boolean b) {}
}
