package com.fivesysdev.Fiveogram.exceptions;

public class Status436SponsorNotFoundException extends CustomException {
    public static final int CODE = 436;

    public Status436SponsorNotFoundException() {
        super(CODE, "Your sponsor isn`t found");
    }
}
