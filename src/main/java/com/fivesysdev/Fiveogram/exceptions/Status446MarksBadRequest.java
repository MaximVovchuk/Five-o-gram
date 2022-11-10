package com.fivesysdev.Fiveogram.exceptions;

public class Status446MarksBadRequest extends CustomException {
    public static final int CODE = 446;

    public Status446MarksBadRequest() {
        super(CODE, "Something wrong with marks fields");
    }
}
