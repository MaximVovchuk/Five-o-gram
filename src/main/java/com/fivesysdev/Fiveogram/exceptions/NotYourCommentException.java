package com.fivesysdev.Fiveogram.exceptions;


public class NotYourCommentException extends Exception{

    @Override
    public String getMessage() {
        return "That`s not your comment";
    }
}
