package com.fivesysdev.Fiveogram.exceptions;

import org.springframework.http.HttpStatus;

public class PostNotFoundException extends Exception{
    public final HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    @Override
    public String getMessage() {
        return "Post was not found";
    }
}
