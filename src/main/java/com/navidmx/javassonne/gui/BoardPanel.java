package com.navidmx.javassonne.gui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

/**
 * Class to represent the Carcassonne Board and a pane to scroll.
 */
class BoardPanel extends JPanel {
    private static final Color BOARD_COLOR = new Color(252, 252, 252);
    private static final int SCROLL_SPEED = 25;
    private final int size;

    BoardPanel(int size) {
        this.size = size;

        setLayout(null);
        setPreferredSize(new Dimension(size, size));
        setBackground(BOARD_COLOR);
    }

    JScrollPane createViewer(int width, int height) {
        JScrollPane viewer = new JScrollPane();
        viewer.setPreferredSize(new Dimension(width, height));
        viewer.setBounds(0, 0, width, height);
        viewer.getHorizontalScrollBar().setUnitIncrement(SCROLL_SPEED);
        viewer.getVerticalScrollBar().setUnitIncrement(SCROLL_SPEED);
        viewer.setViewportView(this);
        Point origin = new Point(size / 2 - width / 2, size / 2 - height / 2);
        viewer.getViewport().setViewPosition(origin);
        return viewer;
    }
}
