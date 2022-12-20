package com.fivesysdev.Fiveogram.exceptions;

public class Status442NoFeedPostsException extends CustomException{
    public static final int CODE = 442;

    public Status442NoFeedPostsException() {
        super(CODE, "You have no feed posts!");
    }
}
