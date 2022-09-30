package com.fivesysdev.Fiveogram.exceptions;


public class Status432NotYourCommentException extends CustomException {
    public static final int CODE = 432;

    public Status432NotYourCommentException() {
        super(CODE, "That`s not your comment");
    }
}
