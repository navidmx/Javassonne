package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a tile on the board, with each of its segments.
 */
public class Tile {
    private final HashMap<Direction, Segment> segments;
    private final ArrayList<Feature> features;
    private final Character id;
    private final boolean shield;
    private final List<List<Direction>> links;
    private int rotation;

    /**
     * Constructor for a tile.
     * @param id Character identifier for the tile.
     * @param top The feature of the top segment.
     * @param right The feature of the right segment.
     * @param bottom The feature of the bottom segment.
     * @param left The feature of the left segment.
     * @param center The feature of the center segment.
     * @param shield Whether the tile has a shield.
     * @param links A list of linked segments in the tile.
     */
    public Tile(Character id,
                FeatureType top,
                FeatureType right,
                FeatureType bottom,
                FeatureType left,
                FeatureType center,
                boolean shield, List<List<Direction>> links) {
        this.id = id;
        this.shield = shield;
        this.links = links;
        segments = new HashMap<>(5);
        features = new ArrayList<>(4);
        rotation = 0;

        segments.put(Direction.TOP, new Segment(top));
        segments.put(Direction.RIGHT, new Segment(right));
        segments.put(Direction.BOTTOM, new Segment(bottom));
        segments.put(Direction.LEFT, new Segment(left));
        segments.put(Direction.CENTER, new Segment(center));

        Set<Direction> completed = new HashSet<>(5);

        // Create temporary features out of the defined links in the tile.
        for (List<Direction> link : links) {
            // Get the type of the feature.
            FeatureType type = segments.get(link.get(0)).getType();
            // Create a temporary feature as a list of segments.
            List<Segment> feature = new ArrayList<>(link.size());
            for (Direction dir : link) {
                completed.add(dir);
                feature.add(segments.get(dir));
            }
            // Compound features can only be cities or roads (and technically, fields).
            if (type == FeatureType.CITY) {
                CityFeature f = new CityFeature(this, feature);
                if (shield) f.addShields(1);
                features.add(f);
            }
            else if (type == FeatureType.ROAD) features.add(new RoadFeature(this, feature));
        }

        // Create temporary features out of the remaining segments.
        for (Direction d : Direction.values()) {
            if (!(completed.contains(d))) {
                Segment s = segments.get(d);
                FeatureType type = s.getType();
                switch (type) {
                    case MONASTERY: features.add(new MonasteryFeature(this, s)); break;
                    // Cities and roads are compound features, so passing in segments as singletons.
                    case ROAD: features.add(new RoadFeature(this, Collections.singletonList(s))); break;
                    case CITY:
                        CityFeature f = new CityFeature(this, Collections.singletonList(s));
                        if (shield) f.addShields(1);
                        features.add(f);
                        break;
                    default: break;
                }
            }
        }
    }

    /**
     * Get the unique ID of the tile.
     * @return the character identifier.
     */
    public char getId() {
        return id;
    }

    /**
     * Get the number of times the tile has been rotated clockwise.
     * @return The number of rotations (0-3).
     */
    public int getRotations() {
        return rotation;
    }

    /**
     * Get a map of the segments at every direction of a tile.
     * @return The map of directions to segments.
     */
    HashMap<Direction, Segment> getSegments() {
        return new HashMap<>(segments);
    }

    /**
     * Retrieve whether the tile bears a coat of arms.
     * @return whether the tile has a shield.
     */
    boolean hasShield() {
        return shield;
    }

    /**
     * Rotates all segments of the tile clockwise.
     */
    void rotate() {
        Segment top = segments.get(Direction.TOP);
        Segment right = segments.get(Direction.RIGHT);
        Segment bottom = segments.get(Direction.BOTTOM);
        Segment left = segments.get(Direction.LEFT);
        segments.put(Direction.TOP, left);
        segments.put(Direction.RIGHT, top);
        segments.put(Direction.BOTTOM, right);
        segments.put(Direction.LEFT, bottom);
        rotation++;
        rotation = rotation % 4;
    }

    /**
     * Place the tile, locking in its segments neighbors.
     * @param top The tile above.
     * @param right The tile to the right.
     * @param bottom The tile below.
     * @param left The tile to the left.
     */
    void place(Tile top, Tile right, Tile bottom, Tile left) {
        Segment segmentTop = segments.get(Direction.TOP);
        Segment segmentRight = segments.get(Direction.RIGHT);
        Segment segmentBottom = segments.get(Direction.BOTTOM);
        Segment segmentLeft = segments.get(Direction.LEFT);
        Segment segmentCenter = segments.get(Direction.CENTER);

        // Get the neighboring segment from each neighboring tile.
        Segment neighborTop = top == null ? null : top.getSegment(Direction.BOTTOM);
        Segment neighborRight = right == null ? null : right.getSegment(Direction.LEFT);
        Segment neighborLeft = left == null ? null : left.getSegment(Direction.RIGHT);
        Segment neighborBottom = bottom == null ? null : bottom.getSegment(Direction.BOTTOM);

        // Set neighbors for each segment out of neighboring tiles and current tile.
        segmentTop.setNeighbors(neighborTop, segmentRight, segmentBottom, segmentLeft);
        segmentRight.setNeighbors(segmentTop, neighborRight, segmentBottom, segmentLeft);
        segmentBottom.setNeighbors(segmentTop, segmentRight, neighborBottom, segmentLeft);
        segmentLeft.setNeighbors(segmentTop, segmentRight, segmentBottom, neighborLeft);
        segmentCenter.setNeighbors(segmentTop, segmentRight, segmentBottom, segmentLeft);
    }

    /**
     * Get the segment for a specific direction of the tile.
     * @param dir The direction of the segment to return.
     * @return The segment in that direction.
     */
    Segment getSegment(Direction dir) {
        return segments.get(dir);
    }

    List<Feature> getFeatures() {
        return features;
    }

    Tile copy() {
        return new Tile(this.id,
                segments.get(Direction.TOP).getType(),
                segments.get(Direction.RIGHT).getType(),
                segments.get(Direction.BOTTOM).getType(),
                segments.get(Direction.LEFT).getType(),
                segments.get(Direction.CENTER).getType(),
                shield,
                links);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // Create shorthand names for each feature type (e.g. City -> C).
        char top = segments.get(Direction.TOP).getType().toString().charAt(0);
        char right = segments.get(Direction.RIGHT).getType().toString().charAt(0);
        char bottom = segments.get(Direction.BOTTOM).getType().toString().charAt(0);
        char left = segments.get(Direction.LEFT).getType().toString().charAt(0);
        char center = segments.get(Direction.CENTER).getType().toString().charAt(0);
        /*
         Print in a directional pattern.
         e.g.   A:    F
                    F M F
                      R
         */
        sb.append("\n").append("    ").append(top).append("   ").append("\n");
        sb.append("  ").append(left).append(" ").append(center).append(" ").append(right).append(" ").append("\n");
        sb.append("    ").append(bottom).append("   \n");
        return sb.toString();
    }
}
