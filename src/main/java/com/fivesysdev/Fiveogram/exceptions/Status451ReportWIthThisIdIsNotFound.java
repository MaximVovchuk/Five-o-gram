package com.fivesysdev.Fiveogram.exceptions;

public class Status451ReportWIthThisIdIsNotFound extends CustomException{
    public static final int CODE = 451;
    public Status451ReportWIthThisIdIsNotFound() {
        super(CODE,"Report with this entity id is not found");
    }
}
