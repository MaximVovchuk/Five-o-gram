package com.fivesysdev.Fiveogram.exceptions;

public class CommentNotFoundException extends Exception{
    @Override
    public String getMessage() {
        return "Comment was not found";
    }
}
