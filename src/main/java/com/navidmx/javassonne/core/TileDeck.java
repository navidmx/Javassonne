package com.navidmx.javassonne.core;

import com.navidmx.javassonne.core.json.JSONConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Represents the deck of all tiles, with methods to draw, shuffle, and reset the deck.
 */
public class TileDeck {
    private final List<Tile> deck;
    private final HashMap<Character, Tile> dict;

    /**
     * Constructor for the deck.
     * @param configPath The location of the JSON file where tiles are represented.
     */
    public TileDeck(String configPath) {
        deck = new ArrayList<>();
        dict = new HashMap<>();

        JSONConfig.JSONDeck parsed = JSONConfig.parse(configPath);
        Tile curr;
        Tile first = null;
        for (JSONConfig.JSONTile tile : parsed.tiles) {
            // Set each segment's feature type.
            FeatureType top = FeatureType.valueOf(tile.top);
            FeatureType right = FeatureType.valueOf(tile.right);
            FeatureType bottom = FeatureType.valueOf(tile.bottom);
            FeatureType left = FeatureType.valueOf(tile.left);
            FeatureType center = FeatureType.valueOf(tile.center);
            // Determine whether the tile contains a shield (for cities).
            boolean shield = tile.shield != null;
            // Go through each link and create the feature out of them.
            ArrayList<List<Direction>> links = new ArrayList<>();
            if (tile.links != null) {
                for (String[] link : tile.links) {
                    ArrayList<Direction> feature = new ArrayList<>();
                    for (String str : link) feature.add(Direction.valueOf(str.toUpperCase()));
                    links.add(feature);
                }
            }
            if (tile.id == null) throw new IllegalArgumentException("Tile is missing an ID.");
            if (tile.amount == null) tile.amount = 1;
            // Create the tile and add it to the deck.
            curr = new Tile(tile.id, top, right, bottom, left, center, shield, links);
            // Store each tile once by ID for easy access when debugging and testing.
            if (!dict.containsKey(tile.id))
                dict.put(tile.id, new Tile(tile.id, top, right, bottom, left, center, shield, links));
            // If the starting tile ("D") is found, store it for later.
            if (tile.id == 'D' && first == null) first = curr;
            // Finally, add the tile to the deck the appropriate number of times.
            for (int i = 0; i < tile.amount; i++) deck.add(curr);
        }

        // Shuffle the deck.
        Collections.shuffle(deck);

        // Ensure the first card drawn is of type "D".
        if (first == null) throw new IllegalArgumentException("Deck is missing a starting tile.");
        deck.remove(first);
        deck.add(first);
    }

    /**
     * Draws the tile at the top of the deck.
     * @return a tile.
     * @throws IndexOutOfBoundsException if there are no remaining tiles.
     */
    public Tile draw() {
        int size = deck.size();
        if (size < 1) throw new IndexOutOfBoundsException("Attempted to draw with no remaining tiles.");
        return deck.remove(size - 1).copy();
    }

    // Primarily for debugging and testing purposes: create a clone of a tile by ID.
    Tile draw(char id) {
        if (!dict.containsKey(id)) throw new IllegalArgumentException("Tile " + id + " does not exist.");
        return (dict.get(id).copy());
    }

    int size() {
        return deck.size();
    }
}
