package com.jakubcitko.handyman.core.application.port.in;

import java.util.UUID;

public interface AddAddressToCustomerUseCase {
    void addAddressToCustomer(AddAddressToCustomerCommand command);

    record AddAddressToCustomerCommand(
            UUID customerId,
            AddressData addressData
    ) {
    }

    record AddressData(
            String street,
            String streetNumber,
            String flatNumber,
            String city,
            String postalCode
    ) {

    }
}
