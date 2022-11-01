package com.fivesysdev.Fiveogram.exceptions;

public class Status441FileIsNullException extends CustomException {
    public static final int CODE = 441;

    public Status441FileIsNullException() {
        super(CODE, "File exception");
    }
}
