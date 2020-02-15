package com.kalah.exception;

/**
 * This is the custom exception to throw when wrong player tries to play round.
 *
 * @author Ahmet Cetin
 * @since 2020-02-12
 */
public class WrongPlayerException extends RuntimeException {
    public WrongPlayerException(String message) {
        super(message);
    }
}
