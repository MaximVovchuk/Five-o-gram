package com.fivesysdev.Fiveogram.exceptions;

public class Status451ReportWithThisIdIsNotFound extends CustomException{
    public static final int CODE = 451;
    public Status451ReportWithThisIdIsNotFound() {
        super(CODE,"Report with this entity id is not found");
    }
}
