package com.fivesysdev.Fiveogram.exceptions;

public class Status455WrongCodeException extends CustomException {
    public static final int CODE = 455;

    public Status455WrongCodeException() {
        super(CODE, "That`s not the code!");
    }
}
