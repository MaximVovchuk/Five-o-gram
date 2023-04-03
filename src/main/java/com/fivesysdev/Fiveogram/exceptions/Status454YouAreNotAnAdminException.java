package com.fivesysdev.Fiveogram.exceptions;

public class Status454YouAreNotAnAdminException extends CustomException{
    public static final int CODE = 454;

    public Status454YouAreNotAnAdminException() {
        super(CODE,"You`re not an admin!");
    }
}
