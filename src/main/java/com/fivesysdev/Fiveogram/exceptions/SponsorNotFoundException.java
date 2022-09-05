package com.fivesysdev.Fiveogram.exceptions;

public class SponsorNotFoundException extends RuntimeException{
    @Override
    public String getMessage() {
        return "Your sponsor isn`t found";
    }
}
