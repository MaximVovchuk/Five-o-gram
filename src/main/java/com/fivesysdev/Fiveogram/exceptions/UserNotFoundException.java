package com.fivesysdev.Fiveogram.exceptions;

public class UserNotFoundException extends Exception{
    @Override
    public String getMessage() {
        return "User was not found";
    }
}
