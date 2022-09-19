package com.fivesysdev.Fiveogram.exceptions;

public class Status407WrongPasswordException extends CustomException{
    public static final int CODE = 407;
    public Status407WrongPasswordException(){
        super(CODE,"","Wrong password!");
    }
}
