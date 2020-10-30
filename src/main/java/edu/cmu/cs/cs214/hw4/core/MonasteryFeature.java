package edu.cmu.cs.cs214.hw4.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Representation of a monastery.
 */
class MonasteryFeature implements Feature {
    private final Segment segment;
    private final Tile tile;
    private Player meeple;

    /**
     * Constructor for the monastery feature.
     * @param startingTile the tile the feature starts on.
     * @param segment The segment where the monastery lies.
     */
    MonasteryFeature(Tile startingTile, Segment segment) {
        this.tile = startingTile;
        this.segment = segment;
    }

    @Override
    public HashMap<Player, Integer> score() {
        HashMap<Player, Integer> res = new HashMap<>();
        if (meeple != null) res.put(meeple, 1 + getCompletedSides());
        return res;
    }

    @Override
    public void returnMeeples() {
        if (meeple != null) {
            meeple.returnMeeple();
            meeple = null;
        }
    }

    @Override
    public boolean isComplete() {
        return getCompletedSides() == 8;
    }

    @Override
    public FeatureType getType() {
        return FeatureType.MONASTERY;
    }

    @Override
    public Collection<Segment> getSegments() {
        return Collections.singleton(segment);
    }

    @Override
    public Map<Player, Integer> getMeeples() {
        Map<Player, Integer> res = new HashMap<>();
        if (meeple != null) res.put(meeple, 1);
        return res;
    }

    @Override
    public Map<Tile, Direction> getTiles() {
        Map<Tile, Direction> res = new HashMap<>();
        if (meeple != null) res.put(tile, Direction.CENTER);
        else res.put(tile, null);
        return res;
    }

    @Override
    public void addMeeple(Player p, Tile tile, Direction dir) {
        if (p == null) throw new IllegalArgumentException("Invalid player provided when adding meeple.");
        meeple = p;
    }

    private int getCompletedSides() {
        int res = 0;
        Segment above = segment.getNeighbor(Direction.TOP).getNeighbor(Direction.TOP);
        Segment below = segment.getNeighbor(Direction.BOTTOM).getNeighbor(Direction.BOTTOM);
        // TOP
        if (above != null) {
            res++;
            // TOP LEFT
            if (above.getNeighbor(Direction.LEFT).getNeighbor(Direction.LEFT) != null) res++;
            // TOP RIGHT
            if (above.getNeighbor(Direction.RIGHT).getNeighbor(Direction.RIGHT) != null) res++;
        }
        // BOTTOM
        if (below != null) {
            res++;
            // BOTTOM LEFT
            if (below.getNeighbor(Direction.LEFT).getNeighbor(Direction.LEFT) != null) res++;
            // BOTTOM RIGHT
            if (below.getNeighbor(Direction.RIGHT).getNeighbor(Direction.RIGHT) != null) res++;
        }
        // RIGHT
        if (segment.getNeighbor(Direction.RIGHT).getNeighbor(Direction.RIGHT) != null) res++;
        // LEFT
        if (segment.getNeighbor(Direction.LEFT).getNeighbor(Direction.LEFT) != null) res++;
        return res;
    }

    @Override
    public String toString() {
        String res = "MONASTERY";
        if (meeple != null) res = res.concat(" (meeple: " + meeple + ')');
        if (isComplete()) res = res.concat(" (completed)");
        return res;
    }
}
