package com.jakubcitko.handyman.core.domain.exception;

public class EmailAlreadyUsedException extends DomainOperationException {

    public EmailAlreadyUsedException(String messagePattern, Object... args) {
        super(messagePattern, args);
    }
}
