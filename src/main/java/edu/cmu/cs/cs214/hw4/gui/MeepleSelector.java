package edu.cmu.cs.cs214.hw4.gui;

import edu.cmu.cs.cs214.hw4.core.Coordinate;
import edu.cmu.cs.cs214.hw4.core.Direction;
import edu.cmu.cs.cs214.hw4.core.GameAPI;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;

/**
 * Class to represent the panel that allows for meeple selection.
 */
class MeepleSelector extends JPanel {
    private final GameAPI game;
    private final JComboBox<Direction> combo;
    private DirectionListener listener;

    MeepleSelector(Color color, GameAPI game) {
        this.game = game;
        setVisible(false);
        combo = new JComboBox<>();
        add(new JLabel("OR select a meeple to place:"));
        add(combo);
        setBackground(color);
    }

    void load(Direction[] directions, Coordinate coord) {
        for (Direction dir : directions) combo.addItem(dir);
        combo.setSelectedIndex(-1);
        listener = new DirectionListener(game, combo, coord);
        combo.addActionListener(listener);
        setVisible(true);
    }

    void unload() {
        combo.removeAllItems();
        combo.removeActionListener(listener);
        setVisible(false);
    }
}
