package com.fivesysdev.Fiveogram.exceptions;

public class Status453NotYourChatRoomException extends CustomException {
    public static final int CODE = 453;

    public Status453NotYourChatRoomException() {
        super(CODE,"That`s not your chatroom");
    }
}
