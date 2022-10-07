package com.fivesysdev.Fiveogram.exceptions;

public class Status431SubscriptionException extends CustomException {
    public static final int CODE = 431;

    public Status431SubscriptionException(String ex) {
        super(CODE, ex);
    }
}
