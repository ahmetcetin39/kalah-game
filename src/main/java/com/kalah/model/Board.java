package com.kalah.model;

import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * This is the model of the Game Board of Kalah.
 * Kalah Board contains 6 houses and 1 store per player.
 *
 * @author Ahmet Cetin
 * @since 2020-01-16
 */
@Getter
@ToString
public class Board {
    public static final int MIN_HOUSE_INDEX = 0;
    public static final int PLAYER_1_STORE_INDEX = 6;
    public static final int PLAYER_2_STORE_INDEX = 13;
    public static final int INITIAL_STONE_COUNT_PER_HOUSE = 4;

    private int[] houses = new int[14];

    public Board() {
        IntStream.range(MIN_HOUSE_INDEX, PLAYER_1_STORE_INDEX)
                .forEach(i -> houses[i] = INITIAL_STONE_COUNT_PER_HOUSE);
        IntStream.range(PLAYER_1_STORE_INDEX + 1, PLAYER_2_STORE_INDEX)
                .forEach(i -> houses[i] = INITIAL_STONE_COUNT_PER_HOUSE);
    }

    public Board(int[] houses) {
        this.houses = houses;
    }

    public static boolean isFirstPlayerHouse(Integer houseIndex) {
        return MIN_HOUSE_INDEX <= houseIndex && houseIndex < PLAYER_1_STORE_INDEX;
    }

    public static boolean isSecondPlayerHouse(Integer houseIndex) {
        return PLAYER_1_STORE_INDEX + 1 <= houseIndex && houseIndex < PLAYER_2_STORE_INDEX;
    }

    public static boolean isGameFinished(int[] houses) {
        return Arrays.stream(houses, 0, PLAYER_1_STORE_INDEX).sum() == 0 ||
                Arrays.stream(houses, PLAYER_1_STORE_INDEX + 1, PLAYER_2_STORE_INDEX).sum() == 0;
    }
}
