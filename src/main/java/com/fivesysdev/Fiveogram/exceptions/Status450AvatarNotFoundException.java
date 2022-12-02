package com.fivesysdev.Fiveogram.exceptions;

public class Status450AvatarNotFoundException extends CustomException{
    public static final int CODE = 450;

    public Status450AvatarNotFoundException() {
        super(CODE,"This avatar is not found");
    }
}
