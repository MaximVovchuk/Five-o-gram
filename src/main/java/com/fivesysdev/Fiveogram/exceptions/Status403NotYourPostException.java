package com.fivesysdev.Fiveogram.exceptions;

public class Status403NotYourPostException extends CustomException {
    public static final int CODE = 403;

    public Status403NotYourPostException() {
        super(CODE, "That`s not your post");
    }
}
