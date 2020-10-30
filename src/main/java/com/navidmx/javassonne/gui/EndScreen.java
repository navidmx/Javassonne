package com.navidmx.javassonne.gui;

import com.navidmx.javassonne.core.Player;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

/**
 * The end screen for the main Carcassonne game panel.
 */
class EndScreen extends JPanel {

    EndScreen(Player[] scores, Color[] colors) {
        JLabel text = new JLabel("Game Over!");
        text.setFont(new Font(text.getText(), Font.BOLD, 20));
        text.setHorizontalAlignment(JLabel.CENTER);
        JLabel subtext = new JLabel("Final scores:");
        subtext.setFont(new Font(text.getText(), Font.PLAIN, 18));
        subtext.setHorizontalAlignment(JLabel.CENTER);
        JPanel scoreTable = new JPanel(new GridBagLayout());
        GridBagConstraints sc = new GridBagConstraints();
        int row = 1;
        for (Player p : scores) {
            int playerId = p.getId();
            JLabel playerName = new JLabel("Player " + playerId);
            JLabel playerScore = new JLabel(String.valueOf(p.getScore()));
            playerName.setFont(new Font(playerName.getText(), Font.PLAIN, 18));
            playerScore.setFont(new Font(playerScore.getText(), Font.BOLD, 22));
            playerScore.setForeground(colors[playerId - 1]);
            sc.gridy = row; sc.ipady = 10; sc.weightx = 0.50;
            sc.gridx = 0; scoreTable.add(playerName, sc);
            sc.gridx = 1; scoreTable.add(playerScore, sc);
            row++;
        }
        setLayout(new GridBagLayout());
        GridBagConstraints pc = new GridBagConstraints();
        pc.ipady = 10; pc.ipadx = 80;
        pc.gridy = 0; add(text, pc);
        pc.gridy = 1; add(subtext, pc);
        pc.gridy = 2; add(scoreTable, pc);
    }
}
