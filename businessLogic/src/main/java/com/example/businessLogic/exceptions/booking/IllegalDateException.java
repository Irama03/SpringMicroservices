package com.example.businessLogic.exceptions.booking;

import java.util.Date;

public class IllegalDateException extends RuntimeException {

    public IllegalDateException(Date end) {
        super("Illegal dates supplied: end date [" + end.toString() + "] is before start date");
    }
}
