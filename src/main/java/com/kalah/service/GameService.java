package com.kalah.service;

import com.kalah.enumaration.GameInitiator;
import com.kalah.model.Game;

/**
 * This is the service interface of game service.
 *
 * @author Ahmet Cetin
 * @since 2020-02-12
 */
public interface GameService {
    /**
     * This is used to return an empty board to the UI.
     *
     * @return the empty board where all houses and stores and empty.
     */
    Game initBoard();

    /**
     * This is used to initialize the Kalah game.
     * It creates a unique id for the game and this id is used to play the rounds.
     *
     * @param gameInitiator the player who will play the first round.
     * @return the initialized game.
     */
    Game startGame(GameInitiator gameInitiator);

    /**
     * This is used to play round of Kalah game.
     * It makes necessary validations, play the round and update the game in cache.
     *
     * @param gameId     unique id used to identify the specific game.
     * @param houseIndex represents the index of house where the game will continue from.
     * @return the updated game.
     */
    Game playRound(String gameId, Integer houseIndex);
}
