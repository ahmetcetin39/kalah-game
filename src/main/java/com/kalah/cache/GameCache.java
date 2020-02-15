package com.kalah.cache;

import com.kalah.exception.ResourceNotFoundException;
import com.kalah.model.Game;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is the cache component to store multiple games in the same map and identify them by their id.
 *
 * @author Ahmet Cetin
 * @since 2020-01-16
 */
@Component
public class GameCache {
    private static final Map<String, Game> gameMap = new ConcurrentHashMap<>();

    public void putGameToCache(Game game) {
        gameMap.put(game.getId(), game);
    }

    public void updateGameInCache(Game game) {
        var gameInCache = gameMap.get(game.getId());
        if (gameInCache == null) {
            throw new ResourceNotFoundException("No game found with id: " + game.getId());
        }
        putGameToCache(game);
    }

    public Game getGameFromCache(String gameId) {
        var gameInCache = gameMap.get(gameId);
        if (gameInCache == null) {
            throw new ResourceNotFoundException("No game found with id: " + gameId);
        }
        return gameInCache;
    }
}
