package com.fivesysdev.Fiveogram.exceptions;

public class Status443DidNotReceivePictureException extends CustomException{
    public static final int CODE = 443;
    public Status443DidNotReceivePictureException() {
        super(CODE, "Did not receive picture!");
    }
}
