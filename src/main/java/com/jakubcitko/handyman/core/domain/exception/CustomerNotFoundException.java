package com.jakubcitko.handyman.core.domain.exception;

public class CustomerNotFoundException extends DomainOperationException {
    public CustomerNotFoundException(String message) {
        super(message);
    }
    public CustomerNotFoundException(String messagePattern, Object... args) {
        super(messagePattern, args);
    }
}
