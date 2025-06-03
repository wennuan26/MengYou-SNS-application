package View;
// This file is part of the MengYou梦友 project.    

import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;

public class jtextfield extends javax.swing.JTextField {

    private final String hint;
    public jtextfield(String hint) {
        super();
        this.hint = hint;
        setFont(new Font("微软雅黑", Font.PLAIN, 16));
        setOpaque(false);
        setText(hint);
        setForeground(guiCons.White);
        setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals(hint)) {
                    setText("");
                    setForeground(guiCons.White);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(hint);
                    setForeground(guiCons.White);
                }
            }
        });
    }
}
