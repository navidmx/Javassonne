package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Game class to implement the top-level methods of GameAPI.
 */
public class Game implements GameAPI {
    private final Board board;
    private final TileDeck deck;
    private final List<GameChangeListener> listeners;
    private boolean running;
    private int turn;
    private Tile currTile;
    private Player currPlayer;
    private ArrayList<Player> players;
    private int playerCount;

    /**
     * Constructor for the game class.
     */
    public Game() {
        turn = 0;
        deck = new TileDeck("src/main/resources/tiles.json");
        board = new Board();
        listeners = new ArrayList<>();
        running = false;
    }

    /**
     * Register a game change listener to be notified of game change events.
     *
     * @param listener The listener to be registered.
     */
    @Override
    public void addGameChangeListener(GameChangeListener listener) {
        listeners.add(listener);
    }

    /**
     * Start game, initiating the starting tile and turn loop for the given number of players.
     * @param playerCount Number of players.
     */
    @Override
    public void startGame(int playerCount) {
        if (playerCount < 2 || playerCount > 5)
            throw new IllegalArgumentException("Game must have between 2 to 5 players.");

        running = true;
        this.playerCount = playerCount;

        // Initialize players.
        players = new ArrayList<>(playerCount);
        for (int i = 0; i < playerCount; i++)
            players.add(new Player(i + 1));

        // Set current player.
        currPlayer = players.get(0);

        // Draw first tile and place it at the origin.
        Coordinate origin = new Coordinate(0, 0);
        Tile startingTile = deck.draw();
        board.placeTile(origin, startingTile, true);
        notifyTilePlaced(origin, startingTile);

        // Load tiles and draw first tile (rotate until valid).
        currTile = deck.draw();
        while (getValidPositions().size() < 1)
            currTile.rotate();

        notifyGameStarted(playerCount);
        notifyTurnStarted(currPlayer, currTile);
    }

    // Rotate the current tile until it can be placed somewhere on the board.
    @Override
    public void rotateTile() {
        currTile.rotate();
        while (getValidPositions().size() < 1)
            currTile.rotate();
        notifyTileRotated(currTile);
    }

    @Override
    public Collection<Coordinate> getValidPositions() {
        HashSet<Coordinate> res = new HashSet<>();
        for (Coordinate c : board.getAvailable())
            if (board.isValid(c, currTile)) res.add(c);
        return res;
    }

    @Override
    public Collection<Direction> getValidSegments() {
        HashSet<Direction> res = new HashSet<>();
        if (currTile == null) return res;
        for (Direction d : Direction.values())
            if (checkSegment(d)) res.add(d);
        return res;
    }

    @Override
    public void placeTile(Coordinate coord) {
        board.placeTile(coord, currTile, false);
        notifyTilePlaced(coord, currTile);
    }

    @Override
    public void placeMeeple(Direction dir) {
        if (currPlayer.getMeeples() < 1) throw new IllegalArgumentException("Player is out of meeples.");
        board.placeMeeple(currTile, dir, currPlayer);
        notifyMeeplePlaced(dir);
    }

    @Override
    public void endTurn() {
        if (deck.size() == 0) {
            endGame(); // Determine if game should be over.
            return;
        }
        turn++; // Increment turn counter;
        currPlayer = players.get(turn % playerCount); // Set new player.
        // Draw a new tile and check all rotations - rotate if necessary until valid.
        currTile = deck.draw();
        while (!isTilePossible()) currTile = deck.draw();
        rotateTile();
        // Check to see if features are now completed and return any meeples.
        Map<Tile, Collection<Direction>> returnedMeeples = board.updateFeatures(true);
        for (Map.Entry<Tile, Collection<Direction>> tile : returnedMeeples.entrySet()) {
            for (Direction dir : tile.getValue())
                if (dir != null) notifyMeepleReturned(tile.getKey(), dir);
        }
        notifyTurnStarted(currPlayer, currTile);
    }

    @Override
    public Player[] getPlayers() {
        return players.toArray(Player[]::new);
    }

    @Override
    public int getRemainingTiles() {
        return deck.size();
    }

    @Override
    public Tile getTile() {
        return currTile;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    // Checks if any rotation of the tile is possible on the board.
    private boolean isTilePossible() {
        for (int i = 0; i < 4; i++) {
            currTile.rotate();
            if (getValidPositions().size() > 0) return true;
        }
        return false;
    }

    private void endGame() {
        running = false;
        Map<Tile, Collection<Direction>> returnedMeeples = board.updateFeatures(false);
        for (Map.Entry<Tile, Collection<Direction>> tile : returnedMeeples.entrySet())
            for (Direction dir : tile.getValue())
                notifyMeepleReturned(tile.getKey(), dir);
        notifyGameEnded();
    }

    private boolean checkSegment(Direction dir) {
        // Ensure segment is not a field.
        Segment s = currTile.getSegment(dir);
        if (s.getType() == FeatureType.FIELD) return false;

        // Ensure feature connected to segment has no meeples.
        Feature f = board.getFeatureBySegment(s);
        return f.getMeeples().size() == 0;
    }

    private void notifyGameStarted(int playerCount) {
        for (GameChangeListener l : listeners) l.gameStarted(playerCount);
    }

    private void notifyGameEnded() {
        for (GameChangeListener l : listeners) l.gameEnded();
    }

    private void notifyTilePlaced(Coordinate coord, Tile tile) {
        for (GameChangeListener l : listeners) l.tilePlaced(coord, tile);
    }

    private void notifyTileRotated(Tile tile) {
        for (GameChangeListener l : listeners) l.tileRotated(tile);
    }

    private void notifyTurnStarted(Player player, Tile tile) {
        for (GameChangeListener l : listeners) l.turnStarted(player, tile);
    }

    private void notifyMeeplePlaced(Direction dir) {
        for (GameChangeListener l : listeners) l.meeplePlaced(currTile, dir);
    }

    private void notifyMeepleReturned(Tile tile, Direction dir) {
        for (GameChangeListener l : listeners) l.meepleReturned(tile, dir);
    }

    /* Mainly for testing and debugging purposes */

    Board getBoard() {
        return board;
    }

    TileDeck getDeck() { return deck; }

    Player getPlayer() {
        return currPlayer;
    }

    void drawTile(char id) {
        currTile = deck.draw(id);
    }
}
