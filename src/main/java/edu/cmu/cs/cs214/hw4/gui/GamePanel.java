package edu.cmu.cs.cs214.hw4.gui;

import edu.cmu.cs.cs214.hw4.core.Coordinate;
import edu.cmu.cs.cs214.hw4.core.Direction;
import edu.cmu.cs.cs214.hw4.core.GameAPI;
import edu.cmu.cs.cs214.hw4.core.GameChangeListener;
import edu.cmu.cs.cs214.hw4.core.Player;
import edu.cmu.cs.cs214.hw4.core.Tile;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Primary game panel for Carcassonne.
 */
public class GamePanel extends JPanel implements GameChangeListener {
    // Dimensions for various panels and buttons
    private static final int
            BOARD_SIZE = 3000,
            WINDOW_WIDTH = 1200,
            WINDOW_HEIGHT = 750,
            VIEWPORT_WIDTH = 900,
            VIEWPORT_HEIGHT = 750,
            TILE_SIZE = TileIconLocation.SIZE,
            ORIGIN = BOARD_SIZE / 2 - TILE_SIZE / 2;
    // Colors for panels and buttons and tile selectors
    private static final Color
            SIDEBAR_COLOR = new Color(242, 242, 242),
            VALID_POS_COLOR = new Color(204, 242, 199);
    // Image with tile icons
    private final BufferedImage image = ImageIO.read(new File(TileIcon.FILE_LOCATION));
    private final GameAPI game;
    private final List<JButton> validPos = new ArrayList<>();
    private final Color[] playerColors = new Color[] {
            new Color(245, 46, 46),
            new Color(84, 99, 255),
            new Color(31, 158, 64),
            new Color(255, 199, 23),
            new Color(50, 50, 50)
    };
    private final Map<Tile, JLabel> tiles = new HashMap<>();
    private final Map<Tile, Collection<Direction>> placedMeeples = new HashMap<>();
    private final JPanel start;
    private final Sidebar sidebar;
    private final JScrollPane boardView;
    private final MeepleSelector meepleSelector;
    private final BoardPanel board;
    private JButton selected, place, rotate, endTurn;
    private JLabel[] players, scores, meeples;
    private JLabel currTileLabel, tilesRemaining;
    private Tile currTile;
    private Player currPlayer;
    private Coordinate currCoord;

