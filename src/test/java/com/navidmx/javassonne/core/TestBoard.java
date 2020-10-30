package com.navidmx.javassonne.core;

import org.junit.Before;
import org.junit.Test;

public class TestBoard {
    Game game;
    Board board;

    @Before
    public void initialize() {
        game = new Game();
        board = game.getBoard();
    }

    @Test
    public void testAvailable() {
        game.startGame(2);
        Tile t = game.getDeck().draw('U');
        board.placeTile(new Coordinate(0, 1), t, false);
    }
}
