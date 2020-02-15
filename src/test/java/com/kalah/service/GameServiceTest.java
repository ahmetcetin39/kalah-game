package com.kalah.service;

import com.kalah.cache.GameCache;
import com.kalah.enumaration.GameInitiator;
import com.kalah.enumaration.GameStatus;
import com.kalah.exception.EmptyHouseException;
import com.kalah.exception.GameInitiatorException;
import com.kalah.exception.WrongPlayerException;
import com.kalah.model.Board;
import com.kalah.model.Game;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static com.kalah.model.Board.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * These are the unit tests of {@link GameService}
 *
 * @author Ahmet Cetin
 * @since 2020-02-15
 */
@RunWith(MockitoJUnitRunner.class)
public class GameServiceTest {
    private static final GameCache gameCache = mock(GameCache.class);

    @InjectMocks
    private GameServiceImpl gameService;

    @Test
    public void initBoardTest() {
        assertEquals(gameService.initBoard(), Game.emptyGame());
    }

    @Test(expected = GameInitiatorException.class)
    public void startGame_whenGameInitiatorIsNotValid_thenThrowGameInitiatorException() {
        gameService.startGame(null);
    }

    @Test
    public void startGame_whenGameInitiatorIsValid_thenStartGame() {
        Game game = gameService.startGame(GameInitiator.PLAYER_2);
        assertNotNull(game);
        assertEquals(GameStatus.TURN_PLAYER_2, game.getGameStatus());
    }

    @Test(expected = WrongPlayerException.class)
    public void playRound_whenWrongPlayer_thenThrowWrongPlayerException() {
        Game game = gameService.startGame(GameInitiator.PLAYER_1);
        when(gameCache.getGameFromCache(game.getId())).thenReturn(game);
        gameService.playRound(game.getId(), 9);
    }

    @Test(expected = EmptyHouseException.class)
    public void playRound_whenHousesIsEmpty_thenThrowEmptyHouseException() {
        Game game = gameService.startGame(GameInitiator.PLAYER_1);
        when(gameCache.getGameFromCache(game.getId())).thenReturn(game);
        gameService.playRound(game.getId(), 0);

        //Play same house which has 0 stones in it.
        gameService.playRound(game.getId(), 0);
    }

    @Test
    public void playRound_whenGameEnds_returnWinner() {
        Game game = gameService.startGame(GameInitiator.PLAYER_1);

        // Manipulate board so that only last round will be left.
        // With this values, PLAYER1 will have 31, PLAYER2 will have 17 stones in store so PLAYER1 should win.
        int[] almostFinishedBoard = new int[]{0, 0, 0, 0, 0, 1, 30, 0, 0, 0, 0, 0, 5, 12};

        Game manipulatedGame = Game.builder()
                .id(game.getId())
                .gameStatus(game.getGameStatus())
                .board(new Board((almostFinishedBoard)))
                .build();

        when(gameCache.getGameFromCache(game.getId())).thenReturn(manipulatedGame);

        Game finishedGame = gameService.playRound(manipulatedGame.getId(), 5);
        for (int i = MIN_HOUSE_INDEX; i < PLAYER_2_STORE_INDEX; i++) {
            if (i != PLAYER_1_STORE_INDEX) {
                assertEquals(0, finishedGame.getBoard().getHouses()[i]);
            }
        }

        assertEquals(31, finishedGame.getBoard().getHouses()[PLAYER_1_STORE_INDEX]);
        assertEquals(17, finishedGame.getBoard().getHouses()[PLAYER_2_STORE_INDEX]);
        assertEquals(GameStatus.FINISHED, finishedGame.getGameStatus());
        assertEquals("PLAYER_1", finishedGame.getWinner());
    }

    @Test
    public void playRound_whenLastStoneIsPutToEmptyHouse_thenCollectStonesFromBothSides() {
        Game game = gameService.startGame(GameInitiator.PLAYER_1);

        // Manipulate board so that last stone will be put to an empty house.
        // If 4th index is played by PLAYER1, last stone will be put to 5th house which was empty before.
        // Therefore, 5th and 7th indexes' stones will be put to PLAYER1's store.
        int[] newBoard = new int[]{0, 0, 0, 0, 1, 0, 30, 5, 0, 0, 0, 0, 5, 7};

        Game manipulatedGame = Game.builder()
                .id(game.getId())
                .gameStatus(game.getGameStatus())
                .board(new Board((newBoard)))
                .build();

        when(gameCache.getGameFromCache(game.getId())).thenReturn(manipulatedGame);

        Game playedGame = gameService.playRound(manipulatedGame.getId(), 4);

        assertEquals(0, playedGame.getBoard().getHouses()[5]);
        assertEquals(0, playedGame.getBoard().getHouses()[7]);
        assertEquals(36, playedGame.getBoard().getHouses()[6]);
    }

    @Test
    public void playRound_whenLastStoneIsPutToOwnStore_thenGameStatusDoesntChange() {
        Game game = gameService.startGame(GameInitiator.PLAYER_1);

        when(gameCache.getGameFromCache(game.getId())).thenReturn(game);

        // At the beginning 2nd index has 4 stones and last stone will be put to PLAYER1's store.
        game = gameService.playRound(game.getId(), 2);

        assertEquals(GameStatus.TURN_PLAYER_1, game.getGameStatus());
    }
}
