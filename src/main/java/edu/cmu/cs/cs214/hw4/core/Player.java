package edu.cmu.cs.cs214.hw4.core;

/**
 * Representation of a player in Carcassonne.
 */
public class Player {
    private final int id;
    private int score;
    private int meepleCount;

    /**
     * Constructor for a player, taking in their player ID.
     * @param id Player's unique ID.
     */
    public Player(int id) {
        this.id = id;
        meepleCount = 7;
    }

    /**
     * Get the player's current score.
     * @return Player's score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Get the player identifier.
     * @return the player id (1-5).
     */
    public int getId() {
        return id;
    }

    /**
     * Get the number of meeples remaining for the player.
     * @return Remaining meeples.
     */
    public int getMeeples() {
        return meepleCount;
    }

    void addScore(int amount) {
        score += amount;
    }

    void useMeeple() {
        meepleCount--;
    }

    void returnMeeple() {
        meepleCount++;
    }

    @Override
    public String toString() {
        return "Player " + id + " (Score: " + score + ", Meeples: " + meepleCount + ')';
    }
}
