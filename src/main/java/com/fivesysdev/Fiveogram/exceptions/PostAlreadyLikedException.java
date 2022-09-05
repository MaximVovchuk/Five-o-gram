package com.fivesysdev.Fiveogram.exceptions;

public class PostAlreadyLikedException extends RuntimeException{
    @Override
    public String getMessage() {
        return "You`ve already liked this post";
    }
}
