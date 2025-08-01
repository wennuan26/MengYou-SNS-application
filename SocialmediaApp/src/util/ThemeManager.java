package util;

import javax.swing.*;
import java.awt.*;

public class ThemeManager {
    public enum Theme {
        LIGHT, DARK
    }

    private static Theme currentTheme = Theme.LIGHT;

    public static void applyTheme(Component component, Theme theme) {
        SwingUtilities.invokeLater(() -> {
            Color background;
            Color foreground;

            if (theme == Theme.DARK) {
                background = new Color(45, 45, 45);
                foreground = Color.WHITE;
            } else {
                background = Color.WHITE;
                foreground = Color.BLACK;
            }

            setComponentTheme(component, background, foreground);
            currentTheme = theme;
        });
    }

    private static void setComponentTheme(Component component, Color bg, Color fg) {
        if (component instanceof JComponent) {
            component.setBackground(bg);
            component.setForeground(fg);
            if (component instanceof Container) {
                for (Component child : ((Container) component).getComponents()) {
                    setComponentTheme(child, bg, fg);
                }
            }
        }
    }

    public static Theme getCurrentTheme() {
        return currentTheme;
    }

    public static void toggleTheme(Component component) {
        if (currentTheme == Theme.LIGHT) {
            applyTheme(component, Theme.DARK);
        } else {
            applyTheme(component, Theme.LIGHT);
        }
    }

    public static void animateButtonClick(JButton button) {
        new Thread(() -> {
            try {
                Color original = button.getBackground();
                button.setBackground(Color.PINK);
                Thread.sleep(150);
                button.setBackground(original);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
