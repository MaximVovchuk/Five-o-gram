package com.fivesysdev.Fiveogram.exceptions;

public class Status444NotYourStoryException extends CustomException{
    public static final int CODE = 444;
    public Status444NotYourStoryException() {
        super(CODE, "This is not your story");
    }
}
