package com.kalah.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalah.enumaration.GameInitiator;
import com.kalah.enumaration.GameStatus;
import com.kalah.model.Game;
import com.kalah.service.GameService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * These are the unit tests of {@link GameController}
 *
 * @author Ahmet Cetin
 * @since 2020-02-15
 */
@RunWith(SpringRunner.class)
@WebMvcTest(GameController.class)
class GameControllerTest {
    private static final String ENDPOINT_INIT_BOARD = "/init";
    private static final String ENDPOINT_START_GAME = "/start/{gameInitiator}";
    private static final String ENDPOINT_PLAY_ROUND = "/games/{gameId}/play/{houseIndex}";
    private static final String GAME_INITIATOR_PLAYER1 = "PLAYER_1";
    private static final String GAME_INITIATOR_PLAYER2 = "PLAYER_2";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private GameService gameService;

    @Test
    public void getBoardTest() throws Exception {
        Game game = Game.emptyGame();
        when(gameService.initBoard()).thenReturn(game);

        mockMvc.perform(post(ENDPOINT_INIT_BOARD))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(game)));
    }

    @Test
    public void startGame_whenPlayer1Starts_thenReturnGameWithStatusPlayer1() throws Exception {
        Game game = Game.newGame(GameStatus.TURN_PLAYER_1);

        when(gameService.startGame(GameInitiator.PLAYER_1)).thenReturn(game);

        mockMvc.perform(post(ENDPOINT_START_GAME, GAME_INITIATOR_PLAYER1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(game)));
    }

    @Test
    public void startGame_whenPlayer2Starts_thenReturnGameWithStatusPlayer2() throws Exception {
        Game game = Game.newGame(GameStatus.TURN_PLAYER_2);

        when(gameService.startGame(GameInitiator.PLAYER_2)).thenReturn(game);

        mockMvc.perform(post(ENDPOINT_START_GAME, GAME_INITIATOR_PLAYER2))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(game)));
    }

    @Test
    public void playRound_whenHouseIndexIsBelowZero_thenBadRequest() throws Exception {
        mockMvc.perform(put(ENDPOINT_PLAY_ROUND, "test", -1))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void playRound_whenHouseIndexIsPlayer1Store_thenBadRequest() throws Exception {
        mockMvc.perform(put(ENDPOINT_PLAY_ROUND, "test", 6))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void playRound_whenHouseIndexIsAboveLimit_thenBadRequest() throws Exception {
        mockMvc.perform(put(ENDPOINT_PLAY_ROUND, "test", 14))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void playRound_whenParametersAreValid_thenReturnGame() throws Exception {
        Game game = Game.newGame(GameStatus.TURN_PLAYER_1);

        when(gameService.playRound(anyString(), anyInt())).thenReturn(game);

        mockMvc.perform(put(ENDPOINT_PLAY_ROUND, "dummyGameId", 0))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(game)));
    }
}
