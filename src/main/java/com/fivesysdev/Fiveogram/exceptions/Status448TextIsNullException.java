package com.fivesysdev.Fiveogram.exceptions;

public class Status448TextIsNullException extends CustomException{
    public static final int CODE = 448;
    public Status448TextIsNullException() {
        super(CODE,"Text is null");
    }
}
