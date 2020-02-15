package com.kalah.service;

import com.kalah.cache.GameCache;
import com.kalah.enumaration.GameInitiator;
import com.kalah.enumaration.GameStatus;
import com.kalah.exception.EmptyHouseException;
import com.kalah.exception.GameInitiatorException;
import com.kalah.exception.WrongPlayerException;
import com.kalah.model.Board;
import com.kalah.model.Game;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

import static com.kalah.model.Board.*;

/**
 * This is the service implementation of game service which contains the logic of Kalah game.
 *
 * @author Ahmet Cetin
 * @since 2020-01-16
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class GameServiceImpl implements GameService {
    private final GameCache gameCache;

    @Override
    public Game initBoard() {
        return Game.builder()
                .gameStatus(GameStatus.NOT_STARTED)
                .board(new Board(new int[14]))
                .build();
    }

    @Override
    public Game startGame(GameInitiator gameInitiator) {
        if (!gameInitiator.equals(GameInitiator.PLAYER_1) && !gameInitiator.equals(GameInitiator.PLAYER_2)) {
            throw new GameInitiatorException("Invalid game initiator. Game initiator should be either PLAYER_1 or PLAYER_2");
        }

        Game game = Game.builder()
                .id(UUID.randomUUID().toString())
                .board(new Board())
                .gameStatus(gameInitiator == GameInitiator.PLAYER_1 ? GameStatus.TURN_PLAYER_1 : GameStatus.TURN_PLAYER_2)
                .build();

        gameCache.putGameToCache(game);
        log.info("Game: " + game);

        return game;
    }

    @Override
    public Game playRound(String gameId, Integer houseIndex) {
        Game game = gameCache.getGameFromCache(gameId);
        validateRoundPlayer(game.getGameStatus(), houseIndex, game.getBoard().getHouses());

        game = play(game, houseIndex);

        gameCache.updateGameInCache(game);
        log.info("Game: " + game);
        return game;
    }

    private Game play(Game game, Integer houseIndex) {
        int[] houses = game.getBoard().getHouses();
        int stoneCountInHand = houses[houseIndex];  // Collect all stones in house to hand.
        houses[houseIndex] = 0;                     // Empty the house
        int currentIndex = houseIndex + 1;          // Start putting the stones from the next house.

        GameStatus gameStatus = null;

        while (stoneCountInHand > 0) {
            houses[currentIndex]++; // Put a stone
            stoneCountInHand--;     // Remove one stone from hand

            if (stoneCountInHand == 0) {
                // Handle last stone
                handleLastStonePutInEmptyHouse(houses, currentIndex, game.getGameStatus());
                if (isGameFinished(houses)) {
                    return getGameWithWinner(game, houses);
                }
                gameStatus = getGameStatus(currentIndex, game.getGameStatus());
            }

            currentIndex = getNextHouseIndex(game.getGameStatus(), currentIndex);
        }

        return Game.builder()
                .id(game.getId())
                .board(new Board(houses))
                .gameStatus(gameStatus)
                .build();
    }

    private void validateRoundPlayer(GameStatus gameStatus, Integer houseIndex, int[] houses) {
        if (gameStatus.equals(GameStatus.TURN_PLAYER_1) && !isFirstPlayerHouse(houseIndex)
                || gameStatus.equals(GameStatus.TURN_PLAYER_2) && !isSecondPlayerHouse(houseIndex)) {
            throw new WrongPlayerException("This is not the round for this player! GameStatus: " + gameStatus + ", houseIndex: " + houseIndex);
        }

        if (houses[houseIndex] == 0) {
            throw new EmptyHouseException("This house has 0 stones in it! Please select a house which contains stones.");
        }
    }

    private Game getGameWithWinner(Game game, int[] houses) {
        // Collect all remaining stones.
        houses[PLAYER_1_STORE_INDEX] += Arrays.stream(houses, 0, PLAYER_1_STORE_INDEX).sum();
        houses[PLAYER_2_STORE_INDEX] += Arrays.stream(houses, PLAYER_1_STORE_INDEX + 1, PLAYER_2_STORE_INDEX).sum();

        // Assign 0 to all houses.
        for (int i = 0; i < PLAYER_2_STORE_INDEX; i++) {
            if (i != PLAYER_1_STORE_INDEX) {
                houses[i] = 0;
            }
        }

        // Find winner
        String winner;
        if (houses[PLAYER_1_STORE_INDEX] < houses[PLAYER_2_STORE_INDEX]) {
            winner = "PLAYER_2";
        } else if (houses[PLAYER_2_STORE_INDEX] < houses[PLAYER_1_STORE_INDEX]) {
            winner = "PLAYER_1";
        } else {
            winner = "EQUAL";
        }

        return Game.builder()
                .id(game.getId())
                .board(game.getBoard())
                .gameStatus(GameStatus.FINISHED)
                .winner(winner)
                .build();
    }

    private int getNextHouseIndex(GameStatus gameStatus, int currentIndex) {
        int nextIndex;
        if (gameStatus.equals(GameStatus.TURN_PLAYER_1)) {
            nextIndex = currentIndex == (PLAYER_2_STORE_INDEX - 1) ? MIN_HOUSE_INDEX : (currentIndex + 1);
        } else { // TURN PLAYER 2
            if (currentIndex == PLAYER_1_STORE_INDEX - 1) { // Jump over PLAYER_1's store
                nextIndex = PLAYER_1_STORE_INDEX + 1;
            } else if (currentIndex == PLAYER_2_STORE_INDEX) {
                nextIndex = MIN_HOUSE_INDEX;
            } else {
                nextIndex = currentIndex + 1;
            }
        }
        return nextIndex;
    }

    private GameStatus getGameStatus(int lastIndex, GameStatus gameStatus) {
        if (gameStatus.equals(GameStatus.TURN_PLAYER_1)) {
            return lastIndex == PLAYER_1_STORE_INDEX ? GameStatus.TURN_PLAYER_1 : GameStatus.TURN_PLAYER_2;
        } else {
            return lastIndex == PLAYER_2_STORE_INDEX ? GameStatus.TURN_PLAYER_2 : GameStatus.TURN_PLAYER_1;
        }
    }

    private void handleLastStonePutInEmptyHouse(int[] houses, int lastIndex, GameStatus gameStatus) {
        if (houses[lastIndex] != 1) {
            return;
        }

        if (gameStatus.equals(GameStatus.TURN_PLAYER_1) && isFirstPlayerHouse(lastIndex)) {
            houses[PLAYER_1_STORE_INDEX] += houses[lastIndex] + houses[12 - lastIndex]; // Collect stones from both sides to the store
            houses[lastIndex] = 0;      // Empty player's side house
            houses[12 - lastIndex] = 0; // Empty opponent's side house
        } else if (gameStatus.equals(GameStatus.TURN_PLAYER_2) && isSecondPlayerHouse(lastIndex)) {
            houses[PLAYER_2_STORE_INDEX] += houses[lastIndex] + houses[12 - lastIndex]; // Collect stones from both sides to the store
            houses[lastIndex] = 0;      // Empty player's side house
            houses[12 - lastIndex] = 0; // Empty opponent's side house
        }
    }
}
