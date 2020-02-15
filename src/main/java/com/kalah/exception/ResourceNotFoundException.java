package com.kalah.exception;

/**
 * This is the custom exception to throw when game is not found with the given id.
 *
 * @author Ahmet Cetin
 * @since 2020-02-12
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
