package com.navidmx.javassonne.gui;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Paint icon with specific transparency.
 */
class TransparentImageIcon extends ImageIcon {
    private final Icon icon;
    private final float alpha;

    TransparentImageIcon(Icon icon, float alpha) {
        this.icon = icon;
        this.alpha = alpha;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.SrcAtop.derive(alpha));
        icon.paintIcon(c, g2, x, y);
        g2.dispose();
    }

    @Override
    public int getIconWidth() {
        return icon.getIconWidth();
    }

    @Override
    public int getIconHeight() {
        return icon.getIconHeight();
    }
}