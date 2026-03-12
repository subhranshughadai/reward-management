package com.charter.rewardmanagement.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

/**
 * Global exception handler for the application.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation related exceptions.
     *
     * @param ex exception message
     * @return bad request response
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleValidation(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    /**
     * Handles invalid date format exceptions.
     *
     * @return bad request response with error message
     */
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateError() {
        return ResponseEntity.badRequest().body("Invalid date format");
    }
}