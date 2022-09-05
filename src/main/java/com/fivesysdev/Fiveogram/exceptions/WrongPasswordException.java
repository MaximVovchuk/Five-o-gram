package com.fivesysdev.Fiveogram.exceptions;

public class WrongPasswordException extends RuntimeException{
    @Override
    public String getMessage() {
        return "Wrong password!";
    }
}
