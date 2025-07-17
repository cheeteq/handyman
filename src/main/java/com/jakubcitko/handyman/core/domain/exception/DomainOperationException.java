package com.jakubcitko.handyman.core.domain.exception;

public abstract class DomainOperationException extends RuntimeException {
    public DomainOperationException(String message) {
        super(message);
    }
}