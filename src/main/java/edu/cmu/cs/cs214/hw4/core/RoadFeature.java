package edu.cmu.cs.cs214.hw4.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Representation of a road.
 */
public class RoadFeature implements CompoundFeature {
    private final Set<Segment> segments;
    private final Map<Tile, Direction> tiles;
    private final HashMap<Player, Integer> meeples;

    /**
     * Constructor for the road feature.
     * @param startingTile the tile the feature starts on.
     * @param segments the segments that make up the feature.
     */
    public RoadFeature(Tile startingTile, List<Segment> segments) {
        this.segments = new HashSet<>(segments);
        this.meeples = new HashMap<>();
        this.tiles = new HashMap<>();
        this.tiles.put(startingTile, null);
    }

    @Override
    public void merge(CompoundFeature f) {
        segments.addAll(f.getSegments());
        // Copy over player meeples
        for (Map.Entry<Player, Integer> meeple : f.getMeeples().entrySet()) {
            Player p = meeple.getKey();
            int amount = meeple.getValue();
            if (meeples.containsKey(p)) meeples.put(p, meeples.get(p) + amount);
            else meeples.put(p, amount);
        }
        // Copy over feature tiles
        for (Map.Entry<Tile, Direction> tile : f.getTiles().entrySet()) {
            tiles.put(tile.getKey(), tile.getValue());
        }
    }

    @Override
    public HashMap<Player, Integer> score() {
        HashMap<Player, Integer> res = new HashMap<>();
        if (isComplete() && meeples.size() > 0) {
            int max = Collections.max(meeples.values());
            for (Map.Entry<Player, Integer> m : meeples.entrySet())
                if (m.getValue() == max)
                    res.put(m.getKey(), tiles.size());
        }
        else
            for (Player p : meeples.keySet())
                res.put(p, tiles.size());
        return res;
    }

    @Override
    public boolean isComplete() {
        for (Segment s : segments)
            for (Segment t : s.getNeighbors())
                if (t == null) return false;
        return true;
    }

    @Override
    public FeatureType getType() {
        return FeatureType.ROAD;
    }

    @Override
    public Set<Segment> getSegments() {
        return segments;
    }

    @Override
    public Map<Player, Integer> getMeeples() {
        return meeples;
    }

    @Override
    public void returnMeeples() {
        for (Map.Entry<Player, Integer> meeple : meeples.entrySet()) {
            Player owner = meeple.getKey();
            int amount = meeple.getValue();
            for (int i = 0; i < amount; i++) owner.returnMeeple();
        }
        meeples.clear();
    }

    @Override
    public Map<Tile, Direction> getTiles() {
        return tiles;
    }

    @Override
    public void addMeeple(Player p, Tile tile, Direction dir) {
        if (p == null) throw new IllegalArgumentException("Invalid player provided when adding meeple.");
        meeples.putIfAbsent(p, 0);
        meeples.put(p, meeples.get(p) + 1);
        tiles.put(tile, dir);
    }

    @Override
    public String toString() {
        String res = "ROAD (tiles: " + tiles + ')';
        if (meeples.size() > 0) res = res.concat(" (meeples: " + meeples + ')');
        if (isComplete()) res = res.concat(" (completed)");
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RoadFeature)) return false;
        RoadFeature other = (RoadFeature) o;
        return getSegments().equals(other.getSegments())
                && getMeeples().equals(other.getMeeples());
    }

    @Override
    public int hashCode() {
        return 31 * segments.hashCode() * meeples.hashCode();
    }
}
