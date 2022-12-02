package com.fivesysdev.Fiveogram.exceptions;

public class Status551ReportWIthThisIdIsNotFound extends CustomException{
    public static final int CODE = 451;
    public Status551ReportWIthThisIdIsNotFound() {
        super(CODE,"Report with this entity id is not found");
    }
}
