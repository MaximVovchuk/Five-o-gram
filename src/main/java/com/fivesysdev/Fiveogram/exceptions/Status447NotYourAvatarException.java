package com.fivesysdev.Fiveogram.exceptions;

public class Status447NotYourAvatarException extends CustomException {
    public static final int CODE = 447;

    public Status447NotYourAvatarException() {
        super(CODE, "This is not your avatar");
    }
}
