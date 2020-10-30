package edu.cmu.cs.cs214.hw4.core;

import java.util.Collection;
import java.util.HashMap;

/**
 * Represents a segment in one of the tile's 5 directions (top, right, bottom, left, center).
 */
public class Segment {
    private final FeatureType type;
    private final HashMap<Direction, Segment> neighbors;

    /**
     * Constructor for the segment class.
     * @param type The feature type of the segment.
     */
    public Segment(FeatureType type) {
        this.type = type;
        neighbors = new HashMap<>();
    }

    /**
     * Set a neighboring segment by direction.
     * @param dir The direction of the neighboring segment.
     * @param s The neighboring segment.
     */
    void setNeighbor(Direction dir, Segment s) {
        neighbors.put(dir, s);
    }

    /**
     * Set all the neighboring segments of a segment.
     * @param above The above segment.
     * @param right The segment to the right.
     * @param below The below segment.
     * @param left The segment to the left.
     */
    void setNeighbors(Segment above, Segment right, Segment below, Segment left) {
        neighbors.put(Direction.TOP, above);
        neighbors.put(Direction.RIGHT, right);
        neighbors.put(Direction.BOTTOM, below);
        neighbors.put(Direction.LEFT, left);
    }

    Segment getNeighbor(Direction dir) {
        return neighbors.get(dir);
    }

    Collection<Segment> getNeighbors() {
        return neighbors.values();
    }

    /**
     * Retrieve the feature type of a segment.
     * @return The feature type.
     */
    public FeatureType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type + "Segment";
    }
}
