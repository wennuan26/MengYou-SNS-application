package views;

@SuppressWarnings("serial")
public class jFrame extends javax.swing.JFrame {
    public jFrame() {
        super("MengYou梦友");
        getContentPane().setBackground(guiCons.BACKGROUND);
        setSize(900, 625);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(false); // remove if you want borderless
    }
}
