package com.jakubcitko.handyman.core.domain.exception;

public class TimeSlotMismatchException extends DomainOperationException {
    public TimeSlotMismatchException(String message) {
        super(message);
    }
}
