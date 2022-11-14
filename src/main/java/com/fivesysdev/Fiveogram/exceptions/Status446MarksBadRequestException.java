package com.fivesysdev.Fiveogram.exceptions;

public class Status446MarksBadRequestException extends CustomException {
    public static final int CODE = 446;

    public Status446MarksBadRequestException() {
        super(CODE, "Something wrong with marks fields");
    }
}
