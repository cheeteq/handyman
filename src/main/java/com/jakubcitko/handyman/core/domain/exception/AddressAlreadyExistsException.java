package com.jakubcitko.handyman.core.domain.exception;

public class AddressAlreadyExistsException extends DomainOperationException {
    public AddressAlreadyExistsException(String message) {
        super(message);
    }
}
