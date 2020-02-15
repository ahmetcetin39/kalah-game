package com.kalah.controller;

import com.kalah.enumaration.GameInitiator;
import com.kalah.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import static com.kalah.model.Board.*;

/**
 * This is the controller where Kalah game endpoints exist.
 *
 * @author Ahmet Cetin
 * @since 2020-01-16
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class GameController {
    private final GameService gameService;

    @PostMapping("/init")
    public ResponseEntity getBoard() {
        log.info("Board is initialized successfully!");
        return ResponseEntity.status(HttpStatus.CREATED).body(gameService.initBoard());
    }

    @PostMapping("/start/{gameInitiator}")
    public ResponseEntity startGame(@PathVariable GameInitiator gameInitiator) {
        return ResponseEntity.ok().body(gameService.startGame(gameInitiator));
    }

    @PutMapping("/games/{gameId}/play/{houseIndex}")
    public ResponseEntity playRound(@PathVariable String gameId, @PathVariable Integer houseIndex) {
        if (StringUtils.isEmpty(gameId) || houseIndex == null) {
            throw new IllegalArgumentException("Both gameId and houseIndex should be provided!");
        }

        if (houseIndex < MIN_HOUSE_INDEX || PLAYER_2_STORE_INDEX <= houseIndex || houseIndex == PLAYER_1_STORE_INDEX) {
            throw new IllegalArgumentException("House index should be between 0 and 13 and not 6!");
        }

        return ResponseEntity.ok().body(gameService.playRound(gameId, houseIndex));
    }
}
