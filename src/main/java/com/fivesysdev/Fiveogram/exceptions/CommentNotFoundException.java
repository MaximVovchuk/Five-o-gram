package com.fivesysdev.Fiveogram.exceptions;

public class CommentNotFoundException extends RuntimeException{
    @Override
    public String getMessage() {
        return "Comment was not found";
    }
}
