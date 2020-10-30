package com.navidmx.javassonne.gui;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Cursor;
import java.awt.Insets;

/**
 * A JButton with an image as the icon.
 */
class ImageButton extends JButton {

    ImageButton(String imgPath) {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(0, 0, 0, 0));
        setContentAreaFilled(false);
        setIcon(new ImageIcon(imgPath));
        setBorder(null);
        setBorderPainted(false);
        setFocusPainted(false);
    }
}
