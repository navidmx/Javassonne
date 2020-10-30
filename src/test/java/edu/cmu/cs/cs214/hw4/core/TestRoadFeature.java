package edu.cmu.cs.cs214.hw4.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestRoadFeature {
    Game game;

    @Before
    public void initialize() {
        game = new Game();
        game.startGame(2);
    }

    @Test
    public void testShortRoad() {
        // Player 1
        game.drawTile('W');
        game.placeTile(new Coordinate(0, -1));
        game.placeMeeple(Direction.BOTTOM);
        game.endTurn();
        // Player 2
        game.drawTile('X');
        game.placeTile(new Coordinate(0, 1));
        game.endTurn();
        // Player 1
        assertEquals(3, game.getPlayer().getScore());
    }

    @Test
    public void testTwoRoads() {
        // Player 1
        game.drawTile('J');
        game.placeTile(new Coordinate(0, -1));
        game.placeMeeple(Direction.BOTTOM);
        game.endTurn();
        // Player 2
        game.drawTile('K');
        game.rotateTile();
        game.placeTile(new Coordinate(0, 1));
        game.endTurn();
        // Player 1
        game.drawTile('W');
        game.placeTile(new Coordinate(1, -1));
        game.endTurn();
        // Player 2
        game.drawTile('X');
        game.placeTile(new Coordinate(1, 1));
        game.placeMeeple(Direction.TOP);
        game.endTurn();
        // Player 1
        game.drawTile('D');
        game.rotateTile();
        game.rotateTile();
        game.placeTile(new Coordinate(1, 0));
        game.endTurn();
        // Player 2
        assertEquals(3, game.getPlayer().getScore());
        game.endTurn();
        // Player 1
        assertEquals(5, game.getPlayer().getScore());
    }

    @Test
    public void testLoopedMergedRoad() {
        // Player 1
        game.drawTile('W');
        game.placeTile(new Coordinate(0, -1));
        game.placeMeeple(Direction.RIGHT);
        game.endTurn();
        // Player 2
        game.drawTile('V');
        game.rotateTile();
        game.rotateTile();
        game.placeTile(new Coordinate(0, 1));
        game.placeMeeple(Direction.RIGHT);
        game.endTurn();
        // Player 1
        game.drawTile('V');
        game.placeTile(new Coordinate(1, -1));
        game.endTurn();
        // Player 2
        game.drawTile('D');
        game.rotateTile();
        game.rotateTile();
        game.placeTile(new Coordinate(1, 0));
        game.endTurn();
        // Player 1
        game.drawTile('V');
        game.rotateTile();
        game.placeTile(new Coordinate(1, 1));
        game.endTurn();
        // Player 2
        int p2 = game.getPlayer().getScore();
        // Player 1
        int p1 = game.getPlayer().getScore();
        assertEquals(p1, p2);
        assertEquals(6, p1);
    }
}
