package com.fivesysdev.Fiveogram.exceptions;

public class PostAlreadyLikedException extends Exception{
    @Override
    public String getMessage() {
        return "You`ve already liked this post";
    }
}
