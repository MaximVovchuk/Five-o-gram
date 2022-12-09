package com.fivesysdev.Fiveogram.exceptions;

public class Status452ChatRoomNotFoundException extends CustomException{
    public static final int CODE = 450;

    public Status452ChatRoomNotFoundException() {
        super(CODE,"This chatroom was not found");
    }
}
