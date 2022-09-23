package com.fivesysdev.Fiveogram.exceptions;

public class Status404CommentNotFoundException extends CustomException {
    public static final int CODE = 404;

    public Status404CommentNotFoundException() {
        super(CODE, "Comment was not found");
    }
}
