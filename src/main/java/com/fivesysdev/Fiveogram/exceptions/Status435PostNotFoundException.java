package com.fivesysdev.Fiveogram.exceptions;

public class Status435PostNotFoundException extends CustomException {
    public static final int CODE = 435;

    public Status435PostNotFoundException() {
        super(CODE, "Post was not found");
    }
}
