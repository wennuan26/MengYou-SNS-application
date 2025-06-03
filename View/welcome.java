package View;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class welcome{
    public welcome(){
        JFrame frame = new JFrame("MengYou梦友");

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(53, 84, 76, 84));
        panel.add(new jlabel("欢迎使用 MengYou梦友",40, guiCons.White, Font.PLAIN), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(6,1,10,10));
        centerPanel.setBackground(Color.BLACK);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(22, 231, 17, 231));
        


    }
}