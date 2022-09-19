package com.fivesysdev.Fiveogram.exceptions;

import org.springframework.http.HttpStatus;

public class ExceptionText {
    private final HttpStatus httpStatus;
    private final String message;

    public ExceptionText(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
