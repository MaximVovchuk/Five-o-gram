package com.fivesysdev.Fiveogram.exceptions;

public class Status404UserNotFoundException extends CustomException{
    public static final int CODE = 404;
    public Status404UserNotFoundException(){
        super(CODE,"User was not found");
    }
}
