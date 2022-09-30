package com.fivesysdev.Fiveogram.exceptions;

public class Status441FileException extends CustomException {
    public static final int CODE = 441;

    public Status441FileException() {
        super(CODE, "File exception");
    }
}
