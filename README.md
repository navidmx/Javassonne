# Javassonne
Homework 4 Carcassonne Implementation for 17-214.

### Running the game
To run the game, simply execute `gradle run` or run the main function in the class `RunGame`.

### Playing the game
The game should function as a normal Carcassonne game, with the following steps:
1. Select the number of players (2-5)
2. A tile will be drawn, the player may:
    - Rotate the tile by hitting `ROTATE` (the tile will only be rotated to orientations
    that can be placed on the board properly)
    - Place the tile by selecting a valid position (marked in green) and then clicking
    `PLACE`
3. After placing a tile:
    - If the player has meeples remaining, and the placed tile has an available feature,
    the user
  can
      - End their turn by clicking `END TURN` OR
      - Place a meeple by selecting one of the valid directions in the meeple selector
      after which
  the turn will automatically end
    - Otherwise, the turn will also end automatically
4. Steps 2-3 will repeat until all the deck is empty, after which final scores
(including incomplete features) will be shown