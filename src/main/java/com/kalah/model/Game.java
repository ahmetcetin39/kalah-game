package com.kalah.model;

import com.kalah.enumaration.GameStatus;
import lombok.Builder;
import lombok.Value;

/**
 * This represents the model of the Kalah game.
 * Id of Game is used to separate games from each other.
 *
 * @author Ahmet Cetin
 * @since 2020-01-16
 */
@Builder
@Value
public class Game {
    private String id;
    private Board board;
    private GameStatus gameStatus;
    private String winner;
}
