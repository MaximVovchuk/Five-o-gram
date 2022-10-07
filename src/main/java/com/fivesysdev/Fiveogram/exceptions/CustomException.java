package com.fivesysdev.Fiveogram.exceptions;

public class CustomException extends Exception {
    public int code;

    public CustomException(int code, String message) {
        super(message);
        this.code = code;
    }
}
