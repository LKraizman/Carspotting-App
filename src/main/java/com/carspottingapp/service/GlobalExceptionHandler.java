package com.carspottingapp.service;

import com.carspottingapp.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<String> handleInvalidPasswordException(InvalidPasswordException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidIdException.class)
    public ResponseEntity<String> handleInvalidIdException(InvalidIdException invalididException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(invalididException.getMessage());
    }

    @ExceptionHandler(InvalidLengthException.class)
    public ResponseEntity<String> handleInvalidLengthException(InvalidLengthException invalidLengthException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(invalidLengthException.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<String> handleUserAlreadyExistException(UserAlreadyExistException userAlreadyExistException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userAlreadyExistException.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException userNotFoundException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userNotFoundException.getMessage());
    }
}

