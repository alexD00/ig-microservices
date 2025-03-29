package com.alex.action.handler;

import com.alex.action.exception.InvalidActionException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidActionException.class)
    public ResponseEntity<ErrorResponse> handleUserPermissionException(InvalidActionException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(),
                HttpStatus.FORBIDDEN.value(),
                LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
}
