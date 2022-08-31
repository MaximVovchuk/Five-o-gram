package com.fivesysdev.Fiveogram.exceptions;

public class UserNotFoundException extends RuntimeException{
    @Override
    public String getMessage() {
        return "User was not found";
    }
}
