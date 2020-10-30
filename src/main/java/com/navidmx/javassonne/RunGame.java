package com.navidmx.javassonne;

import com.navidmx.javassonne.core.Game;
import com.navidmx.javassonne.gui.GamePanel;
import com.navidmx.javassonne.core.GameAPI;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.io.IOException;

/**
 * Main program to run the Carcassonne game.
 */
public class RunGame {
    /**
     * Run to play.
     * @param args Not necessary.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(RunGame::createBoard);
    }

    private static void createBoard() {
        GameAPI game = new Game();

        try {
            GamePanel panel = new GamePanel(game);
            panel.setOpaque(true);
            JFrame frame = new JFrame("Carcassonne");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setContentPane(panel);
            frame.pack();
            frame.setVisible(true);
            frame.setResizable(false);
        } catch (IOException e) {
            System.out.println("Could not load file.");
        }
    }
}