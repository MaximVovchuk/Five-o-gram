package com.fivesysdev.Fiveogram.exceptions;

public class Status433NotYourPostException extends CustomException {
    public static final int CODE = 433;

    public Status433NotYourPostException() {
        super(CODE, "That`s not your post");
    }
}
