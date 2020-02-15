# What is this?
This is a Spring Boot based web application which has a UI to be able to play Kalah game.

This application is built on Spring Boot 2.2 with Maven and uses Java 11 and AngularJS to render UI.

* It is possible to play multiple games at the same time since each game is stored in memory using game id.

## Kalah Game
Kalah, also called Kalaha or Mancala, is a game in the mancala family imported in the United States.

### Rules:
* At the beginning of the game, four seeds are placed in each house. This is the traditional method.
* Each player controls the six houses and their seeds on the player's side of the board. The player's score is the number of seeds in the store to their right.
* Players take turns sowing their seeds. On a turn, the player removes all seeds from one of the houses under their control. Moving counter-clockwise, the player drops one seed in each house in turn, including the player's own store but not their opponent's.
* If the last sown seed lands in an empty house owned by the player, and the opposite house contains seeds, both the last seed and the opposite seeds are captured and placed into the player's store.
* If the last sown seed lands in the player's store, the player gets an additional move. There is no limit on the number of moves a player can make in their turn.
* When one player no longer has any seeds in any of their houses, the game ends. The other player moves all remaining seeds to their store, and the player with the most seeds in their store wins.

NOTE:  It is possible for the game to end in a draw.

For more information about the game, please visit [here](https://en.wikipedia.org/wiki/Kalah).

## How to run?
Download the project by cloning it:
```
git clone git@github.com:ahmetcetin39/kalah-game.git
```

Run the application from an IDE with a default profile. Or run this from terminal:
```
mvn spring-boot:run
```

Go to [http://localhost:8080](http://localhost:8080)

For API documentation please visit [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)


## How to play?
* To start playing, first a beginner player should be chosen from the screen.
* Then, chosen player is able to play.
* Both users must play from the same screen.
* When one of the users don't have any stones anymore on his houses, then game ends and winner appears under board.

NOTE:  In one game, both players must use the same screen.

## Author
Ahmet Cetin
