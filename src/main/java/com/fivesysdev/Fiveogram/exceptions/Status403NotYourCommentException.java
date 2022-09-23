package com.fivesysdev.Fiveogram.exceptions;


public class Status403NotYourCommentException extends CustomException {
    public static final int CODE = 403;

    public Status403NotYourCommentException() {
        super(CODE, "That`s not your comment");
    }
}
