package com.jakubcitko.handyman.core.application.port.in;

import java.util.UUID;

public interface RegisterCustomerUseCase {

    void registerCustomer(RegisterCustomerCommand command);

    record RegisterCustomerCommand(
            String displayName,
            String phoneNumber,
            String email,
            String password
    ) {
    }
}
