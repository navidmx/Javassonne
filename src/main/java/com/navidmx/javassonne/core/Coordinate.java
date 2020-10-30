package com.navidmx.javassonne.core;

import java.util.Objects;

/**
 * Represents a tile's coordinate on the Carcassonne board.
 */
public class Coordinate {
    private final int x;
    private final int y;

    /**
     * Constructor for the Coordinate class.
     * @param x x-axis position of the tile.
     * @param y y-axis position of the tile.
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the x-position of the tile.
     * @return the x position.
     */
    public int getX() {
        return x;
    }

    /**
     * Get the y-position of the tile.
     * @return the y position.
     */
    public int getY() {
        return y;
    }

    /**
     * Get coordinates for the tile above.
     * @return coordinates the tile above.
     */
    public Coordinate getAbove() {
        return new Coordinate(x, y - 1);
    }

    /**
     * Get coordinates for the tile below.
     * @return coordinates the tile below.
     */
    public Coordinate getBelow() {
        return new Coordinate(x, y + 1);
    }

    /**
     * Get coordinates for the tile to the right.
     * @return coordinates the tile to the right.
     */
    public Coordinate getRight() {
        return new Coordinate(x + 1, y);
    }

    /**
     * Get coordinates for the tile to the left.
     * @return coordinates the tile to the left.
     */
    public Coordinate getLeft() {
        return new Coordinate(x - 1, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinate)) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ')';
    }
}
