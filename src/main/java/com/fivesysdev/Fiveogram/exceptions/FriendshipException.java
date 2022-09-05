package com.fivesysdev.Fiveogram.exceptions;

public class FriendshipException extends RuntimeException {
    private final String message;

    public FriendshipException(String ex) {
        message = ex;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
