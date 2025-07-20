package com.jakubcitko.handyman.core.application.port.in;

import com.jakubcitko.handyman.adapters.inbound.web.dto.AddressResponseDto;

import java.util.List;
import java.util.UUID;

public interface GetCustomerAddressesUseCase {
    List<AddressResponseDto> getAddressesForCustomer(GetCustomerAddressesQuery query);

    record GetCustomerAddressesQuery(
            UUID customerId
    ) {
    }
}
