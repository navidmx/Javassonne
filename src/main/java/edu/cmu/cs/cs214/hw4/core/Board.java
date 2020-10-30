package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents the game board in Carcassonne, storing all the tiles and features.
 */
class Board {
    private final Map<Coordinate, Tile> tiles;
    private final Collection<Feature> features;
    private final Collection<Feature> completed;
    private final Collection<Coordinate> available;

    /**
     * Constructor for the Board class.
     */
    Board() {
        tiles = new HashMap<>();
        available = new HashSet<>();
        features = new ArrayList<>();
        completed = new HashSet<>();
    }

    /**
     * Get a segment's corresponding feature.
     * @param s The segment to search for.
     * @return The feature that contains the segment.
     */
    Feature getFeatureBySegment(Segment s) {
        for (Feature f : features)
            if (f.getSegments().contains(s)) return f;
        throw new IllegalArgumentException("Provided segment has not been initialized yet.");
    }

    /**
     * Get the available coordinates to place a tile.
     * @return A collection of available positions.
     */
    Collection<Coordinate> getAvailable() {
        return available;
    }

    /**
     * Gets the tile at the given coordinate.
     * @param coord Coordinate of tile to retrieve.
     * @return The tile at that coordinate.
     */
    Tile getTile(Coordinate coord) {
        return tiles.get(coord);
    }

    private List<Coordinate> getNeighbors(Coordinate c) {
        return Arrays.asList(c.getAbove(), c.getRight(), c.getBelow(), c.getLeft());
    }

    private void scoreFeature(Feature f) {
        for (Map.Entry<Player, Integer> results : f.score().entrySet())
            results.getKey().addScore(results.getValue());
        // Return meeples once done scoring feature.
        f.returnMeeples();
    }

    Map<Tile, Collection<Direction>> updateFeatures(boolean checkComplete) {
        Map<Tile, Collection<Direction>> meeplesRemoved = new HashMap<>();
        for (Feature f : features) {
            if (!checkComplete) scoreFeature(f);
            else if (!completed.contains(f) && f.isComplete()) {
                for (Map.Entry<Tile, Direction> meeple : f.getTiles().entrySet()) {
                    Tile tile = meeple.getKey();
                    Direction dir = meeple.getValue();
                    if (dir != null) {
                        meeplesRemoved.putIfAbsent(tile, new HashSet<>());
                        meeplesRemoved.get(tile).add(dir);
                    }
                }
                // Score feature, mark as completed, return meeples.
                scoreFeature(f);
                completed.add(f);
            }
        }
        return meeplesRemoved;
    }

    private boolean isCompatible(Segment a, Segment b) {
        return a.getType() == b.getType();
    }

    boolean isValid(Coordinate coord, Tile tile) {
        // Check that tile is not already on the board.
        if (tiles.containsKey(coord)) return false;
        if (!available.contains(coord)) return false;

        Tile above = tiles.get(coord.getAbove());
        Tile right = tiles.get(coord.getRight());
        Tile below = tiles.get(coord.getBelow());
        Tile left = tiles.get(coord.getLeft());

        // No tiles in the surrounding area.
        if (above == null && right == null && below == null && left == null) return false;

        // Check that each opposite segment is of the same feature type.
        if (above != null)
            if (!isCompatible(tile.getSegment(Direction.TOP), above.getSegment(Direction.BOTTOM)))
                return false;
        if (right != null)
            if (!isCompatible(tile.getSegment(Direction.RIGHT), right.getSegment(Direction.LEFT)))
                return false;
        if (below != null)
            if (!isCompatible(tile.getSegment(Direction.BOTTOM), below.getSegment(Direction.TOP)))
                return false;
        if (left != null)
            return isCompatible(tile.getSegment(Direction.LEFT), left.getSegment(Direction.RIGHT));

        return true;
    }

    /**
     * Places a tile on the board at the given coordinate.
     * @param coord Location to place the tile.
     * @param tile Tile to be placed.
     * @param skipCheck Flag to indicate that valid placement check should be skipped (e.g. first tile).
     */
    void placeTile(Coordinate coord, Tile tile, boolean skipCheck) {
        if (!skipCheck && !isValid(coord, tile))
            throw new IllegalArgumentException("Can not place tile " + tile + " at coordinate " + coord);

        tiles.put(coord, tile);

        // Get neighboring tiles.
        Tile above = getTile(coord.getAbove());
        Tile right = getTile(coord.getRight());
        Tile below = getTile(coord.getBelow());
        Tile left = getTile(coord.getLeft());

        // Place the tile and initialize segments with neighboring segments.
        tile.place(above, right, below, left);
        features.addAll(tile.getFeatures());

        // Update previous tiles' neighboring segments (if they exist).
        Segment a, b;
        FeatureType type;
        if (above != null) {
            a = above.getSegment(Direction.BOTTOM);
            b = tile.getSegment(Direction.TOP);
            a.setNeighbor(Direction.BOTTOM, b);
            type = a.getType();
            if (type != FeatureType.FIELD && type == b.getType())
                merge(getFeatureBySegment(a), getFeatureBySegment(b));
        }
        if (right != null) {
            a = right.getSegment(Direction.LEFT);
            b = tile.getSegment(Direction.RIGHT);
            a.setNeighbor(Direction.LEFT, b);
            type = a.getType();
            if (type != FeatureType.FIELD && type == b.getType())
                merge(getFeatureBySegment(a), getFeatureBySegment(b));
        }
        if (below != null) {
            a = below.getSegment(Direction.TOP);
            b = tile.getSegment(Direction.BOTTOM);
            a.setNeighbor(Direction.TOP, b);
            type = a.getType();
            if (type != FeatureType.FIELD && type == b.getType())
                merge(getFeatureBySegment(a), getFeatureBySegment(b));
        }
        if (left != null) {
            a = left.getSegment(Direction.RIGHT);
            b = tile.getSegment(Direction.LEFT);
            a.setNeighbor(Direction.RIGHT, b);
            type = a.getType();
            if (type != FeatureType.FIELD && type == b.getType())
                merge(getFeatureBySegment(a), getFeatureBySegment(b));
        }

        available.addAll(getNeighbors(coord));
        available.remove(coord);
    }

    void placeMeeple(Tile tile, Direction dir, Player player) {
        Segment s = tile.getSegment(dir);
        Feature f = getFeatureBySegment(s);
        f.addMeeple(player, tile, dir);
        player.useMeeple();
    }

    private void merge(Feature f1, Feature f2) {
        if (f1 == null)
            throw new IllegalArgumentException("Null provided for feature merge.");
        if (f1.equals(f2)) return;
        if (!features.remove(f2))
            throw new IllegalArgumentException("Feature " + f2 + " does not exist.");
        if (!(f1 instanceof CompoundFeature))
            throw new IllegalArgumentException("Features must be compound features to be merged.");
        if (f1.getType() != f2.getType())
            throw new IllegalArgumentException(f1 + " and " + f2 + " are not of the same type.");

        ((CompoundFeature) f1).merge((CompoundFeature) f2);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Current board: \n");
        for (Map.Entry<Coordinate, Tile> pos : tiles.entrySet()) {
            Coordinate coord = pos.getKey();
            Tile tile = pos.getValue();
            sb.append(coord.toString()).append(": ").append(tile.toString());
        }
        sb.append("\n").append(features);
        return sb.toString();
    }
}
