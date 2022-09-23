package com.fivesysdev.Fiveogram.exceptions;

public class Status404SponsorNotFoundException extends CustomException {
    public static final int CODE = 404;

    public Status404SponsorNotFoundException() {
        super(CODE, "Your sponsor isn`t found");
    }
}