    /**
     * Constructor for the game panel.
     * @param game the implementation of the game to use.
     * @throws IOException if file path provided is invalid.
     */
    public GamePanel(GameAPI game) throws IOException {
        // Register game's event listener
        this.game = game;
        game.addGameChangeListener(this);

        // Set up the panel's size and layout
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setLayout(new BorderLayout());

        // Create the splash screen
        start = new StartScreen(WINDOW_WIDTH, WINDOW_HEIGHT, SIDEBAR_COLOR, game);
        add(start);

        // Pre-load the game components for when game starts
        board = new BoardPanel(BOARD_SIZE);
        boardView = board.createViewer(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

        sidebar = new Sidebar(game, WINDOW_WIDTH - VIEWPORT_WIDTH, WINDOW_HEIGHT, SIDEBAR_COLOR);
        meepleSelector = new MeepleSelector(SIDEBAR_COLOR, game);
        loadSidebar();
    }

    @Override
    public void gameStarted(int playerCount) {
        remove(start);
        add(sidebar, BorderLayout.EAST);
        add(boardView, BorderLayout.WEST);
        // Initialize player column tables based on the player count.
        players = new JLabel[playerCount];
        scores = new JLabel[playerCount];
        meeples = new JLabel[playerCount];
        sidebar.createPlayerTable(playerCount, players, meeples, scores, playerColors);
        update(this);
    }

    @Override
    public void gameEnded() {
        remove(sidebar);
        add(new EndScreen(sortByScore(game.getPlayers()), playerColors));
        update(this);
    }

    @Override
    public void turnStarted(Player player, Tile tile) {
        int id = player.getId();
        for (Player p : game.getPlayers()) {
            int i = p.getId() - 1;
            String name = "Player " + (i + 1);
            // Highlight the current player.
            if ((i + 1) == id) {
                players[i].setText("> " + name);
                players[i].setFont(new Font(players[i].getText(), Font.BOLD, 15));
                scores[i].setFont(new Font(scores[i].getText(), Font.BOLD, 15));
                meeples[i].setFont(new Font(meeples[i].getText(), Font.BOLD, 15));
            }
            else {
                players[i].setText(name);
                players[i].setFont(new Font(players[i].getText(), Font.PLAIN, 14));
                scores[i].setFont(new Font(scores[i].getText(), Font.PLAIN, 14));
                meeples[i].setFont(new Font(meeples[i].getText(), Font.PLAIN, 14));
            }
            // Update meeple/score counts.
            scores[i].setText(String.valueOf(p.getScore()));
            meeples[i].setText(String.valueOf(p.getMeeples()));
        }
        currTile = tile;
        currPlayer = player;
        // Prevent user from ending turn before they've placed tile
        endTurn.setEnabled(false);
        // Allow user to rotate new tile
        rotate.setEnabled(true);
        // Draw the current tile
        currTileLabel.setIcon(drawTile(tile));
        // Update the number of tiles remaining
        tilesRemaining.setText(String.valueOf(game.getRemainingTiles()));
        drawValidPositions();
        meepleSelector.unload();
        update(sidebar);
    }

    @Override
    public void tilePlaced(Coordinate coord, Tile tile) {
        selected = null;
        place.setEnabled(false);
        endTurn.setEnabled(true);
        for (JButton pos : validPos) board.remove(pos);
        validPos.clear();
        // Create and place the tile on the board.
        JLabel tilePiece = new JLabel();
        int x = ORIGIN + coord.getX() * TILE_SIZE;
        int y = ORIGIN + coord.getY() * TILE_SIZE;
        tilePiece.setBounds(x, y, TILE_SIZE, TILE_SIZE);
        tilePiece.setIcon(drawTile(tile));
        board.add(tilePiece);
        tiles.put(tile, tilePiece);
        // Redraw board
        update(board);
    }

    @Override
    public void tileRotated(Tile tile) {
        place.setEnabled(false);
        for (JButton pos : validPos) board.remove(pos);
        validPos.clear();
        currTile = tile;
        currTileLabel.setIcon(drawTile(tile));
        drawValidPositions();
        update(board, sidebar);
    }

    @Override
    public void meeplePlaced(Tile tile, Direction dir) {
        placedMeeples.putIfAbsent(tile, new HashSet<>());
        Collection<Direction> directions = placedMeeples.get(tile);
        directions.add(dir);
        tiles.get(tile).setIcon(drawTile(tile, directions.toArray(new Direction[0])));
    }

    @Override
    public void meepleReturned(Tile tile, Direction dir) {
        Collection<Direction> directions = placedMeeples.get(tile);
        directions.remove(dir);
        tiles.get(tile).setIcon(drawTile(tile, directions.toArray(new Direction[0])));
    }

    private void selectTile(JButton button, Coordinate coord) {
        selected = button;
        // Set a semi-transparent preview of the tile placement.
        selected.setIcon(new TransparentImageIcon(drawTile(currTile), 0.5f));
        rotate.setEnabled(false);
        place.setEnabled(true);
        currCoord = coord;
    }

    private void drawValidPositions() {
        for (Coordinate coord : game.getValidPositions()) {
            JButton highlight = new JButton();
            int x = ORIGIN + coord.getX() * TILE_SIZE;
            int y = ORIGIN + coord.getY() * TILE_SIZE;
            highlight.setBounds(x, y, TILE_SIZE, TILE_SIZE);
            highlight.setBackground(VALID_POS_COLOR);
            highlight.addActionListener(e -> {
                // If tile already selected, deselect it.
                if (selected == highlight) {
                    highlight.setIcon(null);
                    rotate.setEnabled(true);
                    place.setEnabled(false);
                    selected = null;
                } else { // Otherwise, select it.
                    if (selected != null) selected.setIcon(null);
                    selectTile(highlight, coord);
                }
            });
            highlight.setOpaque(true);
            highlight.setBorderPainted(false);
            highlight.setCursor(new Cursor(Cursor.HAND_CURSOR));
            validPos.add(highlight);
            board.add(highlight);
        }
        update(board, boardView);
    }

    private void loadSidebar() {
        // Add the current tile view
        sidebar.addDivider(50);
        currTileLabel = sidebar.createCurrTile(TILE_SIZE);
        // Add the rotate tile button
        sidebar.addDivider(25);
        rotate = sidebar.createRotateButton();
        // Add the place tile button
        sidebar.addDivider(10);
        place = sidebar.createPlaceButton();
        place.addActionListener(e -> {
            game.placeTile(currCoord);
            Direction[] segments = game.getValidSegments().toArray(new Direction[0]);
            // If there are places for meeples to be placed AND player has meeples remaining, let them place one.
            if (segments.length > 0 && currPlayer.getMeeples() > 0) {
                meepleSelector.load(segments, currCoord);
                update(meepleSelector);
                currTileLabel.setIcon(drawTile(currTile, segments));
                update(sidebar);
            }
            // Otherwise, force end their turn.
            else game.endTurn();
        });
        // Add the end turn button
        sidebar.addDivider(10);
        endTurn = sidebar.createEndTurnButton();
        // Add the meeple selector
        sidebar.addDivider(10);
        sidebar.add(meepleSelector);
        // Add the tiles remaining counter
        sidebar.addDivider(230);
        tilesRemaining = sidebar.createTilesRemaining();
        // Leave room for the players table
        sidebar.addDivider(10);
    }

    // Utility to sort player scores in descending order.
    private Player[] sortByScore(Player[] players) {
        List<Player> sorted = Arrays.asList(players);
        sorted.sort(Comparator.comparing(Player::getScore).reversed());
        return sorted.toArray(new Player[0]);
    }

    // Utility to update given components.
    private void update(JComponent...comps) {
        for (JComponent comp : comps) {
            comp.revalidate();
            comp.repaint();
        }
    }

    // Utility to create an image, with optional meeples.
    private ImageIcon drawTile(Tile tile, Direction ...meeples) {
        TileIconLocation icon = TileIconLocation.valueOf(tile.getId() + "");
        BufferedImage img = image.getSubimage(icon.getX(), icon.getY(), TILE_SIZE, TILE_SIZE);
        Color color = currPlayer != null ? playerColors[currPlayer.getId() - 1] : null;
        return new TileIcon(img, tile, color, meeples);
    }
}
