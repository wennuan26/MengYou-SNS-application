package View;

import java.awt.Color;
import java.awt.Font;


@SuppressWarnings("serial")
// This class extends javax.swing.JLabel to create a custom label with specific font, size, color, and style.
public class jlabel extends javax.swing.JLabel {
    public jlabel(String text, int textSize, Color color, int style) {
        setFont(new Font("微软雅黑", style, textSize));
        setText(text);
        setForeground(color);  
    }
    
}
