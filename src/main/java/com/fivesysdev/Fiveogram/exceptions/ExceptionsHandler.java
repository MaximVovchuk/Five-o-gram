package com.fivesysdev.Fiveogram.exceptions;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleAlreadyExistException(CustomException ex) {
        return ResponseEntity.status(ex.code).body(ex.getMessage());
    }
    @ExceptionHandler(SizeLimitExceededException.class)
    public ResponseEntity<Object> handleException(SizeLimitExceededException ex){
        return ResponseEntity.status(430).body("File is too big");
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleException(UsernameNotFoundException ex){
        return ResponseEntity.status(428).body("User with this username is not found");
    }
}
