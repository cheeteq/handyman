package com.jakubcitko.handyman.core.domain.exception;

public class BusinessRuleViolationException extends DomainOperationException {
    public BusinessRuleViolationException(String message) {
        super(message);
    }
    public BusinessRuleViolationException(String messagePattern, Object... args) {
        super(messagePattern, args);
    }
}
