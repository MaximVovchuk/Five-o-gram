package com.fivesysdev.Fiveogram.exceptions;


public class NotYourCommentException extends RuntimeException{

    @Override
    public String getMessage() {
        return "That`s not your comment";
    }
}
