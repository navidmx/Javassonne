package edu.cmu.cs.cs214.hw4.core;

import java.util.List;
import java.util.Map;

/**
 * Listens for game change events in Carcassonne.
 */
public interface GameChangeListener {

    /**
     * Called when the game starts.
     * @param playerCount The number of players in the game.
     */
    void gameStarted(int playerCount);

    /**
     * Called when the game ends.
     */
    void gameEnded();

    /**
     * Called when a turn starts.
     * @param player The player whose turn it is.
     * @param tile The tile drawn for this turn.
     */
    void turnStarted(Player player, Tile tile);

    /**
     * Called when a tile is placed on the board.
     * @param coord Coordinate where it was placed.
     * @param tile The tile placed.
     */
    void tilePlaced(Coordinate coord, Tile tile);

    /**
     * Called when the tile is rotated.
     * @param tile The tile that was rotated.
     */
    void tileRotated(Tile tile);

    /**
     * Called when a meeple is placed.
     * @param tile The tile the meeple was placed on.
     * @param direction The direction of the segment the meeple was placed on.
     */
    void meeplePlaced(Tile tile, Direction direction);

    /**
     * Called when a meeple is returned to its owner.
     * @param tile The tile the meeple was placed on.
     * @param dir The segment the meeple was removed from.
     */
    void meepleReturned(Tile tile, Direction dir);
}
