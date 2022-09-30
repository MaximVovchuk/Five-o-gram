package com.fivesysdev.Fiveogram.exceptions;

public class Status438PostAlreadyLikedException extends CustomException {
    public static final int CODE = 438;

    public Status438PostAlreadyLikedException() {
        super(CODE, "You`ve already liked this post");
    }
}
