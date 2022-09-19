package com.fivesysdev.Fiveogram.exceptions;

public class Status402FriendshipException extends CustomException {
    public static final int CODE = 402;

    public Status402FriendshipException(String ex) {
        super(CODE,ex);
    }
}
