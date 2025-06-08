/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package views;

import java.awt.Color;
import java.awt.Font;

@SuppressWarnings("serial")
public class jlabel extends javax.swing.JLabel {
    public jlabel(String text, int textSize, Color color, int style) {
        super(text);
        setFont(new Font("微软雅黑", style, textSize));
        setForeground(color);
    }
}

