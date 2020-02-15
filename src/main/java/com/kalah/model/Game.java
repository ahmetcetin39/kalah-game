package com.kalah.model;

import com.kalah.enumaration.GameStatus;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

/**
 * This represents the model of the Kalah game.
 * Id of Game is used to separate games from each other.
 *
 * @author Ahmet Cetin
 * @since 2020-02-12
 */
@Builder
@Value
public class Game {
    private String id;
    private Board board;
    private GameStatus gameStatus;
    private String winner;

    public static Game emptyGame() {
        return Game.builder()
                .gameStatus(GameStatus.NOT_STARTED)
                .board(new Board(new int[14]))
                .build();
    }

    public static Game newGame(GameStatus gameStatus) {
        return Game.builder()
                .id(UUID.randomUUID().toString())
                .board(new Board())
                .gameStatus(gameStatus)
                .build();
    }

}
