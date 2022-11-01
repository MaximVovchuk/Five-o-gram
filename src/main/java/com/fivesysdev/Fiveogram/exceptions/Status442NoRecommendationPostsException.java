package com.fivesysdev.Fiveogram.exceptions;

public class Status442NoRecommendationPostsException extends CustomException{
    public static final int CODE = 442;

    public Status442NoRecommendationPostsException() {
        super(CODE, "You have no recommended posts!");
    }
}
