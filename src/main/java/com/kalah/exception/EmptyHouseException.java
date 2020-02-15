package com.kalah.exception;

/**
 * This is the custom exception to throw when player somehow chooses an empty house to start with.
 *
 * @author Ahmet Cetin
 * @since 2020-02-12
 */
public class EmptyHouseException extends RuntimeException {
    public EmptyHouseException(String message) {
        super(message);
    }
}
