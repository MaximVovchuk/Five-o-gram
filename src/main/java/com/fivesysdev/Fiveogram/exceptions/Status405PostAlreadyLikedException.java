package com.fivesysdev.Fiveogram.exceptions;

public class Status405PostAlreadyLikedException extends CustomException {
    public static final int CODE = 405;

    public Status405PostAlreadyLikedException() {
        super(CODE, "You`ve already liked this post");
    }
}
