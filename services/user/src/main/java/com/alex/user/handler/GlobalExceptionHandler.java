package com.alex.user.handler;

import com.alex.user.exception.UserPermissionException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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
            ErrorResponse errorResponse = new ErrorResponse("A user with this username already exists",
                    HttpStatus.BAD_REQUEST.value(),
                    LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        // If it's not duplicate username rethrow exception
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Database error, make sure all required fields are not null",
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

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException exception){
        ErrorResponse errorResponse = new ErrorResponse("Invalid username or password",
                                        HttpStatus.UNAUTHORIZED.value(),
                                        LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(UserPermissionException.class)
    public ResponseEntity<ErrorResponse> handleUserPermissionException(UserPermissionException exception){
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(),
                HttpStatus.FORBIDDEN.value(),
                LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
}
