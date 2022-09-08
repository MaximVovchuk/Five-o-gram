package com.fivesysdev.Fiveogram.exceptions;

public class FriendshipException extends Exception {
    private final String message;

    public FriendshipException(String ex) {
        message = ex;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
