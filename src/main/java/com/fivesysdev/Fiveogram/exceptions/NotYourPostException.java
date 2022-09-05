package com.fivesysdev.Fiveogram.exceptions;

public class NotYourPostException extends RuntimeException{
    @Override
    public String getMessage() {
        return "That`s not your post";
    }
}
