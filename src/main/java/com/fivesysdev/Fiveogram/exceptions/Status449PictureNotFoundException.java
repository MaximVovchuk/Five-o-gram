package com.fivesysdev.Fiveogram.exceptions;

public class Status449PictureNotFoundException extends CustomException{
    public static final int CODE = 449;
    public Status449PictureNotFoundException() {
        super(CODE,"Picture was not found");
    }
}
