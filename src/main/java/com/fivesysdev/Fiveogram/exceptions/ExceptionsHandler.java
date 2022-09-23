package com.fivesysdev.Fiveogram.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<Object> handleException(CustomException e) {
        ExceptionText exceptionText = new ExceptionText(
                HttpStatus.valueOf(e.code),
                e.getMessage()
        );
        return new ResponseEntity<>(exceptionText, exceptionText.getHttpStatus());
    }
}
