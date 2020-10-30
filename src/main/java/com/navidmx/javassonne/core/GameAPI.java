package com.navidmx.javassonne.core;

import java.util.Collection;

/**
 * Provides all top-level methods necessary for Carcassonne gameplay on command line or GUI.
 */
public interface GameAPI {

    /**
     * Register a game change listener to be notified of game change events.
     * @param listener The listener to be registered.
     */
    void addGameChangeListener(GameChangeListener listener);

    /**
     * Start game, initiating the starting tile and turn loop for the given number of players.
     * @param noPlayers Number of players.
     */
    void startGame(int noPlayers);

    /**
     * Rotates the current tile.
     */
    void rotateTile();

    /**
     * Attempts to place the current tile at the given coordinates.
     * @param coord The coordinate where the tile should be placed.
     */
    void placeTile(Coordinate coord);

    /**
     * Place a user's meeple in the given direction.
     * @param dir Direction to place the meeple (TOP, LEFT, BOTTOM, RIGHT, CENTER) on tile.
     */
    void placeMeeple(Direction dir);

    /**
     * End the current player's turn.
     */
    void endTurn();

    /**
     * Status check to see if game is currently running (e.g. started, but not yet out of tiles).
     * @return whether the game is running.
     */
    boolean isRunning();

    /**
     * Get an array of the current players.
     * @return the map of players.
     */
    Player[] getPlayers();

    /**
     * Get a representation of the current tile drawn.
     * @return the tile currently drawn.
     */
    Tile getTile();

    /**
     * Get a list of all possible positions to place the current tile and rotation.
     * @return a collection of valid coordinates.
     */
    Collection<Coordinate> getValidPositions();

    /**
     * Get a list of directions where placing a meeple would be valid on the given tile.
     * @return a collection of valid directions.
     */
    Collection<Direction> getValidSegments();

    /**
     * Get the number of tiles remaining in the deck.
     * @return the number of remaining tiles.
     */
    int getRemainingTiles();
}
