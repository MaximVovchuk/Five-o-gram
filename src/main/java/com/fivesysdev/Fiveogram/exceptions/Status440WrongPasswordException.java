package com.fivesysdev.Fiveogram.exceptions;

public class Status440WrongPasswordException extends CustomException {
    public static final int CODE = 440;

    public Status440WrongPasswordException() {
        super(CODE, "", "Wrong password!");
    }
}
