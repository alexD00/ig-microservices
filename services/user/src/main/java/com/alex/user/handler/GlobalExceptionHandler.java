package com.alex.user.handler;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException exception){
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmail(DataIntegrityViolationException exception){
        if (exception.getMessage().contains("duplicate key value violates unique constraint")){
            ErrorResponse errorResponse = new ErrorResponse("A user with this email already exists",
                    HttpStatus.BAD_REQUEST.value(),
                    LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        // If it's not duplicate email rethrow exception
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Database error: " + exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now()));
    }

    // Handle for @Valid exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception){
        ErrorResponse errorResponse = new ErrorResponse(Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
