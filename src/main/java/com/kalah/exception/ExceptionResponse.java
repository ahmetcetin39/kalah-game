package com.kalah.exception;

import lombok.Value;

/**
 * This is the response object to return when exception is thrown.
 *
 * @author Ahmet Cetin
 * @since 2020-02-16
 */
@Value(staticConstructor = "of")
class ExceptionResponse {
    private String message;
}
