package com.fivesysdev.Fiveogram.exceptions;

public class Status434CommentNotFoundException extends CustomException {
    public static final int CODE = 434;

    public Status434CommentNotFoundException() {
        super(CODE, "Comment was not found");
    }
}
