package com.navidmx.javassonne.gui;

import com.navidmx.javassonne.core.GameAPI;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

/**
 * The start screen for the main Carcassonne game panel.
 */
class StartScreen extends JPanel {
    private static final int MIN_PLAYERS = 2, MAX_PLAYERS = 5;

    StartScreen(int width, int height, Color color, GameAPI game) {
        setLayout(new GridBagLayout());
        setBackground(color);
        setPreferredSize(new Dimension(width, height));
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel logo = new JLabel();
        logo.setIcon(new ImageIcon("src/main/resources/logo.png"));
        JLabel playersLabel = new JLabel("Select number of players:  ");
        playersLabel.setFont(new Font(playersLabel.getText(), Font.PLAIN, 16));
        JPanel playerButtons = new JPanel(new GridLayout(1, 5, 20, 0));
        for (int i = MIN_PLAYERS; i <= MAX_PLAYERS; i++) {
            int numPlayers = i;
            JButton curr = new ImageButton("src/main/resources/players_" + numPlayers + ".png");
            curr.addActionListener(e -> game.startGame(numPlayers));
            playerButtons.add(curr);
        }
        gbc.gridy = 0; gbc.ipady = 5;
        add(logo, gbc);
        gbc.gridy = 1; gbc.ipady = 20;
        add(playersLabel, gbc);
        gbc.gridy = 2;
        add(playerButtons, gbc);
    }
}
