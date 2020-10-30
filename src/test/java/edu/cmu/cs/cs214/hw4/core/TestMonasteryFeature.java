package edu.cmu.cs.cs214.hw4.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestMonasteryFeature {
    Game game;

    @Before
    public void initialize() {
        game = new Game();
        game.startGame(2);
    }

    @Test
    public void testMonastery() {
        // Player 1
        game.drawTile('A');
        game.placeTile(new Coordinate(0, -1));
        game.placeMeeple(Direction.CENTER);
        game.endTurn();
        // Player 2
        game.drawTile('E');
        game.placeTile(new Coordinate(0, -2));
        game.endTurn();
        // Player 1
        game.drawTile('U');
        game.placeTile(new Coordinate(-1, 0));
        game.endTurn();
        // Player 2
        game.drawTile('U');
        game.placeTile(new Coordinate(-1, -1));
        game.endTurn();
        // Player 1
        game.drawTile('U');
        game.placeTile(new Coordinate(-1, -2));
        game.endTurn();
        // Player 2
        game.drawTile('M');
        game.placeTile(new Coordinate(1, 0));
        game.endTurn();
        // Player 1
        game.drawTile('G');
        game.placeTile(new Coordinate(1, -1));
        assertEquals(game.getPlayer().getScore(), 0);
        game.endTurn();
        // Player 2
        game.drawTile('I');
        game.placeTile(new Coordinate(1, -2));
        game.endTurn();
        assertEquals(game.getPlayer().getScore(), 9);
    }
}
