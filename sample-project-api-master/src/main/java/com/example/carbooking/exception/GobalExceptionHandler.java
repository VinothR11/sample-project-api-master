package com.example.carbooking.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GobalExceptionHandler {
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<String> handleConflict(ConflictException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValditionException(MethodArgumentNotValidException ex){
        StringBuilder message = new StringBuilder("validation failed: ");
        for (FieldError error : ex.getBindingResult().getFieldErrors()){
            message.append(error.getField()).append(" ").append(error.getDefaultMessage()).append(";");
        }

        return new ResponseEntity<>(message.toString(), HttpStatus.BAD_REQUEST);
    }
}

