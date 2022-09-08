package com.fivesysdev.Fiveogram.exceptions;

public class UsernameBusyException extends Exception{
    @Override
    public String getMessage() {
        return "This username is already taken";
    }
}
