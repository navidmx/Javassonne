package com.navidmx.javassonne.core;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class TestTile {
    Game game;

    @Before
    public void initialize() {
        game = new Game();
    }

    @Test
    public void testTileCreation() {
        game.startGame(2);
        Tile t = game.getDeck().draw('D');
        HashMap<Direction, Segment> segments = t.getSegments();
        assertEquals(FeatureType.ROAD, segments.get(Direction.TOP).getType());
        assertEquals(FeatureType.CITY, segments.get(Direction.RIGHT).getType());
        assertEquals(FeatureType.ROAD, segments.get(Direction.BOTTOM).getType());
        assertEquals(FeatureType.FIELD, segments.get(Direction.LEFT).getType());
        assertEquals(FeatureType.ROAD, segments.get(Direction.CENTER).getType());
    }
}
