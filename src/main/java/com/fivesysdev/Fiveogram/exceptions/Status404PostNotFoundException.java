package com.fivesysdev.Fiveogram.exceptions;

public class Status404PostNotFoundException extends CustomException{
    public static final int CODE = 404;
    public Status404PostNotFoundException(){
        super(CODE,"Post was not found");
    }
}
