package com.kalah.cache;

import com.kalah.enumaration.GameStatus;
import com.kalah.exception.ResourceNotFoundException;
import com.kalah.model.Game;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * These are the unit tests of {@link GameCache}
 *
 * @author Ahmet Cetin
 * @since 2020-02-15
 */
@RunWith(MockitoJUnitRunner.class)
public class GameCacheTest {
    @InjectMocks
    private GameCache gameCache;

    @Test
    public void putGameToCacheTest() {
        Game game = Game.newGame(GameStatus.TURN_PLAYER_1);

        gameCache.putGameToCache(game);

        assertEquals(game, gameCache.getGameFromCache(game.getId()));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateGameInCache_whenGameNotFound_thenThrowResourceNotFoundException() {
        gameCache.updateGameInCache(Game.newGame(GameStatus.TURN_PLAYER_1));
    }

    @Test
    public void updateGameInCache_whenGameExists_thenUpdateWithNewGame() {
        Game game = Game.newGame(GameStatus.TURN_PLAYER_1);
        gameCache.putGameToCache(game);

        assertEquals(GameStatus.TURN_PLAYER_1, gameCache.getGameFromCache(game.getId()).getGameStatus());

        Game updatedGame = Game.builder()
                .id(game.getId())
                .gameStatus(GameStatus.TURN_PLAYER_2)
                .build();

        gameCache.updateGameInCache(updatedGame);

        assertEquals(GameStatus.TURN_PLAYER_2, gameCache.getGameFromCache(game.getId()).getGameStatus());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getGameFromCache_whenGameNotFound_thenThrowResourceNotFoundException() {
        gameCache.getGameFromCache("nonExistingGameId");
    }

    @Test
    public void getGameFromCache_whenGameFound_thenReturnGame() {
        Game game = Game.newGame(GameStatus.TURN_PLAYER_1);
        gameCache.putGameToCache(game);

        assertEquals(game, gameCache.getGameFromCache(game.getId()));
    }
}
