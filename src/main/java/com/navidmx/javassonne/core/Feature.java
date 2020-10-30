package com.navidmx.javassonne.core;

import java.util.Collection;
import java.util.Map;

/**
 * Represents a feature currently on the board.
 */
interface Feature {
    /**
     * Score the feature, calculating the number of points for each
     * @return a HashMap of each player that had meeples in the feature
     * along with the score they received from the feature.
     */
    Map<Player, Integer> score();

    /**
     * State of feature's completion.
     * @return true if the feature is complete, false otherwise.
     */
    boolean isComplete();

    /**
     * Get the type of feature all segments of this feature should be.
     * @return The feature type.
     */
    FeatureType getType();

    /**
     * Get a list of current segments in the feature.
     * @return a list of the feature's segments.
     */
    Collection<Segment> getSegments();

    /**
     * Get a list of players whose meeples are in the feature.
     * @return a list of the feature's meeple owners and the amount they own.
     */
    Map<Player, Integer> getMeeples();

    /**
     * Get the tiles the feature spans.
     * @return The tiles the feature spans and any segments that contains meeples on those tiles.
     */
    Map<Tile, Direction> getTiles();

    /**
     * Return meeples in the feature to their owners.
     */
    void returnMeeples();

    /**
     * Add a meeple to the feature.
     * @param player Player whose meeple is being added.
     * @param tile Tile it was placed on.
     * @param dir The direction it was placed on.
     */
    void addMeeple(Player player, Tile tile, Direction dir);
}
