package com.navidmx.javassonne.core;

import java.util.Collection;
import java.util.Scanner;

/**
 * Test file to run the game on the command line.
 */
public class TestGame {
    private static final String DIVIDER = "---------------------------------------";

    public static void main(String[] args) {
        System.out.println("--------------------------------------------------");
        System.out.println("|               17-214 CARCASSONNE               |");
        System.out.println("|             Navid Mamoon (nmamoon)             |");
        System.out.println("--------------------------------------------------");

        Game game = new Game();
        Scanner in = new Scanner(System.in, "UTF-8");

        System.out.println("Enter number of players (2-5): ");
        game.startGame(in.nextInt());

        while(game.isRunning()) {
            System.out.println(DIVIDER);
            System.out.println(game.getBoard());
            System.out.println(DIVIDER);
            System.out.println("Players: ");
            for (Player p : game.getPlayers())
                System.out.println((p.equals(game.getPlayer()) ? " > " : "   ") + p);
            System.out.println("Remaining tiles: " + game.getRemainingTiles());
            System.out.print("Current tile: " + game.getTile().getId());
            if (game.getTile().hasShield()) System.out.print(" (coat of arms)\n");
            System.out.println(game.getTile());
            System.out.println(DIVIDER);
            System.out.println("Available locations: " + game.getValidPositions());
            System.out.println("Type 'p' to place, 'r' to rotate, and 'q' to quit: ");

            String res = in.next();
            if (res.equals("q")) break;

            switch(res) {
                case "p":
                    System.out.println("Enter coordinates to place (x,y): ");
                    String[] coords = in.next().split(",");
                    if (coords.length != 2) {
                        System.out.println("Invalid coordinates provided.");
                        break;
                    }
                    int x = Integer.parseInt(coords[0]);
                    int y = Integer.parseInt(coords[1]);
                    System.out.println("Placing at... " + x + ", " + y);
                    game.placeTile(new Coordinate(x, y));
                    Collection<Direction> validSegments = game.getValidSegments();
                    if (game.getPlayer().getMeeples() > 0 && validSegments.size() > 0) {
                        System.out.println("Place meeple (y/n)? ");
                        if (in.next().equals("y")) {
                            System.out.println("Select a direction " + validSegments + ": ");
                            try {
                                Direction d = Direction.valueOf(in.next());
                                if (validSegments.contains(d)) game.placeMeeple(d);
                                else System.out.println("Invalid placement.");
                            }
                            catch (IllegalArgumentException e) {
                                System.out.println("Invalid placement.");
                            }
                        }
                    }
                    game.endTurn();
                    break;
                case "r":
                    game.rotateTile();
                    break;
                default:
                    System.out.println("Invalid input.");
                    break;
            }
        }

        System.out.println("Game over!");
        System.out.println("Final scores: ");
        for (Player p : game.getPlayers()) {
            System.out.println(p.toString());
        }
    }
}
