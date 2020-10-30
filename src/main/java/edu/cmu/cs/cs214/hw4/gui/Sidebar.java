package edu.cmu.cs.cs214.hw4.gui;

import edu.cmu.cs.cs214.hw4.core.GameAPI;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * The sidebar for the main Carcassonne GamePanel.
 */
class Sidebar extends JPanel {
    private final GameAPI game;

    Sidebar(GameAPI game, int width, int height, Color color) {
        this.game = game;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(width, height));
        setBackground(color);
    }

    void addDivider(int size) {
        add(Box.createVerticalStrut(size));
    }

    JLabel createCurrTile(int size) {
        JLabel currTile = new JLabel();
        currTile.setPreferredSize(new Dimension(size, size));
        currTile.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(currTile);
        return currTile;
    }

    JButton createRotateButton() {
        JButton rotate = new JButton("ROTATE");
        rotate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rotate.addActionListener(e -> game.rotateTile());
        rotate.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(rotate);
        return rotate;
    }

    JButton createPlaceButton() {
        JButton place = new JButton("PLACE");
        place.setCursor(new Cursor(Cursor.HAND_CURSOR));
        place.setAlignmentX(Component.CENTER_ALIGNMENT);
        place.setEnabled(false);
        add(place);
        return place;
    }

    JButton createEndTurnButton() {
        JButton endTurn = new JButton("END TURN");
        endTurn.addActionListener(e -> game.endTurn());
        endTurn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        endTurn.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(endTurn);
        return endTurn;
    }

    JLabel createTilesRemaining() {
        JLabel text = new JLabel("Tiles remaining: ");
        JLabel tilesRemaining = new JLabel("");
        tilesRemaining.setFont(new Font(tilesRemaining.getText(), Font.BOLD, 14));
        text.setAlignmentX(Component.CENTER_ALIGNMENT);
        tilesRemaining.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(text);
        add(tilesRemaining);
        return tilesRemaining;
    }

    void createPlayerTable(int playerCount, JLabel[] players, JLabel[] meeples, JLabel[] scores, Color[] colors) {
        JPanel playerTable = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        // Create column names
        JLabel playerCol = new JLabel("Player");
        JLabel meepleCol = new JLabel("Meeples");
        JLabel scoreCol = new JLabel("Score");
        // Set column header font
        playerCol.setFont(new Font(playerCol.getName(), Font.BOLD, 16));
        meepleCol.setFont(new Font(meepleCol.getName(), Font.BOLD, 16));
        scoreCol.setFont(new Font(scoreCol.getName(), Font.BOLD, 16));
        // Column formatting
        c.gridy = 0; c.ipady = 10;
        c.gridx = 1; c.weightx = 0.5; playerTable.add(playerCol, c);
        c.gridx = 2; c.weightx = 0.25; playerTable.add(meepleCol, c);
        c.gridx = 3; c.weightx = 0.2; playerTable.add(scoreCol, c);
        // Load each player
        for (int i = 0; i < playerCount; i++) {
            players[i] = new JLabel("Player " + (i + 1));
            meeples[i] = new JLabel("7");
            scores[i] = new JLabel("0");
            // Set column text font
            players[i].setFont(new Font(players[i].getName(), Font.PLAIN, 14));
            meeples[i].setFont(new Font(meeples[i].getName(), Font.PLAIN, 14));
            scores[i].setFont(new Font(scores[i].getName(), Font.PLAIN, 14));
            // Formatting for each row
            c.ipady = 5; c.gridy = i + 1;
            // Add the player's color
            JPanel dot = new JPanel();
            dot.setPreferredSize(new Dimension(14, 10));
            dot.setBackground(colors[i]);
            c.insets = new Insets(0, 20, 0, 0);
            c.gridx = 0; c.weightx = 0.05; playerTable.add(dot, c);
            c.insets = new Insets(0, 0, 0, 0);
            // Add the player's name
            c.gridx = 1; c.weightx = 0.5; playerTable.add(players[i], c);
            // Add the player's meeple count
            c.gridx = 2; c.weightx = 0.25; playerTable.add(meeples[i], c);
            // Add the player's score
            c.gridx = 3; c.weightx = 0.2; playerTable.add(scores[i], c);
        }
        add(playerTable);
    }
}
