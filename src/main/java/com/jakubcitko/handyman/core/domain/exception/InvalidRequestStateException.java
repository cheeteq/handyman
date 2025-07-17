package com.jakubcitko.handyman.core.domain.exception;

public class InvalidRequestStateException extends DomainOperationException {
    public InvalidRequestStateException(String message) {
        super(message);
    }
}
