package com.fivesysdev.Fiveogram.exceptions;

public class Status445StoryNotFoundException extends CustomException{
    public static final int CODE = 445;
    public Status445StoryNotFoundException() {
        super(CODE,"This story is not found");
    }
}
