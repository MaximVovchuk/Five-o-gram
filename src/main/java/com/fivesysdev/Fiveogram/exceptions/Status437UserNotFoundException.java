package com.fivesysdev.Fiveogram.exceptions;

public class Status437UserNotFoundException extends CustomException {
    public static final int CODE = 437;

    public Status437UserNotFoundException() {
        super(CODE, "User was not found");
    }
}
