package com.navidmx.javassonne.gui;

import com.navidmx.javassonne.core.Direction;
import com.navidmx.javassonne.core.Coordinate;
import com.navidmx.javassonne.core.GameAPI;

import javax.swing.JComboBox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Listens for changes to the meeple selector direction combo box.
 */
class DirectionListener implements ActionListener {
    private final GameAPI game;
    private final JComboBox<Direction> directions;
    private final Coordinate coord;

    DirectionListener(GameAPI game, JComboBox<Direction> directions, Coordinate coord) {
        this.game = game;
        this.directions = directions;
        this.coord = coord;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Direction dir = (Direction) directions.getSelectedItem();
        // When a direction is selected, place a meeple and end the turn.
        if (dir != null) {
            game.placeMeeple(dir);
            game.endTurn();
        }
    }
}
