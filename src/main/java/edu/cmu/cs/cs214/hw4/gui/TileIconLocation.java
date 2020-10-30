package edu.cmu.cs.cs214.hw4.gui;

/**
 * Defines the location of a tile's ID to the tiles.png provided.
 * The locations and defined image size may be updated if a different file is provided.
 */
public enum TileIconLocation {
    A(1, 1), B(1, 2), C(1, 3), D(1, 4), E(1, 5), F(1, 6),
    G(2, 1), H(2, 2), I(2, 3), J(2, 4), K(2, 5), L(2, 6),
    M(3, 1), N(3, 2), O(3, 3), P(3, 4), Q(3, 5), R(3, 6),
    S(4, 1), T(4, 2), U(4, 3), V(4, 4), W(4, 5), X(4, 6);

    public static final int SIZE = 90;
    private final int x, y;

    TileIconLocation(int row, int col) {
        this.x = (col - 1) * SIZE;
        this.y = (row - 1) * SIZE;
    }

    /**
     * Return the x coordinate (in pixels) of the image.
     * @return the x coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Return the y coordinate (in pixels) of the image.
     * @return the y coordinate.
     */
    public int getY() {
        return y;
    }
}
