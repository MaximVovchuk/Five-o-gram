package com.fivesysdev.Fiveogram.exceptions;

public class Status406UsernameBusyException extends CustomException{
    public static final int CODE = 406;
    public Status406UsernameBusyException(){
        super(CODE,"This username is already taken");
    }
}
