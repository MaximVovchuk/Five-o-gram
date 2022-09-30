package com.fivesysdev.Fiveogram.exceptions;

public class Status439UsernameBusyException extends CustomException {
    public static final int CODE = 439;

    public Status439UsernameBusyException() {
        super(CODE, "This username is already taken");
    }
}
