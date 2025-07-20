package com.jakubcitko.handyman.core.domain.exception;

public abstract class DomainOperationException extends RuntimeException {
    public DomainOperationException(String message) {
        super(message);
    }
    public DomainOperationException(String messagePattern, Object... args) {
        super(String.format(messagePattern, args));
    }
}