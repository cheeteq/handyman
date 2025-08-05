package com.jakubcitko.handyman.core.application.port.in;

import com.jakubcitko.handyman.adapters.inbound.web.dto.response.CustomerRequestDetailsResponseDto;

import java.util.Optional;
import java.util.UUID;

public interface GetCustomerRequestDetailsUseCase {
    Optional<CustomerRequestDetailsResponseDto> getDetails(GetCustomerRequestDetailsQuery query);

    record GetCustomerRequestDetailsQuery(
            UUID serviceRequestId,
            UUID requesterId
    ) {}
}
