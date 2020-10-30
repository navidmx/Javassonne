package com.navidmx.javassonne.core;

import com.navidmx.javassonne.core.json.JSONConfig;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestTileDeck {
    TileDeck deck;
    JSONConfig.JSONDeck parsed;
    int amount;

    @Before
    public void initialize() {
        deck = new TileDeck("src/main/resources/tiles.json");
        parsed = JSONConfig.parse("src/main/resources/tiles.json");
        amount = deck.size();
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void testInvalidDraw() {
        for (int i = 0; i <= amount; i++) deck.draw();
    }

    @Test
    public void testCreation() {
        int total = 0;
        for (JSONConfig.JSONTile t : parsed.tiles) {
            if (t.amount == null) total += 1;
            else total += t.amount;
        }
        assertEquals(total, amount);
        assertEquals(72, total);
    }

    @Test
    public void testFirstTile() {
        Tile first = deck.draw();
        assertEquals(first.getId(), 'D');
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void testTooManyDraws() {
        for (int i = 0; i <= 72; i++) deck.draw();
    }
}
