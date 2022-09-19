package com.fivesysdev.Fiveogram.exceptions;

public class Status408FileException extends CustomException{
    public static final int CODE = 408;

    public Status408FileException() {
        super(CODE,"File exception");
    }
}
