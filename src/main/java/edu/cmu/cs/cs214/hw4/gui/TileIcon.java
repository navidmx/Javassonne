package edu.cmu.cs.cs214.hw4.gui;

import edu.cmu.cs.cs214.hw4.core.Direction;
import edu.cmu.cs.cs214.hw4.core.Tile;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * Class to represent tiles as image icons, with options to rotate the tile and place meeples.
 */
class TileIcon extends ImageIcon {
    public static final String FILE_LOCATION = "src/main/resources/tiles.png";
    private static final int MEEPLE_RADIUS = 8, MEEPLE_PADDING = 15, TILE_SIZE = 90;
    private BufferedImage img;

    TileIcon(BufferedImage img, Tile tile, Color color, Direction ...meeples) {
        this.img = img;
        // Rotate image if need be
        int rotations = tile.getRotations();
        if (rotations > 0) this.img = rotateClockwise(img, rotations);
        // Add meeples if need be
        if (color != null) addMeeples(color, meeples);
        setImage(this.img);
    }

    private void addMeeples(Color color, Direction ...meeples) {
        for (Direction dir : meeples) {
            switch(dir) {
                case TOP: img = withCircle(img, color, TILE_SIZE / 2, MEEPLE_PADDING); break;
                case RIGHT: img = withCircle(img, color, TILE_SIZE - MEEPLE_PADDING, TILE_SIZE / 2); break;
                case BOTTOM: img = withCircle(img, color, TILE_SIZE / 2, TILE_SIZE - MEEPLE_PADDING); break;
                case LEFT: img = withCircle(img, color, MEEPLE_PADDING, TILE_SIZE / 2); break;
                case CENTER: img = withCircle(img, color, TILE_SIZE / 2, TILE_SIZE / 2); break;
                default: break;
            }
        }
    }

    // Utility to rotate an image.
    // Source: 17-214 Piazza.
    private BufferedImage rotateClockwise(BufferedImage src, int n) {
        int weight = src.getWidth();
        int height = src.getHeight();

        AffineTransform at = AffineTransform.getQuadrantRotateInstance(n, weight / 2.0, height / 2.0);
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

        BufferedImage dest = new BufferedImage(weight, height, src.getType());
        op.filter(src, dest);
        return dest;
    }

    // Utility to draw a circle on an image.
    // Source: 17-214 Piazza.
    private BufferedImage withCircle(BufferedImage src, Color color, int x, int y) {
        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        Graphics2D g = (Graphics2D) dest.getGraphics();
        g.drawImage(src, 0, 0, null);
        g.setColor(color);
        g.fillOval(x - MEEPLE_RADIUS,y - MEEPLE_RADIUS, 2 * MEEPLE_RADIUS,2 * MEEPLE_RADIUS);
        g.dispose();
        return dest;
    }
}
