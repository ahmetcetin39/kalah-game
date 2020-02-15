package com.kalah.exception;

/**
 * This is the custom exception to throw when game initiator is not valid.
 *
 * @author Ahmet Cetin
 * @since 2020-02-12
 */
public class GameInitiatorException extends RuntimeException {
    public GameInitiatorException(String message) {
        super(message);
    }
}
