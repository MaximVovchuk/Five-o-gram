package com.fivesysdev.Fiveogram.exceptions;

public class UsernameBusyException extends RuntimeException{
    @Override
    public String getMessage() {
        return "This username is already taken";
    }
}
