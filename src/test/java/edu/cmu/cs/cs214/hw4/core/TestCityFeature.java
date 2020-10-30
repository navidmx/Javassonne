package edu.cmu.cs.cs214.hw4.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestCityFeature {
    Game game;

    @Before
    public void initialize() {
        game = new Game();
        game.startGame(2);
    }

    @Test
    public void testSmallCity() {
        // Player 1
        game.drawTile('H');
        game.placeTile(new Coordinate(1, 0));
        game.placeMeeple(Direction.LEFT);
        game.endTurn();
        // Player 2
        game.endTurn();
        // Player 1
        assertEquals(4, game.getPlayer().getScore());
    }

    @Test
    public void testLargeCity() {
        // Player 1
        game.drawTile('N');
        game.placeTile(new Coordinate(1, 0));
        game.placeMeeple(Direction.LEFT);
        game.endTurn();
        // Player 2
        game.drawTile('M');
        game.rotateTile();
        game.rotateTile();
        game.placeTile(new Coordinate(1, -1));
        game.endTurn();
        // Player 1
        game.drawTile('H');
        game.placeTile(new Coordinate(2, -1));
        game.endTurn();
        // Player 2
        game.endTurn();
        // Player 1
        assertEquals(10, game.getPlayer().getScore());
        assertEquals(7, game.getPlayer().getMeeples());
    }

    @Test
    public void testMergingCityTie() {
        // Player 1
        game.drawTile('U');
        game.placeTile(new Coordinate(0, -1));
        game.endTurn();
        // Player 2
        game.drawTile('U');
        game.placeTile(new Coordinate(0, 1));
        game.endTurn();
        // Player 1
        game.drawTile('K');
        game.rotateTile();
        game.placeTile(new Coordinate(1, -1));
        game.placeMeeple(Direction.BOTTOM);
        game.endTurn();
        // Player 2
        game.drawTile('J');
        game.placeTile(new Coordinate(1, 1));
        game.placeMeeple(Direction.TOP);
        game.endTurn();
        // Player 1
        game.drawTile('Q');
        game.rotateTile();
        game.rotateTile();
        game.rotateTile();
        game.placeTile(new Coordinate(1, 0));
        game.endTurn();
        // Player 2
        int p2 = game.getPlayer().getScore();
        game.endTurn();
        // Player 1
        int p1 = game.getPlayer().getScore();
        assertEquals(p1, p2);
        assertEquals(p1, 10);
    }

    @Test
    public void testMergingCityConquest() {
        // Player 1
        game.drawTile('U');
        game.placeTile(new Coordinate(0, -1));
        game.endTurn();
        // Player 2
        game.drawTile('U');
        game.placeTile(new Coordinate(0, 1));
        game.endTurn();
        // Player 1
        game.drawTile('K');
        game.rotateTile();
        game.placeTile(new Coordinate(1, -1));
        game.placeMeeple(Direction.BOTTOM);
        game.endTurn();
        // Player 2
        game.drawTile('J');
        game.placeTile(new Coordinate(1, 1));
        game.endTurn();
        // Player 1
        game.drawTile('Q');
        game.rotateTile();
        game.rotateTile();
        game.rotateTile();
        game.placeTile(new Coordinate(1, 0));
        game.endTurn();
        // Player 2
        int p2 = game.getPlayer().getScore();
        game.endTurn();
        // Player 1
        int p1 = game.getPlayer().getScore();
        assertEquals(p1, 10);
        assertEquals(p2, 0);
    }
}
