package com.fivesysdev.Fiveogram.exceptions;

public class Status431FriendshipException extends CustomException {
    public static final int CODE = 431;

    public Status431FriendshipException(String ex) {
        super(CODE, ex);
    }
}
