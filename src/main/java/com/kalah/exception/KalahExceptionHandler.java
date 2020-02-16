package com.kalah.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * This is the exception handler where custom exceptions are customized.
 *
 * @author Ahmet Cetin
 * @since 2020-02-12
 */
@ControllerAdvice
@Slf4j
public class KalahExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String HANDLING_EXCEPTION_MESSAGE = "Handling exception: ";

    @ExceptionHandler({GameInitiatorException.class, WrongPlayerException.class, EmptyHouseException.class, IllegalArgumentException.class})
    public final ResponseEntity handleBadRequest(Exception e) {
        log.info(HANDLING_EXCEPTION_MESSAGE + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.of(e.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity handleResourceNotFoundException(ResourceNotFoundException e) {
        log.info(HANDLING_EXCEPTION_MESSAGE + e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionResponse.of(e.getMessage()));
    }
}
